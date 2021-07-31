package engine.shapes

import engine.mino
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f

interface Bound3f {
	val minx : Float
	val maxx : Float
	val miny : Float
	val maxy : Float
	val minz : Float
	val maxz : Float

	val width : Float
		get() = maxx - minx
	val height : Float
		get() = maxy - miny
	val depth : Float
		get() = maxz - minz

	val centerX : Float
		get() = (minx + maxx) / 2f
	val centerY : Float
		get() = (miny + maxy) / 2f
	val centerZ : Float
		get() = (minz + maxz) / 2f

	val xRange
		get() = minx..maxx

	val yRange
		get() = miny..maxz

	val zRange
		get() = minz..maxz

	/**
	 * checks if a point lies within the bounds of the box
	 * @param point point to check
	 * @return if the point is in the box
	 */
	fun contains(point: Vector3f) : Boolean {
		return minx < point.x && maxx > point.x && miny < point.y && maxy > point.y && minz < point.z && maxz > point.z
	}

	fun contains(x: Float, y: Float, z: Float) : Boolean {
		return minx < x && maxx > x && miny < y && maxy > y && minz < z && maxz > z
	}

	/**
	 * returns true if the other box is completely inside this box
	 * @param other the other box
	 * @return true if the other box is in this box
	 */
	operator fun contains(other: Bound3f): Boolean {
		return minx < other.minx && maxx > other.maxx && miny < other.miny && maxy > other.maxy && minz < other.minz && maxz > other.maxz
	}

	/**
	 * returns true if any of the vertices of the other box are touching the box;
	 * @param other the other box
	 * @return true if the box is touching the other box
	 */
	fun touches(other: Bound3f): Boolean {
		return contains(other.minx, other.miny, other.minz) || contains(other.minx, other.miny, other.maxz) || contains(
			other.minx,
			other.maxy,
			other.maxz
		) || contains(other.minx, other.maxy, other.minz) || contains(other.maxx, other.miny, other.minz) || contains(
			other.maxx,
			other.miny,
			other.maxz
		) || contains(other.maxx, other.maxy, other.maxz) || contains(other.maxx, other.maxy, other.minz)
	}

	/**
	 * returns true if the box contains any of the points
	 * @param points the points
	 * @return true if any of the points are in the box
	 */
	fun touches(points: Array<Vector3f>): Boolean {
		for (point in points) {
			if (contains(point)) {
				return true
			}
		}
		return false
	}

	/**
	 * returns true if any of the points are contained in the box
	 * @param points formatted [x1, y1, z1, x2, y2, z2...]
	 * @return true if any of the points are contained in the box
	 */
	fun touches(points: FloatArray): Boolean {
		var i = 0
		while (i < points.size) {
			if (contains(points[i], points[i + 1], points[i + 2])) {
				return true
			}
			i += 3
		}
		return false
	}

	fun intersect(other : Bound3f) = Box3d(minx.coerceAtMost(other.minx), maxx.coerceAtLeast(other.maxx),
		miny.coerceAtMost(other.miny), maxy.coerceAtLeast(other.maxy),
		minz.coerceAtMost(other.minz), maxz.coerceAtLeast(other.maxz))
}

interface Bound2f {
	val minx : Float
	val maxx : Float
	val miny : Float
	val maxy : Float

	val width : Float
		get() = maxx - minx
	val height : Float
		get() = maxy - miny

	val centerX : Float
		get() = (minx + maxx) / 2f
	val centerY : Float
		get() = (miny + maxy) / 2f

	/**
	 * checks to see if a point is in a rectangle that is sligtly larger than
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @param extra extra size that the box is when testing the point
	 * @return if the point is in the larger rectangle
	 */
	fun contains(x : Float, y : Float, extra : Float) : Boolean {
		return minx < x + extra && maxx > x - extra && miny < y + extra && maxy > y - extra
	}

	/**
	 * checks to see if a point is in a rectangle that is sligtly larger than
	 * @param point point to check
	 * @param extra extra size that the box is when testing the point
	 * @return if the point is in the larger rectangle
	 */
	fun contains(point : Vector2f, extra : Float) = contains(point.x, point.y, extra)

	/**
	 * checks to see if a point ins in the rectangle
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return if the point is in the rectangle
	 */
	fun contains(x : Float, y : Float) = minx < x && maxx > x && miny < y && maxy > y

	/**
	 * checks to see if a point ins in the rectangle
	 * @param point point to check
	 * @return if the point is in the rectangle
	 */
	fun contains(point : Vector2f) = contains(point.x, point.y)

