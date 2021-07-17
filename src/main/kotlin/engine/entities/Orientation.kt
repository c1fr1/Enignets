package engine.entities

import engine.PIf
import engine.opengl.InputHandler
import engine.opengl.jomlExtensions.rotate
import engine.opengl.jomlExtensions.rotateXYZ
import engine.opengl.jomlExtensions.rotateZYX
import org.joml.Math.*
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3fc

@Suppress("MemberVisibilityCanBePrivate")
open class Orientation(var angles : Vector3f = Vector3f(), pos : Vector3f = Vector3f()) : Orientation3D, Vector3f(pos) {

	var pitch
		get() = angles.x
		set(value) {angles.x = value}
	var yaw
		get() = angles.y
		set(value) {angles.y = value}
	var roll
		get() = angles.z
		set(value) {angles.z = value}

	override fun dirVec() = Vector3f(0f, 0f, -1f).rotateXYZ(-angles.x, -angles.y, -angles.z)

	override fun getRelativePosition(vec: Vector3fc, target: Vector3f) : Vector3f =
		vec.sub(this, target).rotateXYZ(-pitch, -yaw, -roll)

	fun updateAngles(input : InputHandler, sensitivity : Float) {
		angles.x += input.cursorDelY * sensitivity
		angles.y += input.cursorDelX * sensitivity
	}

	fun moveRelativeLeft(amount : Float) {
		val sin = sin(angles.y)
		val cos = cosFromSin(sin, angles.y)
		x -= cos * amount
		z -= sin * amount
	}

	fun moveRelativeRight(amount : Float) {
		val sin = sin(angles.y)
		val cos = cosFromSin(sin, angles.y)
		x += cos * amount
		z += sin * amount
	}

	fun moveRelativeForward(amount : Float) {
		val sin = sin(angles.y)
		val cos = cosFromSin(sin, angles.y)
		x += sin * amount
		z -= cos * amount
	}

	fun moveRelativeBackward(amount : Float) {
		val sin = sin(angles.y)
		val cos = cosFromSin(sin, angles.y)
		x -= sin * amount
		z += cos * amount
	}
}