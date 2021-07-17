package engine.shapes

import engine.opengl.jomlExtensions.plus
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector2fc
import java.lang.Math.fma
import kotlin.math.min
import kotlin.math.max

class Ray2f(x : Float, y : Float, dx : Float, dy : Float) : Vector2f(x, y), Bound2f {
	var delta = Vector2f(dx, dy)

	var dx : Float
		get() = delta.x
		set(value) {delta.x = value}
	var dy : Float
		get() = delta.y
		set(value) {delta.y = value}

	override val minx: Float
		get() = min(x, x + dx)
	override val maxx: Float
		get() = max(x, x + dx)
	override val miny: Float
		get() = min(y, y + dy)
	override val maxy: Float
		get() = max(y, y + dy)

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