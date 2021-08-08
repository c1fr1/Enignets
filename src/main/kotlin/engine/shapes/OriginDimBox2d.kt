package engine.shapes

import org.joml.Vector2f
import org.joml.Vector2fc

class OriginDimBox2d : Box2d {
	override var minx : Float
		get() = x
		set(value) {
			width += x - value
			x = value
		}
	override var maxx : Float
		get() = x
		set(value) { width += value - x }
	override var miny : Float
		get() = y
		set(value) {
			height += y - value
			y = value
		}
	override var maxy : Float
		get() = y
		set(value) { height += value - y }

	override var x : Float = 0f
	override var y : Float = 0f

	override var width : Float = 0f
	override var height : Float = 0f

	constructor() : super()

	constructor(x : Float, y : Float, width : Float, height : Float) {
		this.x = x
		this.y = y
		this.width = width
		this.height = height
	}

	constructor(origin : Vector2fc, dims : Vector2fc) {
		x = origin.x()
		y = origin.y()
		width = dims.x()
		height = dims.y()
	}

	override fun translate(delta : Vector2fc) = translate(delta.x(), delta.y())

	override fun translate(x : Float, y : Float) : OriginDimBox2d {
		this.x += x
		this.y += y
		return this
	}
}