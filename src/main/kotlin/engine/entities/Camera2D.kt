package engine.entities

import engine.opengl.EnigWindow
import org.joml.*
import org.joml.Math.cos
import org.joml.Math.sin

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Camera2D(var projectionMatrix : Matrix4f, rotation : Float = 0f, pos : Vector2f = Vector2f()) :
	Camera, Orientation2D(rotation, pos) {

	constructor(aspectRatio : Float, totalHeight : Float = 100f) : this(Matrix4f().ortho(
		-totalHeight / 2f * aspectRatio,
		totalHeight / 2f * aspectRatio,
		-totalHeight / 2f,
		totalHeight / 2f,
		0f,
		1f))

	constructor(window : EnigWindow, totalHeight : Float = 100f) : this(window.aspectRatio, totalHeight)

	override fun getMatrix() : Matrix4f = Matrix4f(projectionMatrix).rotateY(-rotation).translate(-x, -y, 0f)

	override fun dirVec() = Vector2f(cos(rotation), sin(rotation))
}