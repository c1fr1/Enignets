package engine.entities.animations

import engine.opengl.jomlExtensions.times
import org.joml.Matrix4f
import org.lwjgl.assimp.AIMatrix4x4
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.AIScene

/*fun Matrix4f(mat : AIMatrix4x4) = Matrix4f(
	mat.a1(), mat.a2(), mat.a3(), mat.a4(),
	mat.b1(), mat.b2(), mat.b3(), mat.b4(),
	mat.c1(), mat.c2(), mat.c3(), mat.c4(),
	mat.d1(), mat.d2(), mat.d3(), mat.d4()
)*/

fun AIMatrix4x4.toJoml() = Matrix4f(
	a1(), b1(), c1(), d1(),
	a2(), b2(), c2(), d2(),
	a3(), b3(), c3(), d3(),
	a4(), b4(), c4(), d4()
)

class Skeleton {
	val nodes : Array<Node>
	var rootNode : Int = 0
	constructor(nodes : Array<Node>, rootNode : Int) {
		this.nodes = nodes
		this.rootNode = rootNode
	}
	constructor(obj : AIScene, names : Array<String>) {
		val tmpNodes = Array<Node?>(names.size) {null}
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
				val transform = node.mTransformation().toJoml()
				val totalTransform = if (parent != null) transform * tmpNodes[parent]!!.totalTransform else Matrix4f()
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

	fun getMats(animation: Animation, frame : Int, globalInverse : Matrix4f) : Array<Matrix4f> {
		val ret = animation.getMats(frame)

		fun setMats(nodeID : Int) {
			for (child in nodes[nodeID].children) {
				val nodeGlobalTransform = ret[nodeID] * ret[child]
				ret[child] = nodeGlobalTransform
				//nodes[child].transformation * ret[nodeID]
				setMats(child)
			}
		}
		//ret[rootNode] = nodes[rootNode].transformation
		setMats(rootNode)
		for (i in ret.indices) {
			//ret[i] = ret[i] * nodes[i].totalTransform
			ret[i] = ret[i] * nodes[i].totalTransform
		}

		return ret
	}
}

class Node(val children : Array<Int>, val name : String, val transformation : Matrix4f, val id : Int, val parent : Int?, val totalTransform : Matrix4f) {

}