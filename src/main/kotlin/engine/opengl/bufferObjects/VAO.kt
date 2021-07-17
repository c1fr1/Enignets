package engine.opengl.bufferObjects

import engine.shapes.Box2d
import engine.shapes.Box3d
import engine.getResourceStream
import engine.opengl.GLResource
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.Assimp
import org.lwjgl.assimp.Assimp.aiProcessPreset_TargetRealtime_MaxQuality
import org.lwjgl.assimp.Assimp.aiProcess_Triangulate
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.glDrawElementsInstanced
import java.nio.ByteBuffer
import java.util.ArrayList

@Suppress("unused", "MemberVisibilityCanBePrivate")
class VAO : GLResource {

	private var ibo: IBO

	var vbos: Array<VBO<*>>

	val verticesPerShape : Int

	/**
	 * create a new VAO with vertices, and indices specifying the number of vertices per shape
	 * @param vertices vertices fo the object (3 dimensional)
	 * @param ind indices of the faces (vps per face)
	 * @param vps number of vertices associated with each individual shape
	 */
	constructor(vertices : FloatArray, ind : IntArray, vps : Int = 3) : super(glGenVertexArrays()) {
		glBindVertexArray(id)
		vbos = arrayOf(VBO(vertices, 3))
		vbos[0].assignToVAO(0)
		vaoIDs.add(id)
		ibo = IBO(ind)
		verticesPerShape = vps
	}

	constructor(vbos : Array<VBO<*>>, ind: IntArray, vps : Int = 3) : super(glGenVertexArrays()) {
		glBindVertexArray(id)
		this.vbos = vbos
		for (i in this.vbos.indices) this.vbos[i].assignToVAO(i)
		vaoIDs.add(id)
		ibo = IBO(ind)
		verticesPerShape = vps
	}

	/**
	 * create a square object
	 * @param x corner xpos
	 * @param y corner ypos
	 * @param width width of the box
	 * @param height height of the box
	 */
	constructor(x: Float, y: Float, width: Float, height: Float) : this(floatArrayOf(
		x, y + height, 0f,
		x, y, 0f,
		x + width, y, 0f,
		x + width, y + height, 0f
	), intArrayOf(0, 1, 3, 3, 1, 2)) {
		addVBO(VBO(VBO.squareTC, 2))
	}

	constructor(box : Box2d) : this(box.x, box.y, box.width, box.height)

	constructor(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float) :
			super(glGenVertexArrays()) {
		glBindVertexArray(id)
		val vertices = floatArrayOf(
			minx, miny, minz,  // 0
			minx, miny, maxz,  // 1
			maxx, miny, maxz,  // 2
			maxx, miny, minz,  // 3
			minx, maxy, minz,  // 4
			maxx, maxy, minz,  // 5
			minx, miny, minz,  // 6
			minx, miny, maxz,  // 7
			minx, maxy, maxz,  // 8
			minx, maxy, minz,  // 9
			maxx, miny, maxz,  // 10
			maxx, maxy, maxz,  // 11
			maxx, miny, minz,  // 12
			maxx, miny, maxz,  // 13
			maxx, maxy, maxz,  // 14
			maxx, maxy, minz,  // 15
			minx, maxy, minz,  // 16
			minx, maxy, maxz
		)
		val textureCoordinates = floatArrayOf(
			0.5f, 1f / 3f,  // 0
			0f, 1f / 3f,  // 1
			0f, 0f,  // 2
			0.5f, 0f,  // 3
			1f, 1f / 3f,  // 4
			1f, 0f,  // 5
			0f, 1f / 3f,  // 6
			0.5f, 1f / 3f,  // 7
			0.5f, 2f / 3f,  // 8
			0f, 2f / 3f,  // 9
			1f, 1f / 3f,  // 10
			1f, 2f / 3f,  // 11
			0f, 2f / 3f,  // 12
			0f, 1f,  // 13
			0.5f, 1f,  // 14
			0.5f, 2f / 3f,  // 15
			1f, 2f / 3f,  // 16
			1f, 1f
		)
		ibo = IBO(intArrayOf(
			0, 2, 1,
			0, 3, 2,
			4, 5, 0,
			5, 3, 0,
			8, 6, 7,
			9, 6, 8,
			8, 10, 11,
			8, 7, 10,
			15, 14, 13,
			15, 13, 12,
			17, 14, 15,
			17, 15, 16
		))
		vaoIDs.add(id)
		vbos = arrayOf(VBO(vertices, 3), VBO(textureCoordinates, 2))
		vbos[0].assignToVAO(0)
		vbos[1].assignToVAO(1)
		verticesPerShape = 3
	}

	constructor(box : Box3d) : this(box.minx, box.miny, box.minz, box.maxx, box.maxy, box.maxz)

