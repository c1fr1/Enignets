@file:Suppress("unused")

package engine.entities

import org.joml.*

interface Camera {
	fun getMatrix() : Matrix4f
	fun transformVec(v : Vector4fc, target : Vector4f) : Vector4f = v.mul(getMatrix(), target)
	fun transformVec(v : Vector4f) = transformVec(v, v)
	fun transformVec(v : Vector3fc, target: Vector3f) : Vector3f = v.mulProject(getMatrix(), target)
	fun transformVec(v : Vector3f) = transformVec(v, v)
}