package example

import engine.EnigView
import engine.PIf
import engine.entities.Camera3D
import engine.entities.animations.fuckBlender
import engine.opengl.*
import engine.opengl.bufferObjects.FBO
import engine.opengl.bufferObjects.VAO
import engine.opengl.bufferObjects.VBO3f
import engine.opengl.jomlExtensions.plus
import engine.opengl.jomlExtensions.times
import engine.opengl.jomlExtensions.toFloatArray
import engine.opengl.shaders.ShaderProgram
import engine.opengl.shaders.ShaderType
import engine.shapes.Mesh
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.opengl.GL11.GL_CULL_FACE
import org.lwjgl.opengl.GL11.glDisable

fun main() {
	EnigContext.init()
	val window = EnigWindow("enignets animation test demo", GLContextPreset.standard3D.extended { glDisable(GL_CULL_FACE) })
	val view = MeshSimplifier(window)
	view.runInGLSafe(window)
	EnigContext.terminate()
}

class MeshSimplifier(window : EnigWindow) : EnigView() {
	lateinit var vao : VAO
	lateinit var shader : ShaderProgram

	val cam = Camera3D(window.aspectRatio)
	val input = window.inputHandler

	override fun generateResources(window: EnigWindow) {
		super.generateResources(window)

		fuckBlender = false
		val mesh = Mesh("humanoid.dae", 0)

		val map = IntArray(mesh.vertexCount) {-1}
		val newPoints = ArrayList<Vector3f>()
		val pointCount = ArrayList<Int>()

		for (v in 0 until mesh.vertexCount) {
			for (o in 0 until newPoints.size) {
				if (mesh.getVertex(v).distance(newPoints[o]) < 0.01) {
					newPoints[o] = (newPoints[o] * pointCount[o].toFloat() + mesh.getVertex(v)) / (pointCount[o].toFloat() + 1f)
					pointCount[o] += 1
					map[v] = o
					break
				}
			}
			if (map[v] < 0) {
				map[v] = newPoints.size
				newPoints.add(mesh.getVertex(v))
				pointCount.add(1)
			}
		}

		val newIndices = ArrayList<Int>()

		for (i in 0 until (mesh.indices.size / 3)) {
			val newA = map[mesh.indices[i * 3]]
			val newB = map[mesh.indices[i * 3 + 1]]
			val newC = map[mesh.indices[i * 3 + 2]]
			if (newA == newB || newA == newC || newB == newC) continue
			newIndices.add(newA)
			newIndices.add(newB)
			newIndices.add(newC)
		}

		val max = pointCount.maxOrNull()!!
		println("from ${mesh.vertexCount} to ${newPoints.size}")

		val colors = Array(newPoints.size) {Vector3f(pointCount[it].toFloat() / max.toFloat(), 0.1f, 0.1f)}


		val newMesh = Mesh(newIndices.toTypedArray().toIntArray(), newPoints.toTypedArray().toFloatArray())
		newMesh.writeToOBJ("out.obj")

		vao = VAO(arrayOf(VBO3f(newPoints.toTypedArray().toFloatArray()), VBO3f(colors.toFloatArray())), newIndices.toTypedArray().toIntArray())
		shader = ShaderProgram("bakedShader")
		window.inputEnabled = true
	}

	override fun loop(frameBirth: Long, dtime: Float): Boolean {
		FBO.prepareDefaultRender()
		shader.enable()
		shader[ShaderType.VERTEX_SHADER][0].set(cam.getMatrix())

		vao.fullRender()

		handleMovement(dtime)

		return input.keys[GLFW_KEY_ESCAPE] == KeyState.Released
	}

	private fun handleMovement(dtime : Float) {

		val ms = if (input.keys[GLFW.GLFW_KEY_LEFT_CONTROL].isDown) 0.5f else 2f

		if (input.keys[GLFW.GLFW_KEY_SPACE].isDown) cam.y += dtime * ms
		if (input.keys[GLFW.GLFW_KEY_LEFT_SHIFT].isDown) cam.y -= dtime * ms
		if (input.keys[GLFW.GLFW_KEY_D].isDown) cam.moveRelativeRight(dtime * ms)
		if (input.keys[GLFW.GLFW_KEY_A].isDown) cam.moveRelativeLeft(dtime * ms)
		if (input.keys[GLFW.GLFW_KEY_W].isDown) cam.moveRelativeForward(dtime * ms)
		if (input.keys[GLFW.GLFW_KEY_S].isDown) cam.moveRelativeBackward(dtime * ms)
		cam.updateAngles(input, dtime / 10f)
		cam.angles.x = cam.angles.x.coerceIn(-PIf / 2, PIf / 2)
	}
}