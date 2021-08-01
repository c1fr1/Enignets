package example

import engine.EnigView
import engine.opengl.*
import org.lwjgl.opengl.GL11.*

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	val view = MainView(window)
	view.runInGLSafe(window)
	EnigContext.terminate()
}

class MainView(window : EnigWindow) : EnigView() {
	override fun loop(frameBirth: Long, dtime: Float): Boolean {
		return false
	}
}
