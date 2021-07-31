package example

import engine.*
import engine.entities.Camera2D
import engine.entities.Camera3D
import engine.opengl.*
import engine.opengl.bufferObjects.*
import engine.opengl.jomlExtensions.*
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import engine.shapes.Ray3f
import engine.shapes.edgesIntersectionT
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.glDisable

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	try {
		val view = CollisionTestMain(window)
		view.runIn(window)
	} catch (t : Throwable) {
		t.printStackTrace()
		checkGLError()
	}
	EnigContext.terminate()
}

class CollisionTestMain(window : EnigWindow) : EnigView() {
	lateinit var aBuffer : VBO3f
	lateinit var oBuffer : VBO3f
	lateinit var aVAO : VAO
	lateinit var oVAO : VAO
	lateinit var graphVAO : VAO
	lateinit var shader : ShaderProgram
	lateinit var graphShader : ShaderProgram
	lateinit var texShader : ShaderProgram

	lateinit var fbo : FBO

	val guiCam = Camera2D(window, 2f)
	val cam = Camera3D(window)
	val input = window.inputHandler

	val window = window

	var lv = 0f

	var ea = Ray3f(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat())
	var eb = Ray3f(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat())
	val e : Pair<Ray3f, Ray3f>
		get() = Pair(ea, eb)
	var oa = Ray3f(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat())
	var ob = Ray3f(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat())
	val o : Pair<Ray3f, Ray3f>
		get() = Pair(oa, ob)

	var coeffs = FloatArray(5)

	override fun generateResources(window: EnigWindow) {
		ea *= 10f
		ea -= Vector3f(5f, 5f, 5f)
		oa *= 10f
		oa -= Vector3f(5f, 5f, 5f)
		eb *= 10f
		eb -= Vector3f(5f, 5f, 5f)
		ob *= 10f
		ob -= Vector3f(5f, 5f, 5f)

		super.generateResources(window)
		shader = ShaderProgram("CollisionTestShader")
		graphShader = ShaderProgram("distSquaredGraphShader")
		texShader = ShaderProgram("textureShader")
		window.inputEnabled = true

		val subsections = 100

		aBuffer = VBO3f(getData(ea, eb, subsections), true)
		oBuffer = VBO3f(getData(oa, ob, subsections), true)

		val clrBuffer = VBO2f((Array(subsections * subsections) {Vector2f((it / subsections).toFloat() / subsections.toFloat(), (it % subsections).toFloat() / subsections.toFloat())}).toFloatArray())

		aVAO = VAO(arrayOf(aBuffer, clrBuffer), getIndices(100))
		oVAO = VAO(arrayOf(oBuffer, clrBuffer), getIndices(100))
		graphVAO = VAO(-1f, -1f, 2f, 2f)
		fbo = FBO(1000, 1000)
	}

	override fun loop(frameBirth: Long, dtime: Float): Boolean {

		renderScene()

		handleMovement(dtime)

		if (input.keys[GLFW_KEY_Q].isDown) {
			lv -= dtime / 2f
		}
		if (input.keys[GLFW_KEY_E].isDown) {
			lv += dtime / 2f
		}

		movePoints()

		return input.keys[GLFW_KEY_ESCAPE] == KeyState.Released
	}

	fun handleMovement(dtime : Float) {

		println(edgesIntersectionT(e, o))
		val ms = if (input.keys[GLFW_KEY_LEFT_CONTROL].isDown) 0.5f else 2f
		if (input.keys[GLFW_KEY_SPACE].isDown) cam.y += dtime * ms
		if (input.keys[GLFW_KEY_LEFT_SHIFT].isDown) cam.y -= dtime * ms
		if (input.keys[GLFW_KEY_D].isDown) cam.moveRelativeRight(dtime * ms)
		if (input.keys[GLFW_KEY_A].isDown) cam.moveRelativeLeft(dtime * ms)
		if (input.keys[GLFW_KEY_W].isDown) cam.moveRelativeForward(dtime * ms)
		if (input.keys[GLFW_KEY_S].isDown) cam.moveRelativeBackward(dtime * ms)

		if (input.keys[GLFW_KEY_Z].isDown) {
			cam.set(ea.lerp(eb, lv).closestPointTo(oa.lerp(ob, lv)))
			println(ea.lerp(eb, lv).closestDistance(oa.lerp(ob, lv)))
		}
		if (input.keys[GLFW_KEY_X].isDown) {
			cam.set(oa.lerp(ob, lv).closestPointTo(ea.lerp(eb, lv)))
			println(ea.lerp(eb, lv).closestDistance(oa.lerp(ob, lv)))
		}
		cam.updateAngles(input, dtime / 10f)
		cam.angles.x = cam.angles.x.coerceIn(-PIf / 2, PIf / 2)
	}

