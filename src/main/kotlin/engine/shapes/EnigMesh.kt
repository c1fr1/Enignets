package engine.shapes

import engine.loadScene
import engine.mino
import engine.opengl.bufferObjects.getArray
import engine.opengl.bufferObjects.getArrayFast
import engine.opengl.jomlExtensions.minus
import engine.opengl.jomlExtensions.plus
import engine.opengl.jomlExtensions.times
import engine.shapes.rays.Ray2f
import engine.shapes.rays.Ray3f
import engine.shapes.bounds.Bound3f
import engine.shapes.bounds.Box3d
import engine.shapes.bounds.calcBounds
import engine.shapes.simplexes.Simplex2v3d
import engine.solveCubic
import org.joml.Math.fma
import org.joml.Vector3f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AIScene
import java.io.File
import java.lang.Math.*
import kotlin.collections.ArrayList

interface EnigMesh : Bound3f {

	val vertexCount : Int

	val triangleCount : Int

	fun getAX(i : Int) : Float
	fun getAY(i : Int) : Float
	fun getAZ(i : Int) : Float
	fun getBX(i : Int) : Float
	fun getBY(i : Int) : Float
	fun getBZ(i : Int) : Float
	fun getCX(i : Int) : Float
	fun getCY(i : Int) : Float
	fun getCZ(i : Int) : Float

	fun getA(i : Int) = Vector3f(getAX(i), getAY(i), getAZ(i))
	fun getB(i : Int) = Vector3f(getBX(i), getBY(i), getBZ(i))
	fun getC(i : Int) = Vector3f(getCX(i), getCY(i), getCZ(i))

	fun getSimplex(i : Int) = Simplex2v3d(getA(i), getB(i), getC(i))

	fun getNormal(i : Int) : Vector3f {
		val c = getC(i)
		return (getA(i) - c).cross(getB(i) - c).normalize()
	}

	fun getXYDoubleArea(i : Int) =
		fma(getAX(i), getBY(i) - getCY(i), getCX(i) * (getAY(i) - getBY(i))) - getBX(i) * (getAY(i) + getCY(i))

	fun getXZDoubleArea(i : Int) =
		fma(getAX(i), getBZ(i) - getCZ(i), getCX(i) * (getAZ(i) - getBZ(i))) - getBX(i) * (getAZ(i) + getCZ(i))

	fun getYZDoubleArea(i : Int) =
		fma(getAZ(i), getBY(i) - getCY(i), getCZ(i) * (getAY(i) - getBY(i))) - getBZ(i) * (getAY(i) + getCY(i))
}

open class Mesh(val indices : IntArray, val vdata : FloatArray) : EnigMesh {

	constructor(mesh : AIMesh, counterRotate : Boolean = false) : this(mesh.mFaces().getArrayFast(), mesh.mVertices().getArray(counterRotate))

	constructor(scene : AIScene, index : Int) : this(AIMesh.create(scene.mMeshes()!![index]))

	constructor(path : String, index : Int) : this(loadScene(path), index)

	override val vertexCount = vdata.size / 3

	override val triangleCount = indices.size / 3

	override val minx: Float
		get() = vdata.foldIndexed(Float.POSITIVE_INFINITY) {i, ret, f -> if (i % 3 == 0) ret.coerceAtMost(f) else ret}
	override val maxx: Float
		get() = vdata.foldIndexed(Float.NEGATIVE_INFINITY) {i, ret, f -> if (i % 3 == 0) ret.coerceAtLeast(f) else ret}
	override val miny: Float
		get() = vdata.foldIndexed(Float.POSITIVE_INFINITY) {i, ret, f -> if (i % 3 == 1) ret.coerceAtMost(f) else ret}
	override val maxy: Float
		get() = vdata.foldIndexed(Float.NEGATIVE_INFINITY) {i, ret, f -> if (i % 3 == 1) ret.coerceAtLeast(f) else ret}
	override val minz: Float
		get() = vdata.foldIndexed(Float.POSITIVE_INFINITY) {i, ret, f -> if (i % 3 == 2) ret.coerceAtMost(f) else ret}
	override val maxz: Float
		get() = vdata.foldIndexed(Float.NEGATIVE_INFINITY) {i, ret, f -> if (i % 3 == 2) ret.coerceAtLeast(f) else ret}

