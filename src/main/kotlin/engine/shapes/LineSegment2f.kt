package engine.shapes

import org.joml.Vector2f

class LineSegment2f : Ray2f {
	override var final = Vector2f(0f, 0f)
	override var fx : Float
		get() = final.x
		set(value) {final.x = value}
	override var fy : Float
		get() = final.y
		set(value) {final.y = value}

	override var delta : Vector2f
		get() = Vector2f(dx, dy)
		set(value) {
			dx = value.x
			dy = value.y
		}
	override var dx : Float
		get() = fx - x
		set(value) {fx = x + value}
	override var dy : Float
		get() = fy - y
		set(value) {fy = y + value}

	constructor(startX : Float, startY : Float, finalX : Float, finalY : Float) {
		x = startX
		y = startY
		fx = finalX
		fy = finalY
	}

	constructor(start : Vector2f, final : Vector2f) : this(start.x, start.y, final.x, final.y)
}