	fun renderScene() {
		fbo.bind()
		window.setViewport(0, 0, fbo.boundTexture.width, fbo.boundTexture.height)

		if (input.keys[GLFW_KEY_C] == KeyState.Released) FBO.clearCurrentFrameBuffer()

		FBO.prepareDefaultRender()
		window.setViewport()

		shader.enable()
		shader[ShaderType.VERTEX_SHADER][0].set(cam.getMatrix())
		shader[ShaderType.FRAGMENT_SHADER][0].set(lv)
		shader[ShaderType.FRAGMENT_SHADER][1].set(ea.lerp(eb, lv).closestTTo(oa.lerp(ob, lv)))
		aVAO.fullRender()
		shader[ShaderType.FRAGMENT_SHADER][1].set(oa.lerp(ob, lv).closestTTo(ea.lerp(eb, lv)))
		oVAO.fullRender()

		texShader.enable()
		fbo.boundTexture.bind()
		texShader[ShaderType.VERTEX_SHADER][0].set(guiCam.getMatrix().translate(1f, 0.5f, 0f).scale(0.5f))
		graphVAO.fullRender()
	}

	fun movePoints() {

		val selectedLoc = cam.dirVec() + cam

		if (input.keys[GLFW_KEY_0].isDown) {
			ea.delta.set(ea + ea.delta - selectedLoc)
			ea.set(selectedLoc)
			aBuffer.data = getData(ea, eb, 100)
		}
		if (input.keys[GLFW_KEY_1].isDown) {
			ea.delta.set(selectedLoc - ea)
			aBuffer.data = getData(ea, eb, 100)
		}
		if (input.keys[GLFW_KEY_2].isDown) {
			eb.delta.set(eb + eb.delta - selectedLoc)
			eb.set(selectedLoc)
			aBuffer.data = getData(ea, eb, 100)
		}
		if (input.keys[GLFW_KEY_3].isDown) {
			eb.delta.set(selectedLoc - eb)
			aBuffer.data = getData(ea, eb, 100)
		}
		if (input.keys[GLFW_KEY_4].isDown) {
			oa.delta.set(oa + oa.delta - selectedLoc)
			oa.set(selectedLoc)
			oBuffer.data = getData(oa, ob, 100)
		}
		if (input.keys[GLFW_KEY_5].isDown) {
			oa.delta.set(selectedLoc - oa)
			oBuffer.data = getData(oa, ob, 100)
		}
		if (input.keys[GLFW_KEY_6].isDown) {
			ob.delta.set(ob + ob.delta - selectedLoc)
			ob.set(selectedLoc)
			oBuffer.data = getData(oa, ob, 100)
		}
		if (input.keys[GLFW_KEY_7].isDown) {
			ob.delta.set(selectedLoc - ob)
			oBuffer.data = getData(oa, ob, 100)
		}

		if (input.keys[GLFW_KEY_R] == KeyState.Released) {
			if (lv > 0.5) {
				eb = ea.lerp(eb, lv)
				ob = oa.lerp(ob, lv)
				lv = 1f
			} else {
				ea = ea.lerp(eb, lv)
				oa = oa.lerp(ob, lv)
				lv = 0f
			}
			aBuffer.data = getData(ea, eb, 100)
			oBuffer.data = getData(oa, ob, 100)
		}
	}

	fun getData(a : Ray3f, b : Ray3f, subsections : Int) : FloatArray {
		return FloatArray(3 * subsections * subsections) {
			val vi = it / 3
			val r = vi % subsections
			val c = vi / subsections
			val u  = r.toFloat() / (subsections - 1).toFloat()
			val v  = c.toFloat() / (subsections - 1).toFloat()
			when (it % 3) {
				0 -> a.xAt(u).lerp(b.xAt(u), v)
				1 -> a.yAt(u).lerp(b.yAt(u), v)
				2 -> a.zAt(u).lerp(b.zAt(u), v)
				else -> Float.NaN
			}
		}
	}

	fun getIndices(subsections : Int) : IntArray {
		return IntArray(3 * 2 * (subsections - 1) * (subsections - 1)) {
			val square = it / 6
			val i = square % (subsections - 1)
			val j = square / (subsections - 1)
			when (it % 6) {
				0 -> i + j * (subsections - 1)
				1 -> i + (j + 1) * (subsections - 1)
				2 -> i + j * (subsections - 1) + 1
				3 -> i + j * (subsections - 1)
				4 -> i + (j + 1) * (subsections - 1)
				else -> i + (j + 1) * (subsections - 1) + 1
			}
		}
	}
}