	open fun getX(i : Int) = vdata[getXi(i)]
	open fun getY(i : Int) = vdata[getYi(i)]
	open fun getZ(i : Int) = vdata[getZi(i)]
	open fun getVertex(i : Int) = Vector3f(getX(i), getY(i), getZ(i))

	open fun getXi(i : Int) = i * 3
	open fun getYi(i : Int) = i * 3 + 1
	open fun getZi(i : Int) = i * 3 + 2

	override fun getAX(i: Int) = getX(getAi(i))
	override fun getAY(i: Int) = getY(getAi(i))
	override fun getAZ(i: Int) = getZ(getAi(i))
	override fun getBX(i: Int) = getX(getBi(i))
	override fun getBY(i: Int) = getY(getBi(i))
	override fun getBZ(i: Int) = getZ(getBi(i))
	override fun getCX(i: Int) = getX(getCi(i))
	override fun getCY(i: Int) = getY(getCi(i))
	override fun getCZ(i: Int) = getZ(getCi(i))

	open fun getAi(i : Int) = indices[i * 3]
	open fun getBi(i : Int) = indices[i * 3 + 1]
	open fun getCi(i : Int) = indices[i * 3 + 2]

	open fun intersectionT(other : Mesh, endBuffer : FloatArray, oEndBuffer : FloatArray) : Float? {
		COLLISION_COMPARISONS_PT = 0
		COLLISION_COMPARISONS_EE = 0
		var minX = Float.MAX_VALUE
		var maxX = Float.MIN_VALUE
		var minY = Float.MAX_VALUE
		var maxY = Float.MIN_VALUE
		var minZ = Float.MAX_VALUE
		var maxZ = Float.MIN_VALUE

		for (i in 0 until vertexCount) {
			minX = minX.coerceAtMost(getX(i).coerceAtMost(endBuffer[getXi(i)]))
			maxX = maxX.coerceAtLeast(getX(i).coerceAtLeast(endBuffer[getXi(i)]))
			minY = minY.coerceAtMost(getY(i).coerceAtMost(endBuffer[getYi(i)]))
			maxY = maxY.coerceAtLeast(getY(i).coerceAtLeast(endBuffer[getYi(i)]))
			minZ = minZ.coerceAtMost(getZ(i).coerceAtMost(endBuffer[getZi(i)]))
			maxZ = maxZ.coerceAtLeast(getZ(i).coerceAtLeast(endBuffer[getZi(i)]))
		}

		var oMinX = Float.MAX_VALUE
		var oMaxX = Float.MIN_VALUE
		var oMinY = Float.MAX_VALUE
		var oMaxY = Float.MIN_VALUE
		var oMinZ = Float.MAX_VALUE
		var oMaxZ = Float.MIN_VALUE

		for (i in 0 until vertexCount) {
			oMinX = oMinX.coerceAtMost(other.getX(i).coerceAtMost(oEndBuffer[getXi(i)]))
			oMaxX = oMaxX.coerceAtLeast(other.getX(i).coerceAtLeast(oEndBuffer[getXi(i)]))
			oMinY = oMinY.coerceAtMost(other.getY(i).coerceAtMost(oEndBuffer[getYi(i)]))
			oMaxY = oMaxY.coerceAtLeast(other.getY(i).coerceAtLeast(oEndBuffer[getYi(i)]))
			oMinZ = oMinZ.coerceAtMost(other.getZ(i).coerceAtMost(oEndBuffer[getZi(i)]))
			oMaxZ = oMaxZ.coerceAtLeast(other.getZ(i).coerceAtLeast(oEndBuffer[getZi(i)]))
		}

		val box = Box3d(minX, minY, minZ, maxX, maxY, maxZ)
		val oBox = Box3d(oMinX, oMinY, oMinZ, oMaxX, oMaxY, oMaxZ)

		if (!box.touches(oBox)) {
			return null
		}

		val bounds = box.intersect(oBox)

		val rays : ArrayList<Ray3f> = ArrayList()
		val simplices : ArrayList<SimpPair> = ArrayList()
		val oRays : ArrayList<Ray3f> = ArrayList()
		val oSimplices : ArrayList<SimpPair> = ArrayList()
		val edges : ArrayList<Edge3f> = ArrayList()
		val oEdges : ArrayList<Edge3f> = ArrayList()

		val addedEdges = Array(vertexCount) {Array(it) {false}}
		val oAddedEdges = Array(vertexCount) {Array(it) {false}}

		fun endPoint(i : Int) = Vector3f(endBuffer[getXi(i)], endBuffer[getYi(i)], endBuffer[getZi(i)])
		fun oEndPoint(i : Int) = Vector3f(oEndBuffer[other.getXi(i)], oEndBuffer[other.getYi(i)], oEndBuffer[other.getZi(i)])

		fun addEdge(a : Int, b : Int) {
			val max = a.coerceAtLeast(b)
			val min = a.coerceAtMost(b)
			if (!addedEdges[max][min]) {
				val edge = Edge3f(Ray3f.between(getVertex(a), getVertex(b)), Ray3f.between(endPoint(a), endPoint(b)))
				if (edge.touches(bounds)) {
					edges.add(edge)
					addedEdges[max][min] = true
				}
			}
		}
		fun addOEdge(a : Int, b : Int) {
			val max = a.coerceAtLeast(b)
			val min = a.coerceAtMost(b)
			if (!oAddedEdges[max][min]) {
				val edge = Edge3f(Ray3f.between(other.getVertex(a), other.getVertex(b)), Ray3f.between(oEndPoint(a), oEndPoint(b)))
				if (edge.touches(bounds)) {
					oEdges.add(edge)
					oAddedEdges[max][min] = true
				}
			}
		}

		for (triangle in 0 until triangleCount) {
			val ai = getAi(triangle)
			val bi = getBi(triangle)
			val ci = getCi(triangle)
			val ra = Ray3f.between(getVertex(ai), endPoint(ai))
			val rb = Ray3f.between(getVertex(bi), endPoint(bi))
			val rc = Ray3f.between(getVertex(ci), endPoint(ci))
			val simplexPair = SimpPair(getSimplex(triangle), Simplex2v3d(endPoint(ai), endPoint(bi), endPoint(ci)))
			addEdge(ai, bi)
			addEdge(bi, ci)
			addEdge(ci, ai)
			if (bounds.touches(ra)) rays.add(ra)
			if (bounds.touches(rb)) rays.add(rb)
			if (bounds.touches(rc)) rays.add(rc)

			if (simplexPair.touches(bounds)) simplices.add(simplexPair)
		}

		for (triangle in 0 until other.triangleCount) {
			val ai = other.getAi(triangle)
			val bi = other.getBi(triangle)
			val ci = other.getCi(triangle)
			val ra = Ray3f.between(other.getVertex(ai), oEndPoint(ai))
			val rb = Ray3f.between(other.getVertex(bi), oEndPoint(bi))
			val rc = Ray3f.between(other.getVertex(ci), oEndPoint(ci))
			val simplexPair = SimpPair(other.getSimplex(triangle), Simplex2v3d(oEndPoint(ai), oEndPoint(bi), oEndPoint(ci)))
			addOEdge(ai, bi)
			addOEdge(bi, ci)
			addOEdge(ci, ai)
			if (bounds.touches(ra)) oRays.add(ra)
			if (bounds.touches(rb)) oRays.add(rb)
			if (bounds.touches(rc)) oRays.add(rc)

			if (simplexPair.touches(bounds)) oSimplices.add(simplexPair)
		}
		//val rays
		//val simplices
		//val oRays
		//val oSimplices
		//val edges
		//val oEdges

		fun collisionTPT(rays : List<Ray3f>, simps : List<SimpPair>) : Float? {
			COLLISION_COMPARISONS_PT += rays.size * simps.size
			var t : Float? = null
			for (ray in rays) {
				for (simpPair in simps) {
					val tempT = triangleIntersectionT(simpPair.a, ray, simpPair.b)
					if (tempT != null && !ray.touches(simpPair)) println("failure of a meat sack")
					t = mino(t, tempT)
					if (t != null) if (t == 0f) return 0f
				}
			}
			return t
		}

		fun collisionTEE(a : List<Edge3f>, b : List<Edge3f>) : Float? {
			COLLISION_COMPARISONS_EE += a.size * b.size
			var t : Float? = null
			for (ae in a) {
				for (be in b) {
					val tempT = edgesIntersectionT(ae, be)
					if (tempT != null && !ae.touches(be)) {
						println("${ae.a.x} ${ae.a.y} ${ae.a.z} ${ae.a.dx} ${ae.a.dy} ${ae.a.dz}")
						println("${ae.b.x} ${ae.b.y} ${ae.b.z} ${ae.b.dx} ${ae.b.dy} ${ae.b.dz}")
						println("${be.a.x} ${be.a.y} ${be.a.z} ${be.a.dx} ${be.a.dy} ${be.a.dz}")
						println("${be.b.x} ${be.b.y} ${be.b.z} ${be.b.dx} ${be.b.dy} ${be.b.dz}\n")
					}
					t = mino(t, tempT)
					if (t != null) if (t!! < 0.001) return 0f
				}
			}
			return t
		}

		fun<T : Bound3f> cubeSplit(ts : List<T>) : ArrayList<List<T>> {
			val segments = arrayListOf(
				ts.filter { it.maxx < bounds.centerX },
				ts.filter { it.minx > bounds.centerX }
			).map { s ->
				arrayListOf(
					s.filter { it.maxy < bounds.centerY },
					s.filter { it.miny > bounds.centerY }
				).map { fs ->
					arrayListOf(
						fs.filter { it.maxz < bounds.centerZ },
						fs.filter { it.minz > bounds.centerZ }
					)
				}
			}
			val ret = ArrayList<List<T>>()
			segments.forEach { ylists -> ylists.forEach { ret.addAll(it) } }
			return ret
		}

		fun<A : Bound3f, B : Bound3f> cubeTreeCollision(a : List<A>, b : List<B>, bounds : Bound3f,
		                                                collisionFun : (List<A>, List<B>) -> Float?,
		                                                min : Int = 16) : Float? {
			if (a.size * b.size <= min) return collisionFun(a, b)

			val aSplits = cubeSplit(a)
			val bSplits = cubeSplit(b)

			if (aSplits.indices.any {i -> a.size == aSplits[i].size && b.size == bSplits[i].size})
				return collisionFun(a, b)
			return aSplits.indices.fold(null as Float?) {t, i ->
				val minx = if (i and 4 == 0) bounds.minx else bounds.centerX
				val miny = if (i and 2 == 0) bounds.miny else bounds.centerY
				val minz = if (i and 1 == 0) bounds.minz else bounds.centerZ
				val nw = bounds.width / 2
				val nh = bounds.height / 2
				val nd = bounds.depth / 2
				val smallerBounds = Box3d(minx, miny, minz, minx + nw, miny + nh, minz + nd)
				mino(t, cubeTreeCollision(aSplits[i], bSplits[i], smallerBounds, collisionFun))
			}
		}

		/*var t : Float? = mino(cubeTreeCollision(rays, oSimplices, bounds, {a, b -> collisionTPT(a, b)}),
			cubeTreeCollision(oRays, simplices, bounds, {a, b -> collisionTPT(a, b)}))

		t = mino(t, cubeTreeCollision(edges, oEdges, bounds, {a, b -> collisionTEE(a, b)}))*/
		var t : Float? = mino(collisionTPT(rays, oSimplices), collisionTPT(oRays, simplices))

		t = mino(t, collisionTEE(edges, oEdges))

		return t
	}

