package engine.shapes

import engine.entities.animations.fuckBlender
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
import engine.shapes.simplexes.Simplex2v3d
import engine.solveCubic
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

typealias Edge3f = Pair<Ray3f, Ray3f>

open class Mesh(val indices : IntArray, val vdata : FloatArray) : EnigMesh {

	constructor(mesh : AIMesh) : this(mesh.mFaces().getArrayFast(), mesh.mVertices().getArray(fuckBlender))

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

		if (!Box3d(minX, minY, minZ, maxX, maxY, maxZ).touches(Box3d(oMinX, oMinY, oMinZ, oMaxX, oMaxY, oMaxZ))) {
			return null
		}

		val bounds = Box3d(minX.coerceAtLeast(oMinX), maxX.coerceAtMost(oMaxX), minY.coerceAtLeast(oMinY), maxY.coerceAtMost(oMaxY), minZ.coerceAtLeast(oMinZ), maxZ.coerceAtMost(oMaxZ))

		val rays : ArrayList<Ray3f> = ArrayList()
		val simplices : ArrayList<Pair<Simplex2v3d, Simplex2v3d>> = ArrayList()
		val oRays : ArrayList<Ray3f> = ArrayList()
		val oSimplices : ArrayList<Pair<Simplex2v3d, Simplex2v3d>> = ArrayList()
		val edges : ArrayList<Edge3f> = ArrayList()
		val oEdges : ArrayList<Edge3f> = ArrayList()

		val addedEdges = Array(vertexCount) {Array(it) {false}}
		val oAddedEdges = Array(vertexCount) {Array(it) {false}}

		fun Edge3f.inBounds() = bounds().touches(bounds)
		fun Pair<Simplex2v3d, Simplex2v3d>.inBounds() = first.intersect(second).touches(bounds)

		fun endPoint(i : Int) = Vector3f(endBuffer[getXi(i)], endBuffer[getYi(i)], endBuffer[getZi(i)])
		fun oEndPoint(i : Int) = Vector3f(oEndBuffer[other.getXi(i)], oEndBuffer[other.getYi(i)], oEndBuffer[other.getZi(i)])

