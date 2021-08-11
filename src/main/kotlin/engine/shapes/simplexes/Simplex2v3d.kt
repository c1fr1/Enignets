package engine.shapes.simplexes

import engine.mino
import engine.opengl.jomlExtensions.minus
import engine.opengl.jomlExtensions.plus
import engine.opengl.jomlExtensions.times
import engine.shapes.Bound3f
import engine.shapes.Rays.Ray2f
import engine.shapes.Rays.Ray3f
import org.joml.Vector3f
import org.joml.Vector2fc
import org.joml.Vector3fc
import java.lang.Float.max
import java.lang.Float.min
import java.lang.Math.fma
import kotlin.math.abs

@Suppress("JoinDeclarationAndAssignment", "unused", "MemberVisibilityCanBePrivate")
class Simplex2v3d : Bound3f {
	var a : Vector3f
	var b : Vector3f
	var c : Vector3f

	override val minx: Float
		get() = min(a.x, min(b.x, c.x))
	override val maxx: Float
		get() = max(a.x, max(b.x, c.x))
	override val miny: Float
		get() = min(a.y, min(b.y, c.y))
	override val maxy: Float
		get() = max(a.y, max(b.y, c.y))
	override val minz: Float
		get() = min(a.z, min(b.z, c.z))
	override val maxz: Float
		get() = max(a.z, max(b.z, c.z))

	val normal : Vector3f
		get() = (a - c).cross(b - c).normalize()

	val xy get() = Simplex2v2d(a.x, a.y, b.x, b.y, c.x, c.y)
	val xz get() = Simplex2v2d(a.x, a.z, b.x, b.z, c.x, c.z)
	val yz get() = Simplex2v2d(a.y, a.z, b.y, b.z, c.y, c.z)

	/**
	 * creates a new simplex given 3 points
	 * @param a first point
	 * @param b second point
	 * @param c third point
	 */
	constructor(a : Vector3f, b : Vector3f, c : Vector3f) {
		this.a = a
		this.b = b
		this.c = c
	}

	constructor() : this(Vector3f(), Vector3f(), Vector3f())

	/**
	 * creates a new simplex given the coordinates for 3 points
	 *
	 * @param ax first x
	 * @param ay first y
	 * @param az first z
	 * @param bx second x
	 * @param by second y
	 * @param bz second z
	 * @param cx third x
	 * @param cy third y
	 * @param cz third z
	 */
	constructor(
		ax : Float, ay : Float, az : Float,
		bx : Float, by : Float, bz : Float,
		cx : Float, cy : Float, cz : Float) :
			this(Vector3f(ax, ay, az), Vector3f(bx, by, bz), Vector3f(cx, cy, cz))

	/**
	 * creates a new simplex from normal and plots the points around a given point
	 *
	 * @param norm  normal of the new simplex
	 * @param point point to base the other 3 around them
	 */
	constructor(norm : Vector3fc, point : Vector3fc) : this(
		Vector3f(-1f, (-norm.x() + norm.z()) / norm.y() + point.y(), 1f),
		Vector3f(1f, (norm.x() + norm.z()) / norm.y() + point.y(), 1f),
		Vector3f(-1f, (-norm.x() - norm.z()) / norm.y() + point.y(), -1f))

	/**
	 * creates a new simplex from a x and z coefficient for an equation that represents the plane
	 *
	 * @param xCoeff x coefficient
	 * @param zCoeff z coefficient
	 * @param point  point to base the points around
	 */
	constructor(xCoeff : Float, zCoeff : Float, point : Vector3fc) : this(
		Vector3f(-1f + point.x(), -xCoeff + zCoeff + point.y(), 1f + point.z()),
		Vector3f(1f + point.x(), xCoeff + zCoeff + point.y(), 1f + point.z()),
		Vector3f(-1f + point.x(), -xCoeff - zCoeff + point.y(), -1f + point.z()))

	operator fun plus(delta : Vector3f) = Simplex2v3d(a + delta, b + delta, c + delta)

	/**
	 * gets the corresponding x of a y and z position
	 *
	 * @param y y position
	 * @param z z position
	 * @return x coordinate
	 */
	fun getx(y : Float, z : Float) = -(normal.y * (y - c.y) + normal.z * (z - c.z)) / normal.x + c.x

	/**
	 * gets the corresponding x of a y and z position in a vector
	 *
	 * @param v vector holding the y and z position
	 * @return x coordinate
	 */
	fun getx(v : Vector2fc) = getx(v.x(), v.y())