	open fun writeToOBJ(path : String) {
		val writer = File(path).printWriter()
		for (i in 0 until vertexCount) {
			writer.println("v ${getX(i)} ${getY(i)} ${getZ(i)}")
		}
		for (i in 0 until indices.size / 3) {
			writer.println("f ${indices[i * 3] + 1} ${indices[i * 3 + 1] + 1} ${indices[i * 3 + 2] + 1}")
		}
		writer.close()
	}
}

var COLLISION_COMPARISONS_PT = 0
var COLLISION_COMPARISONS_EE = 0

private class SimpPair(val a : Simplex2v3d, val b : Simplex2v3d) : Bound3f {
	override val minx: Float
		get() = a.minx.coerceAtMost(b.minx)
	override val maxx: Float
		get() = a.maxx.coerceAtLeast(b.maxx)
	override val miny: Float
		get() = a.miny.coerceAtMost(b.miny)
	override val maxy: Float
		get() = a.maxy.coerceAtLeast(b.maxy)
	override val minz: Float
		get() = a.minz.coerceAtMost(b.minz)
	override val maxz: Float
		get() = a.maxz.coerceAtLeast(b.maxz)
}

class Edge3f(val a : Ray3f, val b : Ray3f) : Bound3f {
	override val minx: Float
		get() = a.minx.coerceAtMost(b.minx)
	override val maxx: Float
		get() = a.maxx.coerceAtLeast(b.maxx)
	override val miny: Float
		get() = a.miny.coerceAtMost(b.miny)
	override val maxy: Float
		get() = a.maxy.coerceAtLeast(b.maxy)
	override val minz: Float
		get() = a.minz.coerceAtMost(b.minz)
	override val maxz: Float
		get() = a.maxz.coerceAtLeast(b.maxz)
}

