package engine.opengl

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE

class GLContextPreset(private val settingsSetter : () -> Unit) {

	fun createContext() {
		GL.createCapabilities()
		settingsSetter()
	}

	fun extended(additionalSettingsSetter : () -> Unit) = GLContextPreset {settingsSetter();additionalSettingsSetter()}

	companion object {
		val minimal = GLContextPreset {glClearColor(0.0f, 0.0f, 0.0f, 0.0f)}
		val standard = minimal.extended {
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			glEnable(GL_MULTISAMPLE)
		}
		val standard2D = standard
		val standard3D = standard.extended {
			glEnable(GL_DEPTH_TEST)
			glEnable(GL_CULL_FACE)
		}
	}
}