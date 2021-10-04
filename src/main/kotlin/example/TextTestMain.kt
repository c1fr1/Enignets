package example

import engine.EnigView
import engine.entities.Camera2D
import engine.opengl.*
import engine.opengl.bufferObjects.FBO
import engine.opengl.bufferObjects.VAO
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import java.nio.file.Paths

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard2D)
	val view = TextTestMain(window)
	view.runInGLSafe(window)

	EnigContext.terminate()
}


class TextTestMain(window : EnigWindow) : EnigView() {

	val input = window.inputHandler
	val cam = Camera2D(window, 25f)

	lateinit var font : Font
	lateinit var vao : VAO
	lateinit var shader : ShaderProgram

	override fun generateResources(window: EnigWindow) {
		font = Font("times.ttf", 64f, 512, 512)
		vao = VAO(0f, 0f, 1f, 1f)
		shader = ShaderProgram("textShader")
	}
	override fun loop(frameBirth: Long, dtime: Float) : Boolean {
		FBO.prepareDefaultRender()
		shader.enable()
		font.bind()
		vao.prepareRender()

		lateinit var wmats : Array<Matrix4f>
		lateinit var tcmats : Array<Matrix4f>

		font.getMats("hello world, why are you here?", cam.getMatrix()) {w, tc ->
			wmats = w
			tcmats = tc
		}

		for (i in wmats.indices) {
			shader[ShaderType.VERTEX_SHADER, 0] = wmats[i]
			shader[ShaderType.VERTEX_SHADER, 1] = tcmats[i]
			vao.drawTriangles()
		}
		vao.unbind()

		return input.keys[GLFW_KEY_ESCAPE] == KeyState.Released
	}
}