fun edgesIntersectionT(ea : Vector3f, ead : Vector3f, eb : Vector3f, ebd : Vector3f, oa : Vector3f, oad : Vector3f, ob : Vector3f, obd : Vector3f) : Float? {
	val edA = ebd - ead
	val edB = ead

	val odA = obd - oad
	val odB = oad

	val eA = eb - ea
	val eB = ea

	val oA = ob - oa
	val oB = oa

	val sdelA = eA - oA
	val sdelB = eB - oB

	val etuA = sdelA.x * odA.y - odA.x * sdelA.y
	val etuB = sdelA.x * odB.y + sdelB.x * odA.y - odA.x * sdelB.y - odB.x * sdelA.y
	val etuC = sdelB.x * odB.y - odB.x * sdelB.y
	val etlA = edA.y * odA.x - edA.x * odA.y
	val etlB = edA.y * odB.x + edB.y * odA.x - edA.x * odB.y - edB.x * odA.y
	val etlC = edB.y * odB.x - edB.x * odB.y

	val otuA = edA.x * sdelA.y - sdelA.x * edA.y
	val otuB = edA.x * sdelB.y + edB.x * sdelA.y - sdelA.x * edB.y - sdelB.x * edA.y
	val otuC = edB.x * sdelB.y - sdelB.x * edB.y

	val zdiffA = sdelA.z * etlA + edA.z * etuA + odA.z * otuA
	val zdiffB = sdelA.z * etlB + sdelB.z * etlA + edA.z * etuB + edB.z * etuA + odA.z * otuB + odB.z * otuA
	val zdiffC = sdelA.z * etlC + sdelB.z * etlB + edA.z * etuC + edB.z * etuB + odA.z * otuC + odB.z * otuB
	val zdiffD = sdelB.z * etlC + edB.z * etuC + odB.z * otuC

	fun validIntersection(t : Float?) : Float? {
		if (t == null || t !in 0f..1f) return null
		val es = ea + t * (eb - ea)
		val ed = ead + t * (ead - ebd)
		val os = oa + t * (ob - oa)
		val od = oad + t * (oad - obd)
		val a = Ray3f(es, ed).closestDistance(Ray3f(os, od))//ABSOLUTELY NOT IDEAL
		return t.takeIf { kotlin.math.abs(a) > 0.01f && Ray2f.doesIntersectT(es.x, es.y, ed.x, ed.y, os.x, os.y, od.x, od.y) != null}
	}

	return solveCubic(zdiffA, zdiffB, zdiffC, zdiffD) {a, b, c ->
		mino(mino(validIntersection(a), validIntersection(b)), validIntersection(c))
	}
}

