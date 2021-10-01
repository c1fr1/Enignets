package example

import engine.*
import engine.entities.Camera3D
import engine.entities.animations.Animation
import engine.entities.animations.Skeleton
import engine.opengl.*
import engine.opengl.bufferObjects.*
import engine.opengl.jomlExtensions.times
import engine.opengl.shaders.ComputeProgram
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import engine.shapes.Mesh
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.assimp.AIMesh
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
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
	lateinit var vertCompShader : ComputeProgram
	lateinit var colorShader : ShaderProgram

	lateinit var positionBuffer : SSBO3f
	lateinit var normalBuffer : SSBO3f
	lateinit var boneIndexBuffer : SSBO4i
	lateinit var boneWeightBuffer : SSBO4f

	lateinit var swordPosBuf : SSBO3f
	lateinit var swordNormBuf : SSBO3f

	lateinit var outPosBuffer : SSBO4f
	lateinit var outNormalBuffer : SSBO4f

	lateinit var outSwordPosBuffer : SSBO4f
	lateinit var outSwordNormBuffer : SSBO4f

	lateinit var vao : VAO
	lateinit var sphere : VAO
	lateinit var swordVAO : VAO

	lateinit var skeleton : Skeleton
	lateinit var anim : Animation

	val swordMesh = Mesh("dfd7tool.dae", 0)

	val cam = Camera3D(window.aspectRatio)

	val input = window.inputHandler

	var frame = 0

	override fun generateResources(window: EnigWindow) {

		super.generateResources(window)

		val scene = loadScene("humanoid.dae")
		val mesh = AIMesh.create(scene.mMeshes()!![0])
		anim = Animation(scene, 0)
		skeleton = Skeleton(scene, 0)

		val boneData = loadBoneData(mesh)

		sphere = VAO(loadScene("s peer.dae"), 0)


		val swordAIMesh = AIMesh.create(loadScene("dfd7tool.dae").mMeshes()!![0])
		swordPosBuf = SSBO3f(swordMesh.vdata)
		swordNormBuf = SSBO3f(swordAIMesh.mNormals()!!)

		outSwordPosBuffer = SSBO4f(FloatArray(swordPosBuf.vertexCount * 4), true)
		outSwordNormBuffer = SSBO4f(FloatArray(swordNormBuf.vertexCount * 4), true)

		swordVAO = VAO(arrayOf(outSwordPosBuffer, outSwordNormBuffer), IBO(swordMesh.indices))

		positionBuffer = SSBO3f(mesh.mVertices())
		normalBuffer = SSBO3f(mesh.mNormals()!!)
		boneIndexBuffer = SSBO4i(boneData.first)
		boneWeightBuffer = SSBO4f(boneData.second)

		outPosBuffer = SSBO4f(FloatArray(mesh.mNumVertices() * 4), true)
		outNormalBuffer = SSBO4f(FloatArray(mesh.mNumVertices() * 4), true)

		vao = VAO(arrayOf(outPosBuffer, outNormalBuffer), IBO(mesh.mFaces()))

		computeShader = ComputeProgram("res/shaders/animComputeShader/compute.glsl")
		vertCompShader = ComputeProgram("res/shaders/vertComputeShader/compute.glsl")
		colorShader = ShaderProgram("ssboColorShader")

		cam.z = 5f

		window.inputEnabled = true
	}

	override fun loop(frameBirth: Long, dtime: Float) : Boolean {
		FBO.prepareDefaultRender()

		computeAnimationPos()
		computeSwordPos()

		SSBO.syncSSBOs()

		renderVAO()

		handleMovement(dtime)

		return input.keys[GLFW_KEY_ESCAPE].isDown
	}

	fun renderVAO() {
		colorShader.enable()

		colorShader[ShaderType.VERTEX_SHADER, 0] = cam.getMatrix()
		colorShader[ShaderType.VERTEX_SHADER, 1] = cam.normalize(Vector3f())
		colorShader[ShaderType.FRAGMENT_SHADER, 0] = Vector3f(1f, 1f, 1f)

		swordVAO.fullRender()
		vao.fullRender()
		sphere.prepareRender()

		for (node in skeleton.nodes) {
			colorShader[ShaderType.FRAGMENT_SHADER, 0] = Vector3f(1f, 0f, 0f)
			colorShader[ShaderType.VERTEX_SHADER, 0] = (cam.getMatrix() * node.totalTransform.invertAffine(Matrix4f())).scale(0.03f)
			sphere.drawTriangles()
		}

		val mats = skeleton.getAnimMats(anim, frame / 3)

		for (m in mats) {

			colorShader[ShaderType.FRAGMENT_SHADER, 0] = Vector3f(0f, 1f, 0f)
			colorShader[ShaderType.VERTEX_SHADER, 0] = (cam.getMatrix() * m).scale(0.03f)
			sphere.drawTriangles()
		}
		sphere.unbind()
	}

	fun computeAnimationPos() {

		frame = (frame + 1) % (anim.numFrames * 3)

		computeShader.enable()
		positionBuffer.bindToPosition(0)
		normalBuffer.bindToPosition(1)
		boneIndexBuffer.bindToPosition(2)
		boneWeightBuffer.bindToPosition(3)

		outPosBuffer.bindToPosition(4)
		outNormalBuffer.bindToPosition(5)

		computeShader[0] = Matrix4f()
		val mats = skeleton.getMats(anim, frame / 3)
		computeShader[1] = mats

		computeShader.run(positionBuffer.vertexCount)
	}

	fun computeSwordPos() {
		vertCompShader.enable()
		vertCompShader[0] = Matrix4f().scale(0.3f)
		swordPosBuf.bindToPosition(0)
		swordNormBuf.bindToPosition(1)

		outSwordPosBuffer.bindToPosition(2)
		outSwordNormBuffer.bindToPosition(3)

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