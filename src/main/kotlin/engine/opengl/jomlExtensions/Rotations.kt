@file:Suppress("unused")

package engine.opengl.jomlExtensions

import org.joml.*
import org.joml.Math.*

fun Vector3fc.rotateXYZ(x : Float, y : Float, z : Float, target : Vector3f) : Vector3f =
	rotateX(x, target).rotateY(y).rotateZ(z)
fun Vector3fc.rotateXYZ(angles : Vector3fc, other : Vector3f) = rotateXYZ(angles.x(), angles.y(), angles.z(), other)
fun Vector3f.rotateXYZ(angles : Vector3fc) = rotateXYZ(angles, this)
fun Vector3f.rotateXYZ(x : Float, y : Float, z : Float) : Vector3f = rotateXYZ(x, y, z, this)

fun Vector3fc.rotateZYX(x : Float, y : Float, z : Float, target : Vector3f) : Vector3f =
	rotateZ(z, target).rotateY(y).rotateX(x)
fun Vector3fc.rotateZYX(angles : Vector3fc, other : Vector3f) = rotateZYX(angles.x(), angles.y(), angles.z(), other)
fun Vector3f.rotateZYX(angles : Vector3fc) = rotateZYX(angles, this)
fun Vector3f.rotateZYX(x : Float, y : Float, z : Float) : Vector3f = rotateZYX(x, y, z, this)

fun Vector4fc.rotateXYZ(x : Float, y : Float, z : Float, target : Vector4f) : Vector4f =
	rotateX(x, target).rotateY(y).rotateZ(z)
fun Vector4fc.rotateXYZ(angles : Vector3fc, other : Vector4f) = rotateXYZ(angles.x(), angles.y(), angles.z(), other)
fun Vector4f.rotateXYZ(angles : Vector3fc) = rotateXYZ(angles, this)
fun Vector4f.rotateXYZ(x : Float, y : Float, z : Float) : Vector4f = rotateXYZ(x, y, z, this)

fun Vector4fc.rotateZYX(x : Float, y : Float, z : Float, target : Vector4f) : Vector4f =
	rotateZ(z, target).rotateY(y).rotateX(x)
fun Vector4fc.rotateZYX(angles : Vector3fc, other : Vector4f) = rotateZYX(angles.x(), angles.y(), angles.z(), other)
fun Vector4f.rotateZYX(angles : Vector3fc) = rotateZYX(angles, this)
fun Vector4f.rotateZYX(x : Float, y : Float, z : Float) : Vector4f = rotateZYX(x, y, z, this)

fun Vector2fc.rotate(angle : Float, target : Vector2f) : Vector2f {
	val sin = sin(angle)
	val cos = cosFromSin(sin, angle)
	target.x =  x() * cos + y() * sin
	target.y = -x() * sin + y() * cos
	return target
}
fun Vector2f.rotate(angle : Float) = rotate(angle, this)