		fun addEdge(a : Int, b : Int) {
			val max = a.coerceAtLeast(b)
			val min = a.coerceAtMost(b)
			if (!addedEdges[max][min]) {
				val edge = Pair(Ray3f.between(getVertex(a), getVertex(b)), Ray3f.between(endPoint(a), endPoint(b)))
				if (edge.inBounds()) {
					edges.add(edge)
					addedEdges[max][min] = true
				}
			}
		}
		fun addOEdge(a : Int, b : Int) {
			val max = a.coerceAtLeast(b)
			val min = a.coerceAtMost(b)
			if (!oAddedEdges[max][min]) {
				val edge = Pair(Ray3f.between(other.getVertex(a), other.getVertex(b)), Ray3f.between(oEndPoint(a), oEndPoint(b)))
				if (edge.inBounds()) {
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
			val simplexPair = Pair(getSimplex(triangle), Simplex2v3d(endPoint(ai), endPoint(bi), endPoint(ci)))
			addEdge(ai, bi)
			addEdge(bi, ci)
			addEdge(ci, ai)
			if (bounds.touches(ra)) rays.add(ra)
			if (bounds.touches(rb)) rays.add(rb)
			if (bounds.touches(rc)) rays.add(rc)

			if (simplexPair.inBounds()) simplices.add(simplexPair)
		}

		for (triangle in 0 until other.triangleCount) {
			val ai = other.getAi(triangle)
			val bi = other.getBi(triangle)
			val ci = other.getCi(triangle)
			val ra = Ray3f.between(other.getVertex(ai), oEndPoint(ai))
			val rb = Ray3f.between(other.getVertex(bi), oEndPoint(bi))
			val rc = Ray3f.between(other.getVertex(ci), oEndPoint(ci))
			val simplexPair = Pair(other.getSimplex(triangle), Simplex2v3d(oEndPoint(ai), oEndPoint(bi), oEndPoint(ci)))
			addOEdge(ai, bi)
			addOEdge(bi, ci)
			addOEdge(ci, ai)
			if (bounds.touches(ra)) oRays.add(ra)
			if (bounds.touches(rb)) oRays.add(rb)
			if (bounds.touches(rc)) oRays.add(rc)

			if (simplexPair.inBounds()) oSimplices.add(simplexPair)
		}
		//val rays
		//val simplices
		//val oRays
		//val oSimplices
		//val edges
		//val oEdges

		var t : Float? = null
		for (ray in rays) {
			for (simpPair in oSimplices) {
				t = mino(t, triangleIntersectionT(simpPair.first, ray, simpPair.second))
			}
		}
		for (ray in oRays) {
			for (simpPair in simplices) {
				t = mino(t, triangleIntersectionT(simpPair.first, ray, simpPair.second))
			}
		}

		for (edge in edges) {
			for (oEdge in oEdges) {
				t = mino(t, edgesIntersectionT(edge, oEdge))
			}
		}

		return null
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

private fun Edge3f.bounds() = first.intersect(second)

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
		return t.takeIf { Ray2f.doesIntersectT(es.x, es.y, ed.x, ed.y, os.x, os.y, od.x, od.y) != null}
	}

	return solveCubic(zdiffA, zdiffB, zdiffC, zdiffD) {a, b, c ->
		mino(mino(validIntersection(a), validIntersection(b)), validIntersection(c))
	}
}

fun edgesIntersectionT(e : Pair<Ray3f, Ray3f>, o : Pair<Ray3f, Ray3f>) : Float? {
	val ea : Vector3f = e.first
	val ead = e.first.delta
	val eb : Vector3f = e.second
	val ebd = e.second.delta
	val oa : Vector3f = o.first
	val oad = o.first.delta
	val ob : Vector3f = o.second
	val obd = o.second.delta
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
	/*
        float rx = y * v.z() - z * v.y()
        float ry = z * v.x() - x * v.z()
        float rz = x * v.y() - y * v.x()
	 */
	//this.x * v.x() + this.y * v.y() + this.z * v.z()
	//rx = (s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)
	//rx = ((s.a.y + t * ady - s.c.y - t * cdy) * (s.b.z + t * bdz - s.c.z - t * cdz) - (s.a.z + t * adz - s.c.z - t * cdz) * (s.b.y + t * bdy - s.c.y - t * cdy))
	//ry = ((s.a.z + t * adz - s.c.z - t * cdz) * (s.b.x + t * bdx - s.c.x - t * cdx) - (s.a.x + t * adx - s.c.x - t * cdx) * (s.b.z + t * bdz - s.c.z - t * cdz))
	//rz = ((s.a.x + t * adx - s.c.x - t * cdx) * (s.b.y + t * bdy - s.c.y - t * cdy) - (s.a.y + t * ady - s.c.y - t * cdy) * (s.b.x + t * bdx - s.c.x - t * cdx))
	//0 =
	//(p.x - s.a.x - t * adx) * rx + (p.y - s.a.y - t * ady) * ry + (p.z - s.a.z - t * adz) * rz

	/*
	(p.x - s.a.x - t * adx) * ((s.a.y + t * ady - s.c.y - t * cdy) * (s.b.z + t * bdz - s.c.z - t * cdz) - (s.a.z + t * adz - s.c.z - t * cdz) * (s.b.y + t * bdy - s.c.y - t * cdy)) +
			(p.y - s.a.y - t * ady) * ((s.a.z + t * adz - s.c.z - t * cdz) * (s.b.x + t * bdx - s.c.x - t * cdx) - (s.a.x + t * adx - s.c.x - t * cdx) * (s.b.z + t * bdz - s.c.z - t * cdz)) +
			(p.z - s.a.z - t * adz) * ((s.a.x + t * adx - s.c.x - t * cdx) * (s.b.y + t * bdy - s.c.y - t * cdy) - (s.a.y + t * ady - s.c.y - t * cdy) * (s.b.x + t * bdx - s.c.x - t * cdx))
*/
/*
	(p.x - s.a.x - t * adx) * ((s.a.y - s.c.y + t * (ady - cdy)) * (s.b.z - s.c.z + t * (bdz - cdz)) - (s.a.z - s.c.z + t * (adz - cdz)) * (s.b.y - s.c.y + t * (bdy - cdy)))
	(p.x - s.a.x - t * adx) * (((s.a.y - s.c.y) + t * (ady - cdy)) * ((s.b.z - s.c.z) + t * (bdz - cdz)) - ((s.a.z - s.c.z) + t * (adz - cdz)) * ((s.b.y - s.c.y) + t * (bdy - cdy)))
	((p.x - s.a.x) - t * adx) * (((s.a.y - s.c.y) + t * (ady - cdy)) * ((s.b.z - s.c.z) + t * (bdz - cdz)) - ((s.a.z - s.c.z) + t * (adz - cdz)) * ((s.b.y - s.c.y) + t * (bdy - cdy)))

	((s.a.y - s.c.y) + t * (ady - cdy)) * ((s.b.z - s.c.z) + t * (bdz - cdz))
	t * t * (ady - cdy) * (bdz - cdz) + t * ((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y)) + (s.a.y - s.c.y) * (s.b.z - s.c.z)

	((s.a.z - s.c.z) + t * (adz - cdz)) * ((s.b.y - s.c.y) + t * (bdy - cdy))
	t * t * (adz - cdz) * (bdy - cdy) + t * ((adz - cdz) * (s.b.y - s.c.y) + (bdy - cdy) * (s.a.z - s.c.z)) + (s.a.z - s.c.z) * (s.b.y - s.c.y)
*/
	/*
	t * t * ((ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)) +
			t * ((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) - (adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z)) +
			(s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)
*/
	//- t * t * t * ((ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)) * adx +
	//		t * t * (((ady - cdy) * (bdz - cdz) - (adz - cdz) * (bdy - cdy)) * (p.x - s.a.x) - adx * ((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) - (adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z))) +
	//		t * (((ady - cdy) * (s.b.z - s.c.z) + (bdz - cdz) * (s.a.y - s.c.y) - (adz - cdz) * (s.b.y - s.c.y) - (bdy - cdy) * (s.a.z - s.c.z)) * (p.x - s.a.x) - adx * ((s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)))
	//		((s.a.y - s.c.y) * (s.b.z - s.c.z) - (s.a.z - s.c.z) * (s.b.y - s.c.y)) * (p.x - s.a.x)


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