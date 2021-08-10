package engine.shapes

import engine.opengl.jomlExtensions.div
import engine.opengl.jomlExtensions.minus
import engine.opengl.jomlExtensions.plus
import engine.opengl.jomlExtensions.times
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3fc
import java.lang.Math.fma

open class Ray2f : Vector2f, Bound2f {
	open var delta = Vector2f(0f, 0f)

	open var dx : Float
		get() = delta.x
		set(value) {delta.x = value}
	open  var dy : Float
		get() = delta.y
		set(value) {delta.y = value}

	open var final : Vector2f
		get() = Vector2f(fx, fy)
		set(value) {
			fx = value.x
			fy = value.y
		}
	open var fx : Float
		get() = x + dx
		set(value) {delta.x = value - x}
	open var fy : Float
		get() = y + dy
		set(value) {delta.y = value - y}

	override val minx : Float
		get() = x.coerceAtMost(fx)
	override val maxx : Float
		get() = x.coerceAtLeast(fx)
	override val miny : Float
		get() = y.coerceAtMost(fy)
	override val maxy : Float
		get() = y.coerceAtLeast(fy)

	constructor() : super()

	constructor(x : Float, y : Float, dx : Float, dy : Float) : super(x, y) {
		delta.x = dx
		delta.y = dy
	}

	constructor(start : Vector2fc, delta : Vector2fc) : this(start.x(), start.y(), delta.x(), delta.y())

	constructor(other : Ray2f) : this(other, other.delta)

	fun xAt(t : Float) = fma(dx, t, x)

	fun yAt(t : Float) = fma(dy, t, y)

	fun getPointAt(t : Float) = Vector2f(xAt(t), yAt(t))

	fun rotate(theta : Float, target : Ray2f) : Ray2f {
		val sin = Math.sin(theta)
		val cos = Math.cosFromSin(sin, theta)
		target.x =  x * cos + y * sin
		target.y = -x * sin + y * cos
		target.delta.x =  delta.x * cos + delta.y * sin
		target.delta.y = -delta.x * sin + delta.y * cos
		return target
	}

	fun rotate(theta : Float) = rotate(theta, this)

	fun closestTTo(point : Vector2fc) = -(dx * (x - point.x()) + dy * (y - point.y())) / delta.lengthSquared()

	fun closestPointTo(other : Vector2fc) = getPointAt(closestTTo(other))

	fun closestDistanceSquared(other : Vector2fc) = closestPointTo(other).distanceSquared(other)

	fun closestDistance(other : Vector2fc) = Math.sqrt(closestDistanceSquared(other))

	fun closestPointInRangeTo(other : Vector2fc) : Vector2f {
		val tval = closestTTo(other)

		return if (tval > 0 && tval < 1) {
			getPointAt(tval)
		} else {
			val target = this + delta
			if (distanceSquared(other) < target.distanceSquared(other)) {
				Vector2f(this)
			} else {
				target
			}
		}
	}

	fun closestDistanceInRangeSquared(other : Vector2fc) : Float {
		val tval = closestTTo(other)
		return if (tval > 0 && tval < 1) {
			getPointAt(tval).distanceSquared(other)
		} else {
			distanceSquared(other).coerceAtMost(getPointAt(1f).distanceSquared(other))
		}
	}

	fun closestDistanceInRange(other : Vector2fc) = Math.sqrt(closestDistanceInRangeSquared(other))

	fun intersectionT(ox : Float, oy : Float, odx : Float, ody : Float) =
		((x - ox) * ody - odx * (y - oy)) / (dy * odx - dx * ody)

	fun intersectionT(o : Ray2f) = intersectionT(o.x, o.y, o.dx, o.dy)

	fun intersectionPoint(ox : Float, oy : Float, odx : Float, ody : Float) =
		getPointAt(intersectionT(ox, oy, odx, ody))

	fun intersectionPoint(o : Ray2f) = getPointAt(intersectionT(o))

