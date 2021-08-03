package example

import engine.EnigView
import engine.opengl.*
import engine.opengl.bufferObjects.SSBO
import engine.opengl.shaders.ComputeProgram
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL45.glGetTextureImage

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	//val view = MainView(window)
	//view.runInGLSafe(window)
	val ssbo = SSBO(FloatArray(128 * 4 * 4) { it.toFloat() }, 4, true, true)
	val ssboTarget = SSBO(FloatArray(128 * 4 * 4), 4, true, true)
	for (i in ssboTarget.data.indices) {
		ssboTarget.data[i] = -1f
	}

	val shader = ComputeProgram("res/shaders/animComputeShader/compute.glsl")
	shader.enable()
	ssbo.bindToPosition(0)
	ssboTarget.bindToPosition(1)
	shader.run(128 * 4)
	SSBO.syncSSBOs()
	val processedData = ssboTarget.retrieveGLData()

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