fun edgesIntersectionT(e : Edge3f, o : Edge3f) : Float? {
	val ea : Vector3f = e.a
	val ead = e.a.delta
	val eb : Vector3f = e.b
	val ebd = e.b.delta
	val oa : Vector3f = o.a
	val oad = o.a.delta
	val ob : Vector3f = o.b
	val obd = o.b.delta
	return edgesIntersectionT(ea, ead, eb, ebd, oa, oad, ob, obd)
}

fun triangleIntersectionT(s : Simplex2v3d, l : Ray3f, e : Simplex2v3d) : Float? {
	/*
	ax = s.a.x + t * (e.x - s.x)
	 */
	val adx = e.a.x - s.a.x
	val ady = e.a.y - s.a.y
	val adz = e.a.z - s.a.z
	val bdx = e.b.x - s.b.x
	val bdy = e.b.y - s.b.y
	val bdz = e.b.z - s.b.z
	val cdx = e.c.x - s.c.x
	val cdy = e.c.y - s.c.y
	val cdz = e.c.z - s.c.z

	val quadax = (ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)
	val quaday = (adz - cdz) * (bdx - cdx) - (adx - cdx) * (bdz - cdz)
	val quadaz = (adx - cdx) * (bdy - cdy) - (ady - cdy) * (bdx - cdx)
	val quadbx = (ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) -
			(adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z)
	val quadby = (adz - cdz) * (s.b.x - s.c.x) + (bdx - cdx) * (s.a.z - s.c.z) -
			(adx - cdx) * (s.b.z - s.c.z) - (bdz - cdz) * (s.a.x - s.c.x)
	val quadbz = (adx - cdx) * (s.b.y - s.c.y) + (bdy - cdy) * (s.a.x - s.c.x) -
			(ady - cdy) * (s.b.x - s.c.x) - (bdx - cdx) * (s.a.y - s.c.y)
	val quadcx = (s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)
	val quadcy = (s.a.z - s.c.z) * (s.b.x - s.c.x) - (s.a.x - s.c.x) * (s.b.z - s.c.z)
	val quadcz = (s.a.x - s.c.x) * (s.b.y - s.c.y) - (s.a.y - s.c.y) * (s.b.x - s.c.x)

	val psdx = l.x - s.a.x
	val psdy = l.y - s.a.y
	val psdz = l.z - s.a.z

	val a = quadax * (l.dx - adx) + quaday * (l.dy - ady) + quadaz * (l.dz - adz)
	val b = quadax * psdx + quaday * psdy + quadaz * psdz + (l.dx - adx) * quadbx + (l.dy - ady) * quadby + (l.dz - adz) * quadbz
	val c = quadbx * psdx + quadby * psdy + quadbz * psdz + (l.dx - adx) * quadcx + (l.dy - ady) * quadcy + (l.dz - adz) * quadcz
	val d = quadcx * psdx + quadcy * psdy + quadcz * psdz

	return solveCubic(a, b, c, d) {ansa, ansb, ansc ->
		val ansao = ansa.takeIf{it in 0f..1f}?.takeIf{s.lerp(e, it, Simplex2v3d()).contains(l.getPointAt(it))}
		val ansbo = ansb?.takeIf{it in 0f..1f}?.takeIf{s.lerp(e, it, Simplex2v3d()).contains(l.getPointAt(it))}
		val ansco = ansc?.takeIf{it in 0f..1f}?.takeIf{s.lerp(e, it, Simplex2v3d()).contains(l.getPointAt(it))}
		mino(ansao, mino(ansbo, ansco))
	}
}
/*
	float rx = y * v.z() - z * v.y()
	float ry = z * v.x() - x * v.z()
	float rz = x * v.y() - y * v.x()

this.x * v.x() + this.y * v.y() + this.z * v.z()
rx = (s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)
rx = ((s.a.y + t * ady - s.c.y - t * cdy) * (s.b.z + t * bdz - s.c.z - t * cdz) - (s.a.z + t * adz - s.c.z - t * cdz) * (s.b.y + t * bdy - s.c.y - t * cdy))
ry = ((s.a.z + t * adz - s.c.z - t * cdz) * (s.b.x + t * bdx - s.c.x - t * cdx) - (s.a.x + t * adx - s.c.x - t * cdx) * (s.b.z + t * bdz - s.c.z - t * cdz))
rz = ((s.a.x + t * adx - s.c.x - t * cdx) * (s.b.y + t * bdy - s.c.y - t * cdy) - (s.a.y + t * ady - s.c.y - t * cdy) * (s.b.x + t * bdx - s.c.x - t * cdx))
0 =
(p.x - s.a.x - t * adx) * rx + (p.y - s.a.y - t * ady) * ry + (p.z - s.a.z - t * adz) * rz

(p.x - s.a.x - t * adx) * ((s.a.y + t * ady - s.c.y - t * cdy) * (s.b.z + t * bdz - s.c.z - t * cdz) - (s.a.z + t * adz - s.c.z - t * cdz) * (s.b.y + t * bdy - s.c.y - t * cdy)) +
		(p.y - s.a.y - t * ady) * ((s.a.z + t * adz - s.c.z - t * cdz) * (s.b.x + t * bdx - s.c.x - t * cdx) - (s.a.x + t * adx - s.c.x - t * cdx) * (s.b.z + t * bdz - s.c.z - t * cdz)) +
		(p.z - s.a.z - t * adz) * ((s.a.x + t * adx - s.c.x - t * cdx) * (s.b.y + t * bdy - s.c.y - t * cdy) - (s.a.y + t * ady - s.c.y - t * cdy) * (s.b.x + t * bdx - s.c.x - t * cdx))


(p.x - s.a.x - t * adx) * ((s.a.y - s.c.y + t * (ady - cdy)) * (s.b.z - s.c.z + t * (bdz - cdz)) - (s.a.z - s.c.z + t * (adz - cdz)) * (s.b.y - s.c.y + t * (bdy - cdy)))
(p.x - s.a.x - t * adx) * (((s.a.y - s.c.y) + t * (ady - cdy)) * ((s.b.z - s.c.z) + t * (bdz - cdz)) - ((s.a.z - s.c.z) + t * (adz - cdz)) * ((s.b.y - s.c.y) + t * (bdy - cdy)))
((p.x - s.a.x) - t * adx) * (((s.a.y - s.c.y) + t * (ady - cdy)) * ((s.b.z - s.c.z) + t * (bdz - cdz)) - ((s.a.z - s.c.z) + t * (adz - cdz)) * ((s.b.y - s.c.y) + t * (bdy - cdy)))

((s.a.y - s.c.y) + t * (ady - cdy)) * ((s.b.z - s.c.z) + t * (bdz - cdz))
t * t * (ady - cdy) * (bdz - cdz) + t * ((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y)) + (s.a.y - s.c.y) * (s.b.z - s.c.z)

((s.a.z - s.c.z) + t * (adz - cdz)) * ((s.b.y - s.c.y) + t * (bdy - cdy))
t * t * (adz - cdz) * (bdy - cdy) + t * ((adz - cdz) * (s.b.y - s.c.y) + (bdy - cdy) * (s.a.z - s.c.z)) + (s.a.z - s.c.z) * (s.b.y - s.c.y)


t * t * ((ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)) +
		t * ((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) - (adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z)) +
		(s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)

- t * t * t * ((ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)) * adx +
		t * t * (((ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)) * (p.x - s.a.x) - adx * ((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) - (adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z))) +
		t * (((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) - (adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z)) * (p.x - s.a.x) - adx * ((s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)))
		((s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)) * (p.x - s.a.x)
*/