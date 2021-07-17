package engine.entities

import org.joml.Vector3f
import org.joml.Vector3fc

interface Orientation3D : Vector3fc {
	fun dirVec() : Vector3f
	fun getRelativePosition(vec : Vector3fc, target : Vector3f) : Vector3f
	fun getRelativePosition(vec : Vector3fc) = getRelativePosition(vec, Vector3f())
}