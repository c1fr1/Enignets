package example

import engine.EnigView
import engine.entities.animations.Animation
import engine.entities.animations.Skeleton
import engine.loadBoneData
import engine.loadScene
import engine.opengl.*
import engine.opengl.bufferObjects.SSBO
import engine.opengl.bufferObjects.SSBO3f
import engine.opengl.bufferObjects.SSBO4f
import engine.opengl.bufferObjects.SSBO4i
import engine.opengl.shaders.ComputeProgram
import org.joml.Matrix4f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.opengl.GL11.*

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	val view = MainView(window)
	view.runInGLSafe(window)

	EnigContext.terminate()
}

class MainView(window : EnigWindow) : EnigView() {

	lateinit var shader : ComputeProgram

	lateinit var positionBuffer : SSBO3f
	lateinit var boneIndexBuffer : SSBO4i
	lateinit var boneWeightBuffer : SSBO4f
	lateinit var outPosBuffer : SSBO4f

	lateinit var skeleton : Skeleton
	lateinit var anim : Animation

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)

		val scene = loadScene("dfgod.dae")
		val mesh = AIMesh.create(scene.mMeshes()!![0])
		anim = Animation(scene, 0)
		skeleton = Skeleton(scene, arrayOf("body_Bone", "body_Bone_001", "body_Bone_002", "body_Bone_003"))

		val boneData = loadBoneData(mesh)

		positionBuffer = SSBO(mesh.mVertices())
		boneIndexBuffer = SSBO(boneData.first, 4) as SSBO4i
		boneWeightBuffer = SSBO(boneData.second, 4) as SSBO4f
		outPosBuffer = SSBO(FloatArray(mesh.mNumVertices() * 4), 4, true, true) as SSBO4f

		shader = ComputeProgram("res/shaders/animComputeShader/compute.glsl")
	}

	override fun loop(frameBirth: Long, dtime: Float) : Boolean {
		shader.enable()
		positionBuffer.bindToPosition(0)
		boneIndexBuffer.bindToPosition(1)
		boneWeightBuffer.bindToPosition(2)
		outPosBuffer.bindToPosition(3)

		shader[0] = Matrix4f()
		shader[1] = skeleton.getMats(anim, 0)

		shader.run(positionBuffer.vertexCount)
		SSBO.syncSSBOs()
		return false
	}
}