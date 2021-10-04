package engine.shapes.rays

import engine.lerp
import engine.opengl.jomlExtensions.*
import engine.shapes.bounds.Bound3f
import engine.shapes.simplexes.Simplex2v2d
import engine.shapes.simplexes.Simplex2v3d
import org.joml.Math.sqrt
import org.joml.Vector3f
import org.joml.Vector3fc
import kotlin.math.abs
import org.joml.Math.fma

open class Ray3f : Vector3f, Bound3f {
	open var delta : Vector3f = Vector3f()

	open var dx : Float
		get() = delta.x
		set(value) {delta.x = value}
	open var dy : Float
		get() = delta.y
		set(value) {delta.y = value}
	open var dz : Float
		get() = delta.z
		set(value) {delta.z = value}

	open var final : Vector3f
		get() = Vector3f(fx, fy, fz)
		set(value) {
			fx = value.x
			fy = value.y
			fz = value.z
		}
	open var fx : Float
		get() = x + dx
		set(value) {delta.x = value - x}
	open var fy : Float
		get() = y + dy
		set(value) {delta.y = value - y}
	open var fz : Float
		get() = z + dz
		set(value) {delta.z = value - z}

	override val minx : Float
		get() = x.coerceAtMost(fx)
	override val maxx : Float
		get() = x.coerceAtLeast(fx)
	override val miny : Float
		get() = y.coerceAtMost(fy)
	override val maxy : Float
		get() = y.coerceAtLeast(fy)
	override val minz : Float
		get() = z.coerceAtMost(fz)
	override val maxz : Float
		get() = z.coerceAtLeast(fz)

	constructor() : super()

	constructor(start : Vector3fc, delta : Vector3fc) : super(start) {
		this.delta = Vector3f(delta)
	}

	constructor(sx : Float, sy : Float, sz : Float, dx : Float, dy : Float, dz : Float) : super(sx, sy, sz) {
		delta = Vector3f(dx, dy, dz)
	}

	constructor(other : Ray3f) : super(other) {
		delta = Vector3f(other.delta)
	}

	open fun xAt(t : Float) = fma(delta.x, t, x)

	open fun yAt(t : Float) = fma(delta.y, t, y)

	open fun zAt(t : Float) = fma(delta.z, t, z)

	open fun getPointAt(t : Float) = Vector3f(xAt(t), yAt(t), zAt(t))

	open fun getIntersectionPoint(simplex : Simplex2v3d) : Vector3f? {
		return getPointAt(getIntersectionT(simplex)?: return null)
	}

	open fun intersects(simplex : Simplex2v3d) = getIntersectionPoint(simplex) != null

	open fun getIntersectionT(simplex : Simplex2v3d) : Float? {
		val t = simplex.getTIntersect(this)
		if (t < 0 || t > 1) return null
		val normal = simplex.normal
		val projected : Simplex2v2d
		val ox : Float
		val oy : Float
		if (abs(normal.x) > 0.001) {
			projected = simplex.yz
			ox = yAt(t)
			oy = zAt(t)
		} else if (abs(normal.y) > 0.001) {
			projected = simplex.xz
			ox = xAt(t)
			oy = zAt(y)
		} else {
			projected = simplex.xy
			ox = xAt(t)
			oy = yAt(t)
		}
		return if (projected.containsPoint(ox, oy)) t else null
	}

	open fun planeIntersectionPoint(simplex : Simplex2v3d) = getPointAt(simplex.getTIntersect(this))

	override fun rotateX(theta : Float) : Ray3f {
		super.rotateX(theta)
		delta.rotateX(theta)
		return this
	}

	override fun rotateY(theta : Float) : Ray3f {
		super.rotateY(theta)
		delta.rotateY(theta)
		return this
	}

	override fun rotateZ(theta : Float) : Ray3f {
		super.rotateZ(theta)
		delta.rotateZ(theta)
		return this
	}

	open fun closestTTo(point : Vector3fc) : Float {
		//return -(dx * (x - point.x()) + dy * (y - point.y()) + dz * (z - point.z())) / delta.lengthSquared()
		return (delta dot (point - this)) / delta.lengthSquared()
	}

	open fun closestPointTo(other : Vector3fc) = getPointAt(closestTTo(other))

	open fun closestDistanceSquared(other : Vector3fc) = closestPointTo(other).distanceSquared(other)

	open fun closestDistance(other : Vector3fc) = sqrt(closestDistanceSquared(other))

	open fun closestPointInRangeTo(other : Vector3fc) : Vector3f {
		val tval = closestTTo(other)

		return if (tval > 0 && tval < 1) {
			getPointAt(tval)
		} else {
			val target = this + delta
			if (distanceSquared(other) < target.distanceSquared(other)) {
				Vector3f(this)
			} else {
				target
			}
		}
	}

	open fun closestDistanceInRangeSquared(other : Vector3fc) : Float {
		val tval = closestTTo(other)
		return if (tval > 0 && tval < 1) {
			getPointAt(tval).distanceSquared(other)
		} else {
			distanceSquared(other).coerceAtMost(getPointAt(1f).distanceSquared(other))
		}
	}

	open fun closestDistanceInRange(other : Vector3fc) = sqrt(closestDistanceInRangeSquared(other))