	/**
	 * creates a vao (and vbos) from a obj file
	 * @param path path to the file
	 */
	constructor(path: String) : super(glGenVertexArrays()) {
		vaoIDs.add(id)
		glBindVertexArray(id)
		val fileData = ByteBuffer.wrap(getResourceStream(path).readAllBytes())
		val scene = Assimp.aiImportFile(fileData,
			aiProcessPreset_TargetRealtime_MaxQuality or aiProcess_Triangulate)!!

		val mesh = AIMesh.create(scene.mMeshes()!![0])

		vbos = arrayOf(
			VBO(FloatArray(mesh.mNumVertices() * 3) {i -> when (i % 3) {
				0 -> mesh.mVertices()[i / 3].x()
				1 -> mesh.mVertices()[i / 3].y()
				2 -> mesh.mVertices()[i / 3].x()
				else -> Float.NaN
			}}, 3),
			VBO(FloatArray(mesh.mNumVertices() * 2) {i -> when (i % 2) {
				0 -> mesh.mTextureCoords(i / 2)!!.get().x()
				1 -> mesh.mTextureCoords(i / 2)!!.get().y()
				else -> Float.NaN
			}}, 2),
			VBO(FloatArray(mesh.mNumVertices() * 3) {i -> when (i % 3) {
				0 -> mesh.mNormals()!![i / 3].x()
				1 -> mesh.mNormals()!![i / 3].y()
				2 -> mesh.mNormals()!![i / 3].z()
				else -> Float.NaN
			}}, 3)
		)
		vbos[0].assignToVAO(0)
		vbos[1].assignToVAO(1)
		vbos[2].assignToVAO(2)

		ibo = IBO(IntArray(mesh.mNumFaces() * 3) {i -> mesh.mFaces().mIndices()[i]})
		verticesPerShape = 3
	}

	/**
	 * fully prepares and renders the object, only use this if rendering a single object that looks like this
	 */
	fun fullRender() {
		prepareRender()
		draw()
		unbind()
	}

	/**
	 * binds the vao and enables all of the vbos, prepares the vao to be drawn
	 */
	fun prepareRender() {
		glBindVertexArray(id)
		for (i in vbos.indices) {
			glEnableVertexAttribArray(i)
		}
	}

	/**
	 * draws the object, assuming that the VAO represents points
	 */
	fun drawPoints() {
		glDrawElements(GL_POINTS, ibo.size, GL_UNSIGNED_INT, 0)
	}

	fun drawPointsInstanced(count: Int) {
		glDrawElementsInstanced(GL_POINTS, ibo.size, GL_UNSIGNED_INT, 0, count)
	}

	/**
	 * draws the object, assuming that the VAO represents lines
	 */
	fun drawLines() {
		glDrawElements(GL_LINES, ibo.size, GL_UNSIGNED_INT, 0)
	}

	fun drawLinesInstanced(count: Int) {
		glDrawElementsInstanced(GL_LINES, ibo.size, GL_UNSIGNED_INT, 0, count)
	}

	/**
	 * draws the object, assuming that the VAO represents triangles
	 */
	fun drawTriangles() {
		glDrawElements(GL_TRIANGLES, ibo.size, GL_UNSIGNED_INT, 0)
	}

	fun drawTrianglesInstanced(count: Int) {
		glDrawElementsInstanced(GL_TRIANGLES, ibo.size, GL_UNSIGNED_INT, 0, count)
	}

	/**
	 * draws the object, checking how many vertices per shapes the vao holds
	 */
	fun draw() {
		when (verticesPerShape) {
			1 -> drawPoints()
			2 -> drawLines()
			3 -> drawTriangles()
		}
	}

	fun drawInstanced(count: Int) {
		when (verticesPerShape) {
			1 -> drawPointsInstanced(count)
			2 -> drawLinesInstanced(count)
			3 -> drawTrianglesInstanced(count)
		}
	}

	/**
	 * disables the vbos then unbinds the vao, cleans up after rendering
	 */
	fun unbind() {
		for (i in vbos.indices) {
			glDisableVertexAttribArray(i)
		}
		glBindVertexArray(0)
	}

	/**
	 * binds the vao and enables the specified vbos, prepares the vao to be drawn
	 * @param vboset
	 * the vbos to be enabled
	 */
	fun prepareRender(vboset: IntArray) {
		glBindVertexArray(id)
		for (i in vboset) {
			glEnableVertexAttribArray(i)
		}
	}

	/**
	 * disables the vbos specified then unbinds the vao, cleans up after rendering
	 * @param vboset
	 * the vbos that should be disabled
	 */
	fun unbind(vboset: IntArray) {
		for (i in vboset) {
			glDisableVertexAttribArray(i)
		}
		glBindVertexArray(0)
	}

	/**
	 * adds a vbo to the vao
	 * @param newVBO new vbo
	 */
	fun addVBO(newVBO: VBO<*>) {
		val tempVBOL = Array(vbos.size + 1) {i ->
			when (i) {
				vbos.size -> newVBO
				else -> vbos[i]
			}
		}
		glBindVertexArray(id)
		newVBO.assignToVAO(vbos.size)
		vbos = tempVBOL
	}

	/**
	 * adds a vbo to the vbos in the vao
	 * @param info information for the new vbo
	 * @param size vector size of the vbo
	 */
	fun addVBO(info: FloatArray, size: Int) = addVBO(VBO(info, size))

	/**
	 * returns the number of vertices in the vao
	 * @return number of vertices in the vao
	 */
	val vertxCount: Int get() = vbos[0].vertexCount

	operator fun get(i : Int) = vbos[i]

	/**
	 * deletes the vao
	 */
	override fun destroy() {
		glDeleteVertexArrays(id)
		vaoIDs.removeIf { it == id }
	}

	/**
	 * deletes all vbos in the vao
	 */
	fun deleteVBOS() {
		for (v in vbos) {
			v.destroy()
		}
	}

	companion object {
		var vaoIDs = ArrayList<Int>()
	}
}