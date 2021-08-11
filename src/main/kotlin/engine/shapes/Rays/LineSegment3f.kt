package engine.shapes.Rays

import org.joml.Vector3f

class LineSegment3f : Ray3f {
	override var final = Vector3f(0f, 0f, 0f)
	override var fx : Float
		get() = final.x
		set(value) {final.x = value}
	override var fy : Float
		get() = final.y
		set(value) {final.y = value}
	override var fz : Float
		get() = final.z
		set(value) {final.z = value}

	override var delta : Vector3f
		get() = Vector3f(dx, dy, dz)
		set(value) {
			dx = value.x
			dy = value.y
			dz = value.z
		}
	override var dx : Float
		get() = fx - x
		set(value) {fx = x + value}
	override var dy : Float
		get() = fy - y
		set(value) {fy = y + value}
	override var dz : Float
		get() = fz - z
		set(value) {fz = z + value}

	constructor(startX : Float, startY : Float, startZ : Float, finalX : Float, finalY : Float, finalZ : Float) {
		x = startX
		y = startY
		fx = finalX
		fy = finalY
	}

	constructor(start : Vector3f, final : Vector3f) : this(start.x, start.y, start.z, final.x, final.y, final.z)
}