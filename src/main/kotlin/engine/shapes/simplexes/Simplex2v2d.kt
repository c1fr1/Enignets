package engine.shapes.simplexes

import engine.shapes.bounds.Bound2f
import engine.shapes.bounds.Box2d
import org.joml.Vector2f
import kotlin.math.max
import kotlin.math.min

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class Simplex2v2d(a : Vector2f, b : Vector2f, c : Vector2f) : Bound2f {
	open var a : Vector2f = a
	open var b : Vector2f = b
	open var c : Vector2f = c

	override val minx: Float
		get() = min(a.x, min(b.x, c.x))
	override val maxx: Float
		get() = max(a.x, max(b.x, c.x))
	override val miny: Float
		get() = min(a.y, min(b.y, c.y))
	override val maxy: Float
		get() = max(a.y, max(b.y, c.y))

	/**
	 * creates a simplex based on the coordinates for 3 points
	 * @param ax first x
	 * @param ay first y
	 * @param bx second x
	 * @param by second y
	 * @param cx third x
	 * @param cy third y
	 */
	constructor(ax: Float, ay: Float, bx: Float, by: Float, cx: Float, cy: Float) :
			this(
				Vector2f(ax, ay),
				Vector2f(bx, by),
				Vector2f(cx, cy))

	open fun calculateDoubleArea() = getDoubleArea(a.x, a.y, b.x, b.y, c.x, c.y)

	/**
	 * checks to see if another point is inside this simplex
	 * @param other point to check
	 * @return if that point is in the simplex
	 */
	open fun containsPoint(other: Vector2f): Boolean {
		return containsPoint(other.x, other.y)
	}

	/**
	 * checks to see if another point is inside this simplex
	 * @param ox x of the point
	 * @param oy y of the point
	 * @return if that point is in the simplex
	 */
	open fun containsPoint(ox: Float, oy: Float): Boolean {
		val u = getBarycentricA(ox, oy)
		val v = getBarycentricB(ox, oy)
		return if (u + v > 1f) false else if (u < -0) false else v >= -0
	}

	/**
	 * checks to see if another point is inside this simplex given an extra offset
	 * @param ox x of the point
	 * @param oy y of the point
	 * @param extra extra offset
	 * @return if the point is in the largest simplex
	 */
	open fun containsPoint(ox : Float, oy : Float, extra : Float) : Boolean {
		val u = getBarycentricA(ox, oy)
		if (u < -extra) return false
		val v = getBarycentricB(ox, oy)
		return if (u + v > 1f + extra) false else v >= -extra
	}

	open fun getBarycentricA(ox : Float, oy : Float) =
		getDoubleArea(ox, oy, b.x, b.y, c.x, c.y) / calculateDoubleArea()

	open fun getBarycentricB(ox : Float, oy : Float) =
		getDoubleArea(a.x, a.y, ox, oy, c.x, c.y) / calculateDoubleArea()

	open fun getBarycentricC(ox : Float, oy : Float) =
		getDoubleArea(a.x, a.y, b.x, b.y, ox, oy) / calculateDoubleArea()

	/**
	 * checks to see if a 2d box touches
	 * @param box box that could touch the simplex
	 * @return true if the box is touching the simplex
	 */
	open fun touches(box : Box2d) : Boolean {//TODO doesn't work
		if (box.contains(a)) return true
		if (box.contains(b)) return true
		if (box.contains(c)) return true
		if (containsPoint(box.minx, box.miny)) return true
		if (containsPoint(box.minx, box.maxy)) return true
		if (containsPoint(box.maxx, box.miny)) return true
		return containsPoint(box.maxx, box.maxy)
	}

	/**
	 * checks to see if a 2d box touches given an extra offset
	 * @param box box that could touch the simplex
	 * @param extra extra offset
	 * @return true if the larger box is touching the simplex
	 */
	open fun touches(box: Box2d, extra: Float): Boolean {//TODO doesn't work
		if (box.contains(a, extra)) return true
		if (box.contains(b, extra)) return true
		if (box.contains(c, extra)) return true
		if (containsPoint(box.minx, box.miny, extra)) return true
		if (containsPoint(box.minx, box.maxy, extra)) return true
		if (containsPoint(box.maxx, box.miny, extra)) return true
		return containsPoint(box.maxx, box.maxy)
	}
}

//clockwise simplexes will return a positive area
private fun getDoubleArea(ax : Float, ay : Float, bx : Float, by : Float, cx : Float, cy : Float) =
	(by - ay) * (cx - ax) - (bx - ax) * (cy - ay)
//abs(a.x * b.y - a.x * c.y - b.x * a.y + b.x * c.y + c.x * a.y - c.x * b.y)
fun getBarycentricA(bx : Float, by : Float, cx : Float, cy : Float, ox : Float, oy : Float,
                    doubleArea : Float) = getDoubleArea(ox, oy, bx, by, cx, cy) / doubleArea

fun getBarycentricA(ax : Float, ay : Float, bx : Float, by : Float, cx : Float, cy : Float, ox : Float, oy : Float) =
	getBarycentricA(ox, oy, bx, by, cx, cy, getDoubleArea(ax, ay, bx, by, cx, cy))

fun getBarycentricB(ax : Float, ay : Float, cx : Float, cy : Float, ox : Float, oy : Float,
                    doubleArea : Float) = getDoubleArea(ax, ay, ox, oy, cx, cy) / doubleArea

fun getBarycentricB(ax : Float, ay : Float, bx : Float, by : Float, cx : Float, cy : Float, ox : Float, oy : Float) =
	getBarycentricB(ax, ay, ox, oy, cx, cy, getDoubleArea(ax, ay, bx, by, cx, cy))

fun getBarycentricC(ax : Float, ay : Float, bx : Float, by : Float, ox : Float, oy : Float,
                    doubleArea : Float) = getDoubleArea(ax, ay, bx, by, ox, oy) / doubleArea

fun getBarycentricC(ax : Float, ay : Float, bx : Float, by : Float, cx : Float, cy : Float, ox : Float, oy : Float) =
	getBarycentricC(ax, ay, bx, by, ox, oy, getDoubleArea(ax, ay, bx, by, cx, cy))

fun containsPoint(ax :Float, ay : Float, bx : Float, by : Float, cx : Float, cy : Float, ox: Float, oy: Float) :
		Boolean {
	val da = getDoubleArea(ax, ay, bx, by, cx, cy)
	val u = getBarycentricA(bx, by, cx, cy, ox, oy, da)
	val v = getBarycentricB(ax, ay, cx, cy, ox, oy, da)
	return u >= -0 && v >= -0 && u + v <= 1f
}