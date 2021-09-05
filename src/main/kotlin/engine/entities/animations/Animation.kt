package engine.entities.animations

import engine.loadScene
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import org.lwjgl.assimp.*
import kotlin.math.sqrt

var fuckBlender = true

open class Animation(obj : AIAnimation) {

	constructor(scene : AIScene, index : Int) : this(AIAnimation.create(scene.mAnimations()!![index]))

	open var name = obj.mName().dataString()
	open var duration = obj.mDuration()
	open var ticksPerSecond = obj.mTicksPerSecond()

	open var nodeChannels : Array<NodeAnim> = Array(obj.mNumChannels()) {if (fuckBlender && it == 0) NodeAnim(AINodeAnim.create(obj.mChannels()!![it]), true) else NodeAnim(AINodeAnim.create(obj.mChannels()!![it]))}

	open fun getMats(frame : Int) = Array(nodeChannels.size) {nodeChannels[it].mats[frame]}

	open var meshChannels : Array<MeshAnim> = Array(obj.mNumMeshChannels()) { MeshAnim(AIMeshAnim.create(obj.mMeshChannels()!![it])) }

	companion object {
		operator fun invoke(path : String) = Companion(loadScene(path))
		operator fun invoke(scene : AIScene) = Array(scene.mNumAnimations()) { Animation(AIAnimation.create(scene.mAnimations()!![it])) }
	}
}

open class MeshAnim(obj : AIMeshAnim) {
	open var name = obj.mName().dataString()
	open var keys = Array(obj.mNumKeys()) {MeshKey(obj.mKeys()[it])}
}

open class MeshKey(obj : AIMeshKey) {
	open val time = obj.mTime()
	open val meshIndex = obj.mValue()
}

open class NodeAnim(obj : AINodeAnim, antiRotate : Boolean = false) {
	open val nodeName : String = obj.mNodeName().dataString()
	open val positionKeys : Array<VectorKey> = Array(obj.mNumPositionKeys()) { VectorKey(obj.mPositionKeys()!![it]) }
	open val rotationKeys : Array<QuatKey> = Array(obj.mNumRotationKeys()) {
		val ret = QuatKey(obj.mRotationKeys()!![it])
		if (antiRotate) {ret.mul(Quaternionf(sqrt(2.0f) / 2.0f, 0f, 0f, sqrt(2.0f) / 2.0f));ret} else ret
	}
	open val scalingKeys : Array<VectorKey> = Array(obj.mNumScalingKeys()) { VectorKey(obj.mScalingKeys()!![it]) }

	open val mats : Array<Matrix4f> =
		Array(obj.mNumPositionKeys().coerceAtLeast(obj.mNumRotationKeys()).coerceAtLeast(obj.mNumScalingKeys())) {
			Matrix4f().translate(positionKeys[it.coerceAtMost(obj.mNumPositionKeys() - 1)].negate())
				.rotate(rotationKeys[it.coerceAtMost(obj.mNumRotationKeys() - 1)])
				.scale(scalingKeys[it.coerceAtMost(obj.mNumScalingKeys() - 1)])
		}

	open val preState : AnimBehaviour = AnimBehaviour(obj.mPreState())
	open val postState : AnimBehaviour = AnimBehaviour(obj.mPostState())
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

open class VectorKey(obj : AIVectorKey) : Vector3f(obj.mValue().x(), obj.mValue().y(), obj.mValue().z()) {
	open var time : Double = obj.mTime()
}

open class QuatKey(obj : AIQuatKey) : Quaternionf(obj.mValue().x(), obj.mValue().y(), obj.mValue().z(), obj.mValue().w()) {
	open var time : Double = obj.mTime()
}