	/**
	 * gets the corresponding y of a x and z position
	 *
	 * @param x x position
	 * @param z z position
	 * @return y coordinate
	 */
	fun gety(x : Float, z : Float) = -(normal.x * (x - c.x) + normal.z * (z - c.z)) / normal.y + c.y

	/**
	 * gets the corresponding y of a x and z position in a vector
	 *
	 * @param v vector holding the x and z position
	 * @return y coordinate
	 */
	fun gety(v : Vector2fc) = gety(v.x(), v.y())

	/**
	 * gets the corresponding z of a x and y position
	 *
	 * @param x x position
	 * @param y y position
	 * @return z coordinate
	 */
	fun getz(x : Float, y : Float) = -(normal.x * (x - c.x) + normal.y * (y - c.y)) / normal.z + c.z

	/**
	 * gets the corresponding z of a x and y position in a vector
	 *
	 * @param v vector holding the x and y position
	 * @return z coordinate
	 */
	fun getz(v : Vector2fc) = getz(v.x(), v.y())

	fun getTIntersect(l: Ray3f) : Float {
		val k = l - a
		return -k.dot(normal) / l.delta.dot(normal)
	}

	fun getIntersectionPoint(l : Ray3f) : Vector3f? = l.getIntersectionPoint(this)

	fun getPlaneIntersectPoint(l : Ray3f) : Vector3f = l.planeIntersectionPoint(this)

	fun getIntersectT(d : Vector3fc, o : Simplex2v3d) = getIntersectT({it + d}, o)

	fun getIntersectT(transformation : (Vector3fc) -> Vector3f , o : Simplex2v3d) : Float? {
		val aRay = Ray3f(a, transformation(a) - a)
		val bRay = Ray3f(b, transformation(b) - b)
		val cRay = Ray3f(c, transformation(c) - c)
		val alt = o.getTIntersect(aRay)
		val blt = o.getTIntersect(bRay)
		val clt = o.getTIntersect(cRay)
		val norm = normal
		val anx = abs(norm.x)
		val any = abs(norm.y)
		val anz = abs(norm.z)

		fun projectedT(alx : Float, aly : Float, blx : Float, bly : Float, clx : Float, cly : Float,
		               oax : Float, oay : Float, obx : Float, oby : Float, ocx : Float, ocy : Float) : Float? {
			val abab = Ray2f.doesIntersectTPts(alx, aly, blx, bly, oax, oay, obx, oby)
			val acab = Ray2f.doesIntersectTPts(alx, aly, clx, cly, oax, oay, obx, oby)
			val bcab = Ray2f.doesIntersectTPts(blx, bly, clx, cly, oax, oay, obx, oby)
			val abac = Ray2f.doesIntersectTPts(alx, aly, blx, bly, oax, oay, ocx, ocy)
			val acac = Ray2f.doesIntersectTPts(alx, aly, clx, cly, oax, oay, ocx, ocy)
			val bcac = Ray2f.doesIntersectTPts(blx, bly, clx, cly, oax, oay, ocx, ocy)
			val abbc = Ray2f.doesIntersectTPts(alx, aly, blx, bly, obx, oby, ocx, ocy)
			val acbc = Ray2f.doesIntersectTPts(alx, aly, clx, cly, obx, oby, ocx, ocy)
			val bcbc = Ray2f.doesIntersectTPts(blx, bly, clx, cly, obx, oby, ocx, ocy)
			fun lerpToRange(a : Float, b : Float, t : Float?) : Float? {
				return lerp(a, b, t?: return null).takeIf {it in 0f..1f}
			}
			var ret = lerpToRange(alt, blt, abab)
			ret = mino(ret, lerpToRange(alt, clt, acab))
			ret = mino(ret, lerpToRange(blt, clt, bcab))
			ret = mino(ret, lerpToRange(alt, blt, abac))
			ret = mino(ret, lerpToRange(alt, clt, acac))
			ret = mino(ret, lerpToRange(blt, clt, bcac))
			ret = mino(ret, lerpToRange(alt, blt, abbc))
			ret = mino(ret, lerpToRange(alt, clt, acbc))
			ret = mino(ret, lerpToRange(blt, clt, bcbc))

			fun updateRetForPoint(lx : Float, ly : Float, lt : Float) {
				if (containsPoint(oax, oay, obx, oby, ocx, ocy, lx, ly)) {
					ret = mino(ret, lt.takeIf { it in 0f..1f })
				}
			}

			updateRetForPoint(alx, aly, alt)
			updateRetForPoint(blx, bly, blt)
			updateRetForPoint(clx, cly, clt)

			val ytCoeff = ((alt - blt) * (clx - blx) - (clt - blt) * (alx - blx)) /
					((aly - bly) * (clx - blx) + (bly - cly) * (alx - blx))
			val xtCoeff = (alt - blt - ytCoeff * (aly - bly)) / (alx - blx)
			val offset = alt - xtCoeff * alx - ytCoeff * aly

			fun updateRetForOPoint(ox : Float, oy : Float) {
				if (containsPoint(alx, aly, blx, bly, clx, cly, ox, oy)) {
					ret = mino(ret, (offset + ox * xtCoeff + oy * ytCoeff).takeIf {it in 0f..1f})
				}
			}
			updateRetForOPoint(oax, oay)
			updateRetForOPoint(obx, oby)
			updateRetForOPoint(ocx, ocy)
			return ret
		}

		return when {
			max(any, anz) <= anx -> projectedT(aRay.yAt(alt), aRay.zAt(alt), bRay.yAt(blt), bRay.zAt(blt),
				cRay.yAt(clt), cRay.zAt(clt), o.a.y, o.a.z, o.b.y, o.b.z, o.c.y, o.c.z)//yz
			any >= anz -> projectedT(aRay.xAt(alt), aRay.zAt(alt), bRay.xAt(blt), bRay.zAt(blt),
				cRay.xAt(clt), cRay.zAt(clt), o.a.x, o.a.z, o.b.x, o.b.z, o.c.x, o.c.z)//xz
			else -> projectedT(aRay.xAt(alt), aRay.yAt(alt), bRay.xAt(blt), bRay.yAt(blt),
				cRay.xAt(clt), cRay.yAt(clt), o.a.x, o.a.y, o.b.x, o.b.y, o.c.x, o.c.y)//xy
		}
	}

