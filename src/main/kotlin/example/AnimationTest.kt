package example

import engine.EnigView
import engine.PIf
import engine.entities.Camera3D
import engine.entities.animations.Animation
import engine.loadScene
import engine.opengl.EnigContext
import engine.opengl.EnigWindow
import engine.opengl.KeyState
import engine.opengl.bufferObjects.FBO
import engine.opengl.bufferObjects.VAO
import engine.opengl.bufferObjects.VBO3f
import engine.opengl.checkGLError
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.glDisable

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets animation test demo")
	glDisable(GL_CULL_FACE)
	try {
		val view = AnimationTestView(window)
		view.runIn(window)
	} catch (t : Throwable) {
		t.printStackTrace()
		checkGLError()
	}
	EnigContext.terminate()
}

class AnimationTestView(window : EnigWindow) : EnigView() {
	lateinit var vaos : Array<VAO>
	lateinit var shader : ShaderProgram


	val scene = loadScene("dfgod.dae")
	val anims = Animation(scene)

	val cam = Camera3D(window.aspectRatio)
	val input = window.inputHandler

	fun checkDuplicates(vao : VAO) {
		for (i in 0 until vao.vertexCount) {
			var count = 0
			for (j in 0 until vao.vertexCount) {
				if (j != i) {
					var dist = (vao.vbos[0] as VBO3f)[i].distanceSquared((vao.vbos[0] as VBO3f)[j])
					dist += (vao.vbos[2] as VBO3f)[i].distanceSquared((vao.vbos[2] as VBO3f)[j])
					if (dist < 0.00001) {
						++count
					}
				}
			}
			println(count)
		}
		println("face count ${vao.ibo.data.size / 3}")
	}

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)
		vaos = VAO(scene)
		checkDuplicates(vaos[0])
		shader = ShaderProgram("jointWeightsShader")
		window.inputEnabled = true
	}

	override fun loop(frameBirth: Long, dtime: Float): Boolean {
		FBO.prepareDefaultRender()
		shader.enable()
		shader[ShaderType.VERTEX_SHADER][0].set(cam.getMatrix())

		vaos[0].fullRender()

		handleMovement(dtime)

		return input.keys[GLFW.GLFW_KEY_ESCAPE] == KeyState.Released
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