	/**
	 * returns true if the other box is completely inside this box
	 * @param other the other box
	 * @return true if the other box is in this box
	 */
	fun contains(other : Box2d) = minx < other.minx && maxx > other.maxx && miny < other.miny && maxy > other.maxy

	fun touching(oMinX : Float, oMinY : Float, oMaxX : Float, oMaxY : Float) =
		oMinX <= maxx && oMaxX >= minx && oMinY <= maxy && oMaxY >= miny

	/**
	 * returns true if any of the vertices of the other box are touching the box;
	 * @param other the other box
	 * @return true if the box is touching the other box
	 */
	fun touching(other : Box2d) = touching(other.minx, other.miny, other.maxx, other.maxy)

	/**
	 * returns true if the box contains any of the points
	 * @param points the points
	 * @return true if any of the points are in the box
	 */
	fun touches(points : Array<Vector2f>) : Boolean {
		for (point in points) {
			if (contains(point)) {
				return true
			}
		}
		return false
	}

	/**
	 * returns true if any of the points are contained in the box
	 * @param points formatted [x1, y1, x2, y2...]
	 * @return true if any of the points are contained in the box
	 */
	fun touches(points : FloatArray) : Boolean {
		var i = 0
		while (i < points.size) {
			if (contains(points[i], points[i + 1])) {
				return true
			}
			i += 2
		}
		return false
	}

	fun translate(x : Float, y : Float, other : Box2d) : Box2d {
		other.minx = minx + x
		other.maxx = maxx + x
		other.miny = miny + y
		other.maxy = maxy + y
		return other
	}

	fun translate(delta : Vector2fc, other : Box2d) = translate(delta.x(), delta.y(), other)

	fun getCollisionT(dx : Float, dy : Float, oMinX : Float, oMinY : Float, oMaxX : Float, oMaxY : Float) : Float? {
		val odx = oMaxX - oMinX
		val ody = oMaxY - oMinY
		val tdx = width
		val tdy = height
		if (dx > 0) {
			var ret = mino(Ray2f.doesIntersectTXA(maxx, maxy, dx, dy, oMinX, oMinY, ody),
				Ray2f.doesIntersectTXA(oMinX, oMaxY, -dx, -dy, maxx, miny, tdy))
			ret = if (dy > 0) {
				mino(Ray2f.doesIntersectTYA(maxx, maxy, dx, dy, oMinX, oMinY, odx),
					mino(ret, Ray2f.doesIntersectTYA(oMaxX, oMinY, -dx, -dy, minx, maxy, tdx))
				)
			} else {
				mino(Ray2f.doesIntersectTYA(minx, miny, dx, dy, oMinX, oMaxY, odx),
					mino(ret, Ray2f.doesIntersectTYA(oMinX, oMaxY, -dx, -dy, minx, miny, tdx))
				)
			}
			return ret
		} else {
			var ret = mino(Ray2f.doesIntersectTXA(minx, maxy, dx, dy, oMaxX, oMinY, ody),
				Ray2f.doesIntersectTXA(oMaxX, oMaxY, -dx, -dy, minx, miny, tdy))
			ret = if (dy > 0) {
				mino(Ray2f.doesIntersectTYA(minx, maxy, dx, dy, oMinX, oMinY, odx),
					mino(ret, Ray2f.doesIntersectTYA(oMinX, oMinY, -dx, -dy, minx, maxy, tdx))
				)
			} else {//WRONG
				mino(Ray2f.doesIntersectTYA(maxx, miny, dx, dy, oMinX, oMaxY, odx),
					mino(ret, Ray2f.doesIntersectTYA(oMaxX, oMaxY, -dx, -dy, minx, miny, tdx))
				)
			}
			return ret
		}
	}
	fun getCollisionT(d : Vector2f, oMinX : Float, oMinY : Float, oMaxX : Float, oMaxY : Float)
			= getCollisionT(d.x, d.y, oMinX, oMinY, oMaxX, oMaxY)
	fun getCollisionT(dx : Float, dy : Float, o : Box2d) = getCollisionT(dx, dy, o.minx, o.miny, o.maxx, o.maxy)
	fun getCollisionT(d : Vector2f, o : Box2d) = getCollisionT(d.x, d.y, o.minx, o.miny, o.maxx, o.maxy)

	fun intersect(other : Bound2f) = Box2d(minx.coerceAtMost(other.minx), maxx.coerceAtLeast(other.maxx),
		miny.coerceAtMost(other.miny), maxy.coerceAtLeast(other.maxy))
}