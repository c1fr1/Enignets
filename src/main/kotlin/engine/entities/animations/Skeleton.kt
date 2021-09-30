package engine.entities.animations

import engine.opengl.jomlExtensions.times
import org.joml.Matrix4f
import org.lwjgl.assimp.*

fun AIMatrix4x4.toJoml() = Matrix4f(
	a1(), b1(), c1(), d1(),
	a2(), b2(), c2(), d2(),
	a3(), b3(), c3(), d3(),
	a4(), b4(), c4(), d4()
)

open class Skeleton {
	open val nodes : Array<Node>
	open var rootNode : Int = 0
	constructor(nodes : Array<Node>, rootNode : Int) {
		this.nodes = nodes
		this.rootNode = rootNode
	}

	constructor(obj : AIScene, names : Array<String>) {
		val tmpNodes = Array<Node?>(names.size) {null}
		val globalTransform = obj.mRootNode()!!.mTransformation().toJoml()
		var rootNodeSet = false

		fun addNamed(node : AINode, parent : Int?) {
			val id = if (names.contains(node.mName().dataString())) {
				val id = names.indexOf(node.mName().dataString())
				if (rootNodeSet) {
					rootNode = id
					rootNodeSet = true
				}
				val children = Array(node.mNumChildren()) {
					names.indexOf(AINode.create(node.mChildren()!![it]).mName().dataString())
				}
				var transform = node.mTransformation().toJoml()

				val totalTransform = if (parent != null) tmpNodes[parent]!!.totalTransform * transform else transform
				tmpNodes[id] = Node(children, node.mName().dataString(), transform, id, parent, totalTransform)
				id
			} else {
				null
			}
			for (i in 0 until node.mNumChildren()) {
				addNamed(AINode.create(node.mChildren()!![i]), id)
			}
		}

		addNamed(obj.mRootNode()!!, null)

		nodes = Array(tmpNodes.size) {tmpNodes[it]!!}
	}

	constructor(obj : AIScene, mesh : AIMesh) : this(obj, Array(mesh.mNumBones()) { AIBone.create(mesh.mBones()!![it]).mName().dataString()})
	constructor(obj : AIScene, meshi : Int) : this(obj, AIMesh.create(obj.mMeshes()!![meshi]))

	fun getMats(animation : Animation, frame : Int) : Array<Matrix4f> {
		val ret = getAnimMats(animation, frame)
		for (i in ret.indices) {
			ret[i] = ret[i] * nodes[i].totalTransform.invertAffine(Matrix4f())
		}

		return ret
	}

	fun getAnimMats(animation : Animation, frame : Int) : Array<Matrix4f> {
		val ret = Array(nodes.size) { nodes[it].transformation }
		for (nanim in animation.nodeChannels) {
			ret[nodes.indexOfFirst { it.name == nanim.nodeName }] = nanim.mats[frame]
		}

		fun setMats(nodeID : Int) {
			for (child in nodes[nodeID].children) {
				val nodeGlobalTransform = ret[nodeID] * ret[child]
				ret[child] = nodeGlobalTransform
				setMats(child)
			}
		}
		setMats(rootNode)

		return ret
	}
}

open class Node(open val children : Array<Int>, open val name : String, open val transformation : Matrix4f, open val id : Int, open val parent : Int?, open val totalTransform : Matrix4f)
