package engine.entities

import engine.opengl.jomlExtensions.rotate
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector2fc

open class Orientation2D(var rotation : Float = 0f, pos : Vector2f = Vector2f()) : Vector2f(pos) {

	open fun dirVec() = Vector2f(Math.cos(rotation), Math.sin(rotation))

	open fun getRelativePosition(vec : Vector2fc, target : Vector2f) : Vector2f =
		vec.sub(this, target).rotate(-rotation)

	open fun getRelativePosition(vec : Vector2fc) = getRelativePosition(vec, Vector2f())
}