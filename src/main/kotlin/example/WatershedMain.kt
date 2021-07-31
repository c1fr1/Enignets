package example

import engine.EnigView
import engine.entities.Camera2D
import engine.opengl.*
import engine.opengl.bufferObjects.FBO
import engine.opengl.bufferObjects.VAO
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard2D)
	try {
		val view = WatershedMain(window)
		view.runIn(window)
	} catch (t : Throwable) {
		t.printStackTrace()
		checkGLError()
	}
	EnigContext.terminate()
}

class WatershedMain(window : EnigWindow) : EnigView() {
	lateinit var vao : VAO
	lateinit var shader : ShaderProgram

	val cam = Camera2D(1.5f, window.aspectRatio)
	val input = window.inputHandler
	val representation = Watershed(arrayOf(
		intArrayOf(3, 4, 2, 2),
		intArrayOf(2, 3, 3, 1),
		intArrayOf(1, 2, 3, 2),
		intArrayOf(1, 1, 2, 3)
	), 5)

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)
		vao = VAO(-0.5f, -0.5f, 1f, 1f)
		shader = ShaderProgram("colorShader")
	}

	override fun loop(frameBirth : Long, dtime : Float) : Boolean {
		FBO.prepareDefaultRender()
		shader.enable()
		renderWatershed()

		if (input.keys[GLFW.GLFW_KEY_SPACE] == KeyState.Released) {
			representation.runWatershedStep()
		}
		return input.keys[GLFW.GLFW_KEY_ESCAPE] == KeyState.Released
	}

	fun renderWatershed() {
		vao.prepareRender()
		for (y in representation.yIndices) {
			for (x in representation.xIndices) {
				shader[ShaderType.VERTEX_SHADER][0].set(
					cam.getMatrix()
						.scale(0.25f)
						.translate(x.toFloat() - 1.5f, 1.5f - y.toFloat(), 0f)
				)
				shader[ShaderType.FRAGMENT_SHADER][0].set(representation[x, y])
				vao.drawTriangles()
			}
		}
		vao.unbind()
	}
}

class Watershed(var values : Array<IntArray>, val maxValue : Int) {

	var returnState : Array<IntArray> = Array(values.size) {IntArray(values[it].size)}

	var queue : Queue<Vector2i> = LinkedBlockingQueue()

	private var maxLabel = 0

	init {
		for (i in 1..maxValue) {
			for (y in yIndices) {
				for (x in xIndices) {
					if (values[y][x] == i) {
						queue.offer(Vector2i(x, y))
					}
				}
			}
		}
	}

	fun runWatershedStep() {
		if (queue.isNotEmpty()) {
			runWatershedStep(queue.remove())
		}
	}

	fun runWatershedStep(location : Vector2i) {
		var ret : Int? = null
		for (dx in -1..1) {
			for (dy in -1..1) {
				val x = location.x + dx
				val y = location.y + dy
				if ((dx != 0 || dy != 0) && y in yIndices && x in xIndices) {
					if (returnState[y][x] > 0) {
						if (ret == null) {
							ret = returnState[y][x]
						} else if (ret != returnState[y][x]) {
							returnState[location.y][location.x] = -1
							return
						}
					}
				}
			}
		}
		if (ret == null) {
			++maxLabel
			returnState[location.y][location.x] = maxLabel
		} else {
			returnState[location.y][location.x] = ret
		}
	}

	val xIndices
		get() = values[0].indices

	val yIndices
		get() = values.indices

	//Used for getting the color of the pixel when rendering
	operator fun get(x : Int, y : Int) : Vector3f {
		return when (returnState[y][x]) {
			0 -> Vector3f((1 + values[y][x]).toFloat() / (maxValue + 1))//unset (greyscale)
			-1 -> Vector3f(1f, 0f, 0f)// watershed (red)

			1 -> Vector3f(0f, 1f, 0f)//hardcoded the first section to be green
			2 -> Vector3f(0f, 0f, 1f)//hardcoded the second section to be blue
			else -> Vector3f(0f, 0f, 0f)// black is an error stat ig
		}
	}
}