	fun getReflected(ray : Ray3f) : Ray3f {
		val intersection = getPlaneIntersectPoint(ray)
		return Ray3f(
			intersection,
			normal.mul(2 * normal.dot(ray.delta) / normal.lengthSquared(), Vector3f()).add(ray.delta)
		)
	}

	fun collides(v : Vector3fc, other : Simplex2v3d, ov : Vector3fc) = collides(other, v - ov)

	fun collides(other : Simplex2v3d, vdel : Vector3fc) : Float? {
		var t = Ray3f(a, vdel).getIntersectionT(other) ?: 2f
		t = min(t, Ray3f(b, vdel).getIntersectionT(other) ?: 2f)
		t = min(t, Ray3f(c, vdel).getIntersectionT(other) ?: 2f)
		val nvdel = vdel * -1f
		t = min(t, Ray3f(other.a, nvdel).getIntersectionT(this) ?: 2f)
		t = min(t, Ray3f(other.b, nvdel).getIntersectionT(this) ?: 2f)
		t = min(t, Ray3f(other.c, nvdel).getIntersectionT(this) ?: 2f)
		return if (t > 1f) null else t
	}

	fun lerp(other : Simplex2v3d, t : Float, dest : Simplex2v3d) : Simplex2v3d {
		a.lerp(other.a, t, dest.a)
		b.lerp(other.b, t, dest.b)
		c.lerp(other.c, t, dest.c)
		return dest
	}

	fun lerp(other : Simplex2v3d, t : Float) = lerp(other, t, this)
}

private fun lerp(a : Float, b : Float, t : Float) = fma(t, b - a, a)

private fun getTIfLands(x : Float, y : Float, dx : Float, dy : Float,
                        ox : Float, oy : Float, odx : Float, ody : Float) : Float? {
	val t = Ray2f.intersectionT(x, y, dx, dy, ox, oy, odx, ody)
	val ot = if (abs(dx) > 0.001) {
		(fma(dx, t, x) - ox) / odx
	} else {
		(fma(dy, t, y) - oy) / ody
	}
	return if (ot in 0f..1f) null else t
}

private fun getTifLandsPts(ax : Float, ay : Float, bx : Float, by : Float,
                           oax : Float, oay : Float, obx : Float, oby : Float) =
	getTIfLands(ax, ay, bx - ax, by - ay, oax, oay, obx - oax, oby - oay)

private fun lerpToRange(a : Float, b : Float, t : Float?) : Float? {
	return lerp(a, b, t?: return null).takeIf {it in 0f..1f}
}