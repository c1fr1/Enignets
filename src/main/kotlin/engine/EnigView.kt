package engine

import engine.opengl.EnigContext
import engine.opengl.EnigWindow
import engine.opengl.checkGLError

abstract class EnigView {

	open fun generateResources(window : EnigWindow) {
		EnigContext.enterScope()
	}

	/**
	 * cleans up at the end of a frame
	 */
	open fun cleanUpResources() {
		EnigContext.exitScope()
	}

	/**
	 * loop that gets called every frame
	 * @return if the view should close after this frame ends
	 */
	abstract fun loop(frameBirth : Long, dtime : Float) : Boolean

	open fun runIn(window : EnigWindow) {
		generateResources(window)
		window.runView { frameBirth, dtime ->
			loop(frameBirth, dtime)
		}
		cleanUpResources()
	}

	open fun runInGLSafe(window : EnigWindow) {
		try {
			runIn(window)
		} catch (t : Throwable) {
			t.printStackTrace()
			checkGLError()
		}
	}
}