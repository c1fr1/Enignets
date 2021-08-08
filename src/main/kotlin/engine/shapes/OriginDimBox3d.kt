package engine.shapes

import org.joml.Vector3fc

class OriginDimBox3d : Box3d {
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
	override var minz : Float
		get() = z
		set(value) {
			depth += z - value
			z = value
		}
	override var maxz : Float
		get() = z
		set(value) { depth += value - z }

	override var x : Float = 0f
	override var y : Float = 0f
	override var z : Float = 0f

	override var width : Float = 0f
	override var height : Float = 0f
	override var depth : Float = 0f

	constructor() : super()

	constructor(x : Float, y : Float, z : Float, width : Float, height : Float, depth : Float) {
		this.x = x
		this.y = y
		this.z = z
		this.width = width
		this.height = height
		this.depth = depth
	}

	constructor(origin : Vector3fc, dims : Vector3fc) {
		x = origin.x()
		y = origin.y()
		z = origin.z()
		width = dims.x()
		height = dims.y()
		height = dims.z()
	}

	override fun translate(delta : Vector3fc) = translate(delta.x(), delta.y(), delta.z())

	override fun translate(x : Float, y : Float, z : Float) : OriginDimBox3d {
		this.x += x
		this.y += y
		this.z += z
		return this
	}
}