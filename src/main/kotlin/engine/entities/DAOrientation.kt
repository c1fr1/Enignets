package engine.entities

import org.joml.*

//Dynamic Axis Orientation
@Suppress("unused")
open class DAOrientation(rotation : Quaternionfc = Quaternionf(), pos : Vector3fc = Vector3f()) :
	Orientation3D, Vector3f(pos) {

	var rotation : Quaternionf = Quaternionf(rotation)

	override fun dirVec(): Vector3f = rotation.transform(Vector3f(0f, 0f, 1f))

	override fun getRelativePosition(vec: Vector3fc, target: Vector3f) : Vector3f =
		rotation.transformInverse(vec.sub(this, target))

	fun pitch(angle : Float) : DAOrientation {
		rotation.rotateX(angle)
		return this
	}

	fun yaw(angle : Float) : DAOrientation {
		rotation.rotateY(angle)
		return this
	}

	fun roll(angle : Float) : DAOrientation {
		rotation.rotateZ(angle)
		return this
	}

}