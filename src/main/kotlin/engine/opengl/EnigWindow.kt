package engine.opengl

import engine.input.InputHandler
import engine.makeGLFWImage
import engine.opengl.shaders.ShaderProgram
import org.lwjgl.glfw.Callbacks
import org.lwjgl.system.MemoryUtil
import java.lang.RuntimeException
import org.joml.Vector2i
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

private fun getPrimaryMonitorDimensions() : Vector2i {
	val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
	if (vidMode != null) {
		return Vector2i(vidMode.width(), vidMode.height())
	} else {
		throw RuntimeException("unable to find the dimensions of the primary monitor")
	}
}

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class EnigWindow {
	open val id: Long
	var width = 0
		private set
	var height = 0
		private set
	open val aspectRatio
		get() = width.toFloat() / height.toFloat()
	open var fps = 144
	open var inputEnabled = false
		set(value) {
			glfwSetInputMode(id, GLFW_CURSOR, if (value) GLFW_CURSOR_DISABLED else GLFW_CURSOR_NORMAL)
			field = value
		}

	open val inputHandler : InputHandler

	/**
	 * creates a window
	 * @param windowSize size of window
	 * @param title name of the window
	 * @param decorated if the window should be decorated
	 * @param resizeable if the window should be resizeable
	 */
	constructor(windowSize : Vector2i, title: String, preset : GLContextPreset = GLContextPreset.standard,
	            decorated: Boolean = true, resizeable: Boolean = true) {

		// Configure GLFW
		glfwDefaultWindowHints()
		glfwWindowHint(GLFW_SAMPLES, 4)
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, if (resizeable) 1 else 0)
		glfwWindowHint(GLFW_DECORATED, if (decorated) 1 else 0)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

		// Create the window
		width = windowSize.x()
		height = windowSize.y()
		id = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL) //fullscreen
		if (id == MemoryUtil.NULL) {
			throw RuntimeException("Failed to create the GLFW window")
		}

		inputHandler = InputHandler(id)

		glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL)

		// Make the window visible
		glfwShowWindow(id)
		if (resizeable) {
			glfwSetWindowSizeCallback(id) { _: Long, w: Int, h: Int ->
				width = w
				height = h
				setViewport()
			}
		}

		setContext()
		preset.createContext()
		// Enable v-sync
		//glfwSwapInterval(1)
	}

	/**
	 * creates a window
	 * @param w width
	 * @param h height
	 * @param title name of the window
	 * @param decorated if the window should be decorated
	 * @param resizeable if the window should be resizeable
	 */
	constructor(w: Int, h: Int, title: String, preset : GLContextPreset = GLContextPreset.standard,
	            decorated: Boolean = true, resizeable: Boolean = true)
			: this(Vector2i(w, h), title, preset, decorated, resizeable)

	/**
	 * creates a window
	 * @param w width
	 * @param h height
	 * @param title name of the window
	 * @param title name of the window
	 * @param decorated if it should be decorated or not
	 * @param resizeable if the window should be resizeable
	 */
	constructor(w : Int, h : Int, title : String, iconPath : String,
	            preset : GLContextPreset = GLContextPreset.standard, decorated: Boolean = false,
	            resizeable: Boolean = true)
			: this(w, h, title, preset, decorated, resizeable) {
		setIcon(iconPath)
	}

	/**
	 * creates an undecorated window
	 * @param title name of the window
	 * @param decorated if the window should be decorated or not
	 * @param resizeable if the window should be resizeable or not
	 */
	constructor(title : String, preset : GLContextPreset = GLContextPreset.standard, decorated : Boolean = false,
	            resizeable : Boolean = true)
			: this(getPrimaryMonitorDimensions(), title, preset, decorated, resizeable)

	/**
	 * creates an undecorated window
	 * @param title name of the window
	 * @param iconPath path to the icon for the window
	 * @param decorated if the window should be decorated or not
	 * @param resizeable if the window should be resizeable or not
	 */
	constructor(title : String, iconPath : String, preset : GLContextPreset = GLContextPreset.standard,
	            decorated : Boolean = false, resizeable : Boolean = true)
			: this(title, preset, decorated, resizeable) {
		setIcon(iconPath)
	}

	open fun setContext() = glfwMakeContextCurrent(id)

	open fun<T, R> runFrame(runData : T, drawingFun : (T) -> R) : R {
		setContext()

		val ret = drawingFun(runData)

		update()
		return ret
	}

	open fun runView(drawingFun : (frameBirth : Long, dtime : Float) -> Boolean) {
		setContext()
		val startTime = System.nanoTime()
		var birth = 0L
		var delta : Float
		do {
			val newTime = System.nanoTime() - startTime
			delta = (newTime - birth).toFloat() / 1e9f
			birth = newTime

			val shouldExit = drawingFun(birth, delta)
			update()
		} while (!glfwWindowShouldClose(id) && !shouldExit)
	}

	/**
	 * close the window, delete all resources, close openGL, openAL, and clean things up
	 */
	open fun terminate() {
		// Free the window callbacks and destroy the window
		Callbacks.glfwFreeCallbacks(id)
		glfwDestroyWindow(id)
	}

	/**
	 * syncs the fps and
	 */
	open fun update() {
		//sync(fps)
		inputHandler.update()
		ShaderProgram.disable()
		glfwSwapBuffers(id)
		glfwPollEvents()
		checkGLError()
	}

	private var variableYieldTime: Long = 0
	private var lastTime: Long = 0

	// TODO ensure that this isn't just wasting time
	/**
	 * An accurate sync method that adapts automatically
	 * to the system it runs on to provide reliable results.
	 *
	 * @param fps The desired frame rate, in frames per second
	 * @author kappa (On the LWJGL Forums)
	 */
	private fun sync(fps: Int) {
		if (fps <= 0) return
		val sleepTime = (1000000000 / fps).toLong() // nanoseconds to sleep this frame
		// yieldTime + remainder micro & nano seconds if smaller than sleepTime
		val yieldTime = sleepTime.coerceAtMost(variableYieldTime + sleepTime % (1000 * 1000))
		var overSleep: Long = 0 // time the sync goes over by
		try {
			while (true) {
				val t = System.nanoTime() - lastTime
				if (t < sleepTime - yieldTime) {
					//Thread.sleep(1);
				} else if (t < sleepTime) {
					// burn the last few CPU cycles to ensure accuracy
					Thread.yield()
				} else {
					overSleep = t - sleepTime
					break // exit while loop
				}
			}
		} finally {
			lastTime = System.nanoTime() - overSleep.coerceAtMost(sleepTime)

			// auto tune the time sync should yield
			if (overSleep > variableYieldTime) {
				// increase by 200 microseconds (1/5 a ms)
				variableYieldTime = sleepTime.coerceAtMost(variableYieldTime + 200 * 1000)
			} else if (overSleep < variableYieldTime - 200 * 1000) {
				// decrease by 2 microseconds
				variableYieldTime = (variableYieldTime - 2 * 1000).coerceAtLeast(0)
			}
		}
	}

	open fun setViewport() {
		val frameBufferWidth = IntArray(1)
		val frameBufferHeight = IntArray(1)
		glfwGetFramebufferSize(id, frameBufferWidth, frameBufferHeight)
		setViewport(0, 0, frameBufferWidth[0], frameBufferHeight[0])
	}

	open fun setViewport(x : Int, y : Int, width : Int, height : Int) {
		glViewport(x, y, width, height)
	}

	open fun setIcon(imagePath: String) {
		val image = makeGLFWImage(imagePath)
		val buffer = GLFWImage.malloc(1)
		buffer.put(0, image)
		glfwSetWindowIcon(id, buffer)
	}
}