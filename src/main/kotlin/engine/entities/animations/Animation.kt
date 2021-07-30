package engine.entities.animations

import engine.PIf
import engine.loadScene
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import org.lwjgl.assimp.*

class Animation(obj : AIAnimation) {

	constructor(scene : AIScene, index : Int) : this(AIAnimation.create(scene.mAnimations()!![index]))

	var name = obj.mName().dataString()
	var duration = obj.mDuration()
	var ticksPerSecond = obj.mTicksPerSecond()

	var nodeChannels : Array<NodeAnim> = Array(obj.mNumChannels()) { NodeAnim(AINodeAnim.create(obj.mChannels()!![it])) }

	var meshChannels : Array<MeshAnim> = Array(obj.mNumMeshChannels()) { MeshAnim(AIMeshAnim.create(obj.mMeshChannels()!![it])) }

	companion object {
		operator fun invoke(path : String) = Companion(loadScene(path))
		operator fun invoke(scene : AIScene) = Array(scene.mNumAnimations()) { Animation(AIAnimation.create(scene.mAnimations()!![it])) }
	}
}

class MeshAnim(obj : AIMeshAnim) {
	var name = obj.mName().dataString()
	var keys = Array(obj.mNumKeys()) {MeshKey(obj.mKeys()[it])}
}

class MeshKey(obj : AIMeshKey) {
	val time = obj.mTime()
	val meshIndex = obj.mValue()
}

class NodeAnim(obj : AINodeAnim, antiRotate : Boolean = false) {
	val nodeName : String = obj.mNodeName().dataString()
	val positionKeys : Array<VectorKey> = Array(obj.mNumPositionKeys()) { VectorKey(obj.mPositionKeys()!![it]) }
	val rotationKeys : Array<QuatKey> = Array(obj.mNumRotationKeys()) {
		val ret = QuatKey(obj.mRotationKeys()!![it])
		if (antiRotate) {ret.rotateX(PIf);ret} else ret
	}
	val scalingKeys : Array<VectorKey> = Array(obj.mNumScalingKeys()) { VectorKey(obj.mScalingKeys()!![it]) }

	val mats : Array<Matrix4f> =
		Array(obj.mNumPositionKeys().coerceAtLeast(obj.mNumRotationKeys()).coerceAtLeast(obj.mNumScalingKeys())) {
			Matrix4f().translate(positionKeys[it.coerceAtMost(obj.mNumPositionKeys() - 1)].negate())
				.rotate(rotationKeys[it.coerceAtMost(obj.mNumRotationKeys() - 1)])
				.scale(scalingKeys[it.coerceAtMost(obj.mNumScalingKeys() - 1)])
		}

	val preState : AnimBehaviour = AnimBehaviour(obj.mPreState())
	val postState : AnimBehaviour = AnimBehaviour(obj.mPostState())
}

enum class AnimBehaviour {
	DEFAULT,
	CONSTANT,
	LINEAR,
	REPEAT;
	companion object {
		operator fun invoke(value : Int) : AnimBehaviour {
			return when (value) {
				0 -> DEFAULT
				1 -> CONSTANT
				2 -> LINEAR
				3 -> REPEAT
				else -> DEFAULT
			}
		}
	}
}

class VectorKey(obj : AIVectorKey) : Vector3f(obj.mValue().x(), obj.mValue().y(), obj.mValue().z()) {
	var time : Double = obj.mTime()
}

class QuatKey(obj : AIQuatKey) : Quaternionf(obj.mValue().x(), obj.mValue().y(), obj.mValue().z(), obj.mValue().w()) {
	var time : Double = obj.mTime()
}