package engine.opengl

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwSetErrorCallback
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.openal.ALC10.*
import org.lwjgl.openal.ALCCapabilities
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import java.nio.ByteBuffer
import java.nio.IntBuffer

object EnigContext : EnigScope() {
	private var device: Long = 0
	private var context: Long = 0
	var alDeviceCapabilities : ALCCapabilities? = null
		private set
	var staticScope : Boolean = false

	override fun isLeaf(): Boolean = super.isLeaf() or staticScope

	fun init() {
		GLFWErrorCallback.createPrint(System.err).set()
		check(GLFW.glfwInit()) { "Unable to initialize GLFW" }
		initOpenAL()
	}

	private fun initOpenAL() {
		device = alcOpenDevice(null as ByteBuffer?)
		check(device != 0L) { "Failed to open the default device." }
		alDeviceCapabilities = ALC.createCapabilities(device)
		context = alcCreateContext(device, null as IntBuffer?)
		check(context != 0L) { "Failed to create an OpenAL context." }
		alcMakeContextCurrent(context)
		AL.createCapabilities(alDeviceCapabilities!!)
	}

	fun terminate() {
		glfwTerminate()
		alcDestroyContext(context)
		alcCloseDevice(device)
		glfwSetErrorCallback(null)!!.free()
	}
}