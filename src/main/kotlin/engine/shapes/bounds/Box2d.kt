package engine.shapes.bounds

import org.joml.Vector2fc

typealias AABB = Box2d

open class Box2d : Bound2f {
	override var minx : Float = 0f
	override var maxx : Float = 0f
	override var miny : Float = 0f
	override var maxy : Float = 0f

	override var width : Float
		get() = maxx - minx
		set(value) {maxx = minx + value}
	override var height : Float
		get() = maxy - miny
		set(value) {maxy = miny + value}

	override var x : Float
		get() = minx
		set(value) {
			maxx = value + width
			minx = value
		}
	override var y : Float
		get() = miny
		set(value) {
			maxy = value + height
			miny = value
		}

	constructor()

	/**
	 * creates a new box given the minimums and maximum coordinates
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param xmax maximum x
	 * @param ymax maximum y
	 */
	constructor(xmin : Float, ymin : Float, xmax : Float, ymax : Float) {
		minx = xmin
		maxx = xmax
		miny = ymin
		maxy = ymax
	}

	/**
	 * creates a new box given two vectors on opposing ends
	 * @param a first corner
	 * @param b opposing corner
	 */
	constructor(a : Vector2fc, b : Vector2fc) {
		minx = a.x().coerceAtMost(b.x())
		maxx = a.x().coerceAtLeast(b.x())
		miny = a.y().coerceAtMost(b.y())
		maxy = a.y().coerceAtLeast(b.y())
	}

	open fun translate(delta : Vector2fc) = translate(delta.x(), delta.y())

	open fun translate(x : Float, y : Float) = translate(x, y, this)

}