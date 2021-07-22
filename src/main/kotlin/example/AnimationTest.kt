package example

import engine.EnigView
import engine.PIf
import engine.entities.Camera3D
import engine.entities.animations.Animation
import engine.opengl.EnigContext
import engine.opengl.EnigWindow
import engine.opengl.KeyState
import engine.opengl.bufferObjects.FBO
import engine.opengl.bufferObjects.VAO
import engine.opengl.checkGLError
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.glDisable

fun main() {
	val anim = Animation("testDragon.dae")
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
	lateinit var vao: VAO
	lateinit var shader: ShaderProgram

	val anim = Animation("testDragon.dae")

	val cam = Camera3D(window.aspectRatio)
	val input = window.inputHandler

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)
		vao = VAO("testDragon.dae")
		shader = ShaderProgram("colorShader")
		window.inputEnabled = true
	}

	override fun loop(frameBirth: Long, dtime: Float): Boolean {
		FBO.prepareDefaultRender()
		shader.enable()
		shader[ShaderType.VERTEX_SHADER][0].set(cam.getMatrix())
		shader[ShaderType.FRAGMENT_SHADER][0].set(0.7f, 0.7f, 0.7f)

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