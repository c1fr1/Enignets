package engine.shapes

import org.joml.Vector2f
import engine.opengl.bufferObjects.VAO
import org.joml.Vector2fc

typealias AABB = Box2d

class Box2d : Bound2f {
	override var minx: Float
	override var maxx: Float
	override var miny: Float
	override var maxy: Float

	var x : Float
		get() = minx
		set(value) {
			maxx = value + width
			minx = value
		}
	var y : Float
		get() = miny
		set(value) {
			maxy = value + height
			miny = value
		}

	/**
	 * creates a new box given the minimums and maximum coordinates
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param xmax maximum x
	 * @param ymax maximum y
	 */
	constructor(xmin: Float, ymin: Float, xmax: Float, ymax: Float) {
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
	constructor(a: Vector2f, b: Vector2f) {
		minx = a.x.coerceAtMost(b.x)
		maxx = a.x.coerceAtLeast(b.x)
		miny = a.y.coerceAtMost(b.y)
		maxy = a.y.coerceAtLeast(b.y)
	}

	constructor() : this(0f, 0f, 0f, 0f)

	fun translate(delta : Vector2fc) = translate(delta.x(), delta.y())

	fun translate(x : Float, y : Float) = translate(x, y, this)

	/**
	 * returns the center of the rectangle
	 * @return center of the triangle
	 */
	val center : Vector2f
		get() = Vector2f((minx + maxx) / 2, (miny + maxy) / 2)

	/**
	 * creates a new VAO based on the coordinates
	 * @return new VAO based on this object
	 */
	fun makeVAO() = VAO(x, y, width, height)
}