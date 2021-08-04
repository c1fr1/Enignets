package example

import engine.EnigView
import engine.entities.animations.Animation
import engine.entities.animations.Skeleton
import engine.entities.animations.fuckBlender
import engine.loadBoneData
import engine.loadScene
import engine.opengl.*
import engine.opengl.bufferObjects.SSBO
import engine.opengl.shaders.ComputeProgram
import org.joml.Matrix4f
import org.lwjgl.assimp.AIAnimation
import org.lwjgl.assimp.AIMesh
import org.lwjgl.opengl.GL11.*

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	//val view = MainView(window)
	//view.runInGLSafe(window)
	val scene = loadScene("dfgod.dae")
	val mesh = AIMesh.create(scene.mMeshes()!![0])
	val anim = Animation(scene, 0)
	val skeleton = Skeleton(scene, arrayOf("body_Bone", "body_Bone_001", "body_Bone_002", "body_Bone_003"))

	val boneData = loadBoneData(mesh)
	val positionBuffer = SSBO(mesh.mVertices(), false, false, fuckBlender)
	val boneIndexBuffer = SSBO(boneData.first, 4)
	val boneWeightBuffer = SSBO(boneData.second, 4)
	val outPosBuffer = SSBO(FloatArray(mesh.mNumVertices() * 4), 4, true, true)

	val shader = ComputeProgram("res/shaders/animComputeShader/compute.glsl")
	shader.enable()
	positionBuffer.bindToPosition(0)
	boneIndexBuffer.bindToPosition(1)
	boneWeightBuffer.bindToPosition(2)
	outPosBuffer.bindToPosition(3)

	shader[0] = Matrix4f()
	shader[1] = skeleton.getMats(anim, 0)

	shader.run(mesh.mNumVertices())
	SSBO.syncSSBOs()
	val processedData = outPosBuffer.retrieveGLData()

	for (f in processedData) {
		println(f)
	}

	checkGLError()


	EnigContext.terminate()
}

class MainView(window : EnigWindow) : EnigView() {
	override fun loop(frameBirth: Long, dtime: Float): Boolean {
		return false
	}
}