package example

import engine.EnigView
import engine.PIf
import engine.entities.Camera3D
import engine.entities.animations.Animation
import engine.entities.animations.Skeleton
import engine.loadBoneData
import engine.loadScene
import engine.opengl.*
import engine.opengl.bufferObjects.*
import engine.opengl.shaders.ComputeProgram
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.opengl.GL11.*

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	val view = MainView(window)
	view.runInGLSafe(window)

	EnigContext.terminate()
}

class MainView(window : EnigWindow) : EnigView() {

	lateinit var computeShader : ComputeProgram
	lateinit var colorShader : ShaderProgram

	lateinit var positionBuffer : SSBO3f
	lateinit var boneIndexBuffer : SSBO4i
	lateinit var boneWeightBuffer : SSBO4f
	lateinit var outPosBuffer : SSBO4f

	lateinit var vao : VAO

	lateinit var skeleton : Skeleton
	lateinit var anim : Animation

	val cam = Camera3D(window.aspectRatio)

	val input = window.inputHandler

	var frame = 0

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)

		val scene = loadScene("dfgod.dae")
		val mesh = AIMesh.create(scene.mMeshes()!![0])
		anim = Animation(scene, 0)
		skeleton = Skeleton(scene, arrayOf("body_Bone", "body_Bone_001", "body_Bone_002", "body_Bone_003"))

		val boneData = loadBoneData(mesh)

		positionBuffer = SSBO3f(mesh.mVertices())
		boneIndexBuffer = SSBO4i(boneData.first)
		boneWeightBuffer = SSBO4f(boneData.second)
		outPosBuffer = SSBO4f(FloatArray(mesh.mNumVertices() * 4), true, true)

		vao = VAO(arrayOf(outPosBuffer), IBO(mesh.mFaces()))

		computeShader = ComputeProgram("res/shaders/animComputeShader/compute.glsl")
		colorShader = ShaderProgram("ssboColorShader")

		window.inputEnabled = true
	}

	override fun loop(frameBirth: Long, dtime: Float) : Boolean {
		FBO.prepareDefaultRender()

		computeAnimationPos()

		SSBO.syncSSBOs()

		renderVAO()

		handleMovement(dtime)

		return input.keys[GLFW_KEY_ESCAPE].isDown
	}

	fun renderVAO() {
		colorShader.enable()
		outPosBuffer.bindToPosition(3)
		colorShader[ShaderType.VERTEX_SHADER, 0] = cam.getMatrix()
		colorShader[ShaderType.FRAGMENT_SHADER, 0] = Vector3f(1f, 0f, 0f)
		vao.fullRender()
	}

	fun computeAnimationPos() {

		frame = (frame + 1) % 60

		computeShader.enable()
		positionBuffer.bindToPosition(0)
		boneIndexBuffer.bindToPosition(1)
		boneWeightBuffer.bindToPosition(2)
		outPosBuffer.bindToPosition(3)

		computeShader[0] = Matrix4f()
		computeShader[1] = skeleton.getMats(anim, frame)

		computeShader.run(positionBuffer.vertexCount)
	}

	private fun handleMovement(dtime : Float) {

		val ms = if (input.keys[GLFW.GLFW_KEY_LEFT_CONTROL].isDown) 0.5f else 2f

		if (input.keys[GLFW.GLFW_KEY_SPACE].isDown) cam.y += dtime * ms
		if (input.keys[GLFW.GLFW_KEY_LEFT_SHIFT].isDown) cam.y -= dtime * ms
		if (input.keys[GLFW.GLFW_KEY_D].isDown) cam.moveRelativeRight(dtime * ms)
		if (input.keys[GLFW.GLFW_KEY_A].isDown) cam.moveRelativeLeft(dtime * ms)
		if (input.keys[GLFW.GLFW_KEY_W].isDown) cam.moveRelativeForward(dtime * ms)
		if (input.keys[GLFW.GLFW_KEY_S].isDown) cam.moveRelativeBackward(dtime * ms)
		cam.updateAngles(input, dtime / 10f)
		cam.angles.x = cam.angles.x.coerceIn(-PIf / 2, PIf / 2)
	}
}