	open fun closestTTo(o : Ray3f) : Float {
		/*val deltadot = delta dot other.delta
		val startDel = other - this
		return ((startDel dot delta) * other.delta.lengthSquared() + deltadot * (deltadot - (startDel dot other.delta))) / delta.lengthSquared()*/
		/*(x + t * dx - o.x - u * o.dx) * (x + t * dx - o.x - u * o.dx) +
				(y + t * dy - o.y - u * o.dy) * (y + t * dy - o.y - u * o.dy) +
				(z + t * dz - o.z - u * o.dz) * (z + t * dz - o.z - u * o.dz)
*/

		/*(x + t * dx - o.x - u * o.dx) * dx +
				(y + t * dy - o.y - u * o.dy) * dy +
				(z + t * dz - o.z - u * o.dz) * dz*/

		/*(x + t * dx - o.x - u * o.dx) * o.dx +
				(y + t * dy - o.y - u * o.dy) * o.dy +
				(z + t * dz - o.z - u * o.dz) * o.dz*/

		/*(x + t * dx - o.x) * o.dx + (y + t * dy - o.y) * o.dy + (z + t * dz - o.z) * o.dz = u * (o.delta.lengthSquared())

		((x - o.x) * dx + (y - o.y) * dy + (z - o.z) * dz) * o.delta.lengthSquared() - ((x - o.x) * o.dx + (y - o.y) * o.dy + (z - o.z) * o.dz) * (delta dot o.delta)
		// =
		t * ((delta dot o.delta) * (dx + dy + dz) - o.delta.lengthSquared() * (delta.lengthSquared()))*/
		//return ((x - o.x) * dx + (y - o.y) * dy + (z - o.z) * dz) * o.delta.lengthSquared() - ((x - o.x) * o.dx + (y - o.y) * o.dy + (z - o.z) * o.dz) * (delta dot o.delta) / ((delta dot o.delta) * (dx + dy + dz) - o.delta.lengthSquared() * (delta.lengthSquared()))

		/*val rb = (delta dot (o - this)) / delta.lengthSquared()
		val rm = (delta dot o.delta) / delta.lengthSquared()
		val base = this + rb * delta - o
		val del = (delta * rm - o.delta)
		//(base.x + t * del.x) * (base.x + t * del.x)
		//(base.y + t * del.y) * (base.y + t * del.y)
		//(base.z + t * del.z) * (base.z + t * del.z)

		//base.x * del.x + t * del.x * del.x
		//base.y * del.y + t * del.y * del.y
		//base.z * del.z + t * del.z * del.z
		return - (base dot del) / del.lengthSquared()*/
		//ATTEMPT 2
		/*val rb = (o.delta dot (this - o)) / o.delta.lengthSquared()
		val rm = (o.delta dot delta) / o.delta.lengthSquared()
		val base = o + rb * o.delta - this
		val del = (o.delta * rm - delta)
		return - (base dot del) / del.lengthSquared()*/
		//ATTEMPT 3
		//0 = (o - this + o.delta * u - delta * v) dot delta
		//val u = ((this - o + delta * v) dot delta) / (o.delta dot delta)
		//0 = (o - this + o.delta * u - delta * v) dot o.delta
		//val v = ((o - this + o.delta * u) dot o.delta) / (delta dot o.delta)

		val sdel = o - this
		//val v = (((o - this) * (delta dot o.delta) + o.delta * ((this - o + delta * v) dot delta)) dot o.delta) / ((delta dot o.delta) * (delta dot o.delta))
		//val v * (delsDot * delsDot) = ((o - this) * delsDot + o.delta * ((this - o + delta * v) dot delta)) dot o.delta
		//v * (delsDot * delsDot - (o.delta dot o.delta) * (delta dot delta))
		// =
		//(((o - this) * delsDot) dot o.delta) + (o.delta dot o.delta) * (((this - o) dot delta))

		//val lower = (delsDot * delsDot - (o.delta dot o.delta) * (delta dot delta))
		val lower = (delta cross o.delta).lengthSquared()
		return (o.delta.lengthSquared() * (sdel dot delta) - (sdel dot o.delta) * (delta dot o.delta)) / lower
	}

	open fun closestPointTo(other : Ray3f) = getPointAt(closestTTo(other))

	open fun closestDistanceSquared(other : Ray3f) = other.closestDistanceSquared(closestPointTo(other))

	open fun closestDistance(other : Ray3f) = sqrt(closestDistanceSquared(other))

	open fun lerp(other : Ray3f, t : Float) : Ray3f {
		return Ray3f(x.lerp(other.x, t), y.lerp(other.y, t), z.lerp(other.z, t), dx.lerp(other.dx, t), dy.lerp(other.dy, t), dz.lerp(other.dz, t))
	}

	open operator fun plus(inc : Vector3fc) = Ray3f((this as Vector3fc) + inc, delta)
	open operator fun plus(o : Ray3f) = Ray3f((this as Vector3fc) + o, delta + o.delta)
	open operator fun minus(inc : Vector3fc) = Ray3f((this as Vector3fc) - inc, delta)
	open operator fun minus(o : Ray3f) = Ray3f((this as Vector3fc) - o, delta + o.delta)
	open operator fun times(s : Float) = Ray3f((this as Vector3fc) * s, s * delta)
	override operator fun div(s : Float) = Ray3f((this as Vector3fc) / s, delta / s)

	override fun mul(scalar: Float) : Ray3f {
		super.mul(scalar)
		delta.mul(scalar)
		return this
	}

	companion object {
		fun between(a : Vector3f, b : Vector3f) = Ray3f(a, b - a)
	}
}

operator fun Vector3fc.plus(r : Ray3f) = r + this
operator fun Vector3fc.minus(r : Ray3f) = r - this
operator fun Float.times(r : Ray3f) = r * this