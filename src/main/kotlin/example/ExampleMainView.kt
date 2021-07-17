package example

import engine.EnigView
import engine.PIf
import engine.TAUf
import engine.entities.Camera2D
import engine.entities.Camera3D
import engine.measurePerformanceFor
import engine.opengl.EnigContext
import engine.opengl.EnigWindow
import engine.opengl.KeyState
import engine.opengl.bufferObjects.FBO
import engine.opengl.bufferObjects.VAO
import engine.opengl.bufferObjects.VBO
import engine.opengl.bufferObjects.VBO3f
import engine.opengl.checkGLError
import engine.opengl.jomlExtensions.minus
import engine.opengl.jomlExtensions.plus
import engine.opengl.jomlExtensions.rotate
import engine.opengl.jomlExtensions.times
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import engine.shapes.Box2d
import engine.shapes.Simplex2v3d
import org.joml.Random
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

var r = Random(100110562541)

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets demo")
	glDisable(GL_CULL_FACE)
	//glDisable(GL_DEPTH_TEST)
	try {
		val view = MainView(window)
		view.runIn(window)
	} catch (t : Throwable) {
		t.printStackTrace()
		checkGLError()
	}
	EnigContext.terminate()
}

class MainView(window : EnigWindow) : EnigView() {
	lateinit var vao: VAO
	lateinit var triangles: VAO
	lateinit var triangleBuffer: VBO3f
	lateinit var shader: ShaderProgram

	val cam = Camera3D(window.aspectRatio)
	val input = window.inputHandler

	val a = Simplex2v3d(
		0f, 0f, 1f,
		-1f, 0.5f, 1.2f,
		-1f, -0.5f, 1f
	)
	val b = Simplex2v3d(
		1.2f, -1f, 0f,
		0.9f, -1f, 2f,
		1f, 2f, 1f
	)

	var del = Vector3f(5f, 0.3f, 0.2f)

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)
		vao = VAO(-2f, -2f, -2f, 2f, -1f, 2f)
		shader = ShaderProgram("colorShader")
		window.inputEnabled = true
		r = Random(r.nextInt(Int.MAX_VALUE).toLong())

		triangleBuffer = VBO3f(getData(), true)
		triangles = VAO(arrayOf(triangleBuffer), intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8))
	}

	override fun loop(frameBirth: Long, dtime: Float): Boolean {
		FBO.prepareDefaultRender()
		shader.enable()
		shader[ShaderType.VERTEX_SHADER][0].set(cam.getMatrix())
		shader[ShaderType.FRAGMENT_SHADER][0].set(0.7f, 0.7f, 0.7f)
		/*vao.prepareRender()
		vao.drawTriangles()
		vao.unbind()*/
		triangles.fullRender()

		if (input.keys[GLFW_KEY_E] == KeyState.Released) generateTriangles()


		val ms = if (input.keys[GLFW_KEY_LEFT_CONTROL].isDown) 0.5f else 2f

		if (input.keys[GLFW_KEY_SPACE].isDown) cam.y += dtime * ms
		if (input.keys[GLFW_KEY_LEFT_SHIFT].isDown) cam.y -= dtime * ms
		if (input.keys[GLFW_KEY_D].isDown) cam.moveRelativeRight(dtime * ms)
		if (input.keys[GLFW_KEY_A].isDown) cam.moveRelativeLeft(dtime * ms)
		if (input.keys[GLFW_KEY_W].isDown) cam.moveRelativeForward(dtime * ms)
		if (input.keys[GLFW_KEY_S].isDown) cam.moveRelativeBackward(dtime * ms)
		cam.updateAngles(input, dtime / 10f)
		cam.angles.x = cam.angles.x.coerceIn(-PIf / 2, PIf / 2)
		return input.keys[GLFW_KEY_ESCAPE] == KeyState.Released
	}

	fun generateTriangles() {
		val seed = r.nextInt(Int.MAX_VALUE).toLong()

		println("seed : $seed")
		val rand = Random(seed)

		generateTriangles(rand)
	}

	fun generateTriangles(r : Random) {
		del = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()) * 5f
		a.a = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()) - del
		a.b = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()) - del
		a.c = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()) - del

		b.a = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat())
		b.b = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat())
		b.c = Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat())
		del *= r.nextFloat() * 2f

		triangleBuffer.data = getData()
	}

	fun getData() : FloatArray {

		val t = a.getIntersectT(del, b)

		val c = a + del * (t?: 1f)

		println("t : $t")

		return floatArrayOf(
			a.a.x, a.a.y, a.a.z,
			a.b.x, a.b.y, a.b.z,
			a.c.x, a.c.y, a.c.z,

			b.a.x, b.a.y, b.a.z,
			b.b.x, b.b.y, b.b.z,
			b.c.x, b.c.y, b.c.z,

			c.a.x, c.a.y, c.a.z,
			c.b.x, c.b.y, c.b.z,
			c.c.x, c.c.y, c.c.z,
		)
	}
}