	operator fun plus(inc : Vector2fc) = Ray2f((this as Vector2fc) + inc, delta)
	operator fun plus(o : Ray2f) = Ray2f((this as Vector2fc) + o, delta + o.delta)
	operator fun minus(inc : Vector2fc) = Ray2f((this as Vector2fc) - inc, delta)
	operator fun minus(o : Ray2f) = Ray2f((this as Vector2fc) - o, delta + o.delta)
	operator fun times(s : Float) = Ray2f((this as Vector2fc) * s, s * delta)
	override operator fun div(s : Float) = Ray2f((this as Vector2fc) / s, delta / s)

	operator fun plusAssign(inc : Vector2fc) {add(inc)}
	operator fun plusAssign(o : Ray2f) {add(o);delta.add(o.delta)}
	operator fun minusAssign(inc : Vector2fc) {sub(inc)}
	operator fun minusAssign(o : Ray2f) {sub(o);delta.sub(o.delta)}
	operator fun timesAssign(s : Float) {mul(s)}
	operator fun divAssign(s : Float) {super.div(s);delta.div(s)}

	override fun mul(scalar: Float) : Ray2f {
		super.mul(scalar)
		delta.mul(scalar)
		return this
	}

	companion object {
		fun intersectionT(x : Float, y : Float, dx : Float, dy : Float,
		                  ox : Float, oy : Float, odx : Float, ody : Float) =
			((x - ox) * ody - odx * (y - oy)) / (dy * odx - dx * ody)

		fun intersectionTPts(ax : Float, ay : Float, bx : Float, by : Float,
		                  oax : Float, oay : Float, obx : Float, oby : Float) =
			intersectionT(ax, ay, bx - ax, by - ay, oax, oay, obx - oax, oby - oay)

		fun intersectionTXA(x : Float, dx : Float, ox : Float) = (ox - x) / dx

		fun intersectionTYA(y : Float, dy : Float, oy : Float) = (oy - y) / dy

		fun doesIntersectT(x : Float, y : Float, dx : Float, dy : Float,
		                   ox : Float, oy : Float, odx : Float, ody : Float) : Float? {
		// TODO get better names than this shit
			val ret = intersectionT(x, y, dx, dy, ox, oy, odx, ody)
			if (ret in 0.0..1.0) {
				val arrivalT = if (odx * odx > 0.0001) {
					(fma(dx, ret, x) - ox) / odx
				} else {
					(fma(dy, ret, y) - oy) / ody
				}
				if (arrivalT in 0f..1f) {
					return ret
				}
			}
			return null
		}

		fun doesIntersectTPts(ax : Float, ay : Float, bx : Float, by : Float,
		                      oax : Float, oay : Float, obx : Float, oby : Float) =
			doesIntersectT(ax, ay, bx - ax, by - ay, oax, oay, obx - oax, oby - oay)

		fun doesIntersectTXA(x : Float, y : Float, dx : Float, dy : Float,
		                   ox : Float, oy : Float, ody : Float) : Float? {
			// TODO get better names than this shit
			val ret = intersectionTXA(x, dx, ox)
			return if (ret in 0.0..1.0) {
				if ((fma(dy, ret, y) - oy) / ody in 0.0..1.0) {
					ret
				} else {
					null
				}
			} else {
				null
			}
		}

		fun doesIntersectTYA(x : Float, y : Float, dx : Float, dy : Float,
		                   ox : Float, oy : Float, odx : Float) : Float? {
			// TODO get better names than this shit
			val ret = intersectionTYA(y, dy, oy)
			return if (ret in 0.0..1.0) {
				if ((fma(dx, ret, x) - ox) / odx in 0.0..1.0) {
					ret
				} else {
					null
				}
			} else {
				null
			}
		}
	}
}

operator fun Vector2fc.plus(r : Ray2f) = r + this
operator fun Vector2fc.minus(r : Ray2f) = r - this
operator fun Float.times(r : Ray2f) = r * this