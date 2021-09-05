package engine.opengl.bufferObjects

import engine.entities.animations.fuckBlender
import engine.loadBoneData
import engine.loadScene
import engine.opengl.GLResource
import engine.shapes.bounds.Bound2f
import engine.shapes.bounds.Bound3f
import org.lwjgl.assimp.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.glDrawElementsInstanced


@Suppress("unused", "MemberVisibilityCanBePrivate")
open class VAO : GLResource {

	open var ibo : IBO

	open var vbos : Array<VBO<*>>

	open val verticesPerShape : Int

	/**
	 * create a new VAO with vertices, and indices specifying the number of vertices per shape
	 * @param vertices vertices fo the object (3 dimensional)
	 * @param ind indices of the faces (vps per face)
	 * @param vps number of vertices associated with each individual shape
	 */
	constructor(vertices : FloatArray, ind : IntArray, vps : Int = 3, dynamic : Boolean = false) : super(glGenVertexArrays()) {
		glBindVertexArray(id)
		vbos = arrayOf(VBO(vertices, 3, dynamic))
		vbos[0].assignToVAO(0)
		ibo = IBO(ind)
		verticesPerShape = vps
	}

	constructor(vbos : Array<VBO<*>>, ind : IntArray, vps : Int = 3) : this(vbos, IBO(ind), vps)

	constructor(vbos : Array<VBO<*>>, ind : IBO, vps : Int = 3) : super(glGenVertexArrays()) {
		glBindVertexArray(id)
		this.vbos = vbos
		for (i in this.vbos.indices) this.vbos[i].assignToVAO(i)
		ibo = ind
		ibo.bind()
		verticesPerShape = vps
	}

	/**
	 * create a square object
	 * @param x corner xpos
	 * @param y corner ypos
	 * @param width width of the box
	 * @param height height of the box
	 */
	constructor(x: Float, y: Float, width: Float, height: Float, dynamic : Boolean = false) : this(floatArrayOf(
		x, y + height, 0f,
		x, y, 0f,
		x + width, y, 0f,
		x + width, y + height, 0f
	), intArrayOf(0, 1, 3, 3, 1, 2), 3, dynamic) {
		addVBO(VBO(VBO.squareTC, 2))
	}

	constructor(box : Bound2f, dynamic : Boolean = false) : this(box.x, box.y, box.width, box.height, dynamic)

	constructor(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float, dynamic : Boolean = false) :
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
		vbos = arrayOf(VBO(vertices, 3, dynamic), VBO(textureCoordinates, 2, dynamic))
		vbos[0].assignToVAO(0)
		vbos[1].assignToVAO(1)
		verticesPerShape = 3
	}

	constructor(box : Bound3f, dynamic : Boolean = false) : this(box.minx, box.miny, box.minz, box.maxx, box.maxy, box.maxz, dynamic)

	/**
	 * creates a vao (and vbos) from a assimp mesh
	 * vbos are in the following order, if data for a specific vbo is not given, the following vbos will be moved down a slot
	 * 0 | vertexes
	 * 1 | texture coordinates (if there is texture data for multiple textures, those vbos will be inserted after this in order)
	 * 2 | normals
	 * 3 | bone indexes
	 * 4 | bone weights
	 * @param mesh assimp mesh
	 */
	constructor(mesh : AIMesh, dynamic : Boolean = false) : super(glGenVertexArrays()) {
		glBindVertexArray(id)
		val tempVBOs = arrayListOf<VBO<*>>()
		tempVBOs.add(VBO(mesh.mVertices(), dynamic, fuckBlender))
		var textureIndex = 0
		while (mesh.mTextureCoords(textureIndex) != null) {
			tempVBOs.add(VBO(mesh.mTextureCoords(textureIndex)!!))
			++textureIndex
		}
		if (mesh.mNormals() != null) {
			tempVBOs.add(VBO(mesh.mNormals()!!, dynamic, fuckBlender))
		}

		if (mesh.mBones() != null) {
			val weightData = loadBoneData(mesh)

			tempVBOs.add(VBO(weightData.first, 4, dynamic))
			tempVBOs.add(VBO(weightData.second, 4, dynamic))
		}

		vbos = tempVBOs.toTypedArray()
		for (i in vbos.indices) vbos[i].assignToVAO(i)

		val tempIBO = arrayListOf<Int>()
		for (f in mesh.mFaces().filter { it.mNumIndices() == 3 }) {
			tempIBO.add(f.mIndices()[0])
			tempIBO.add(f.mIndices()[1])
			tempIBO.add(f.mIndices()[2])
		}
		ibo = IBO(tempIBO.toIntArray())
		verticesPerShape = 3
	}

	constructor(scene : AIScene, index : Int, dynamic : Boolean = false) : this(AIMesh.create(scene.mMeshes()!![index]), dynamic)

	/**
	 * fully prepares and renders the object, only use this if rendering a single object that looks like this
	 */
	open fun fullRender() {
		prepareRender()
		draw()
		unbind()
	}

	/**
	 * binds the vao and enables all of the vbos, prepares the vao to be drawn
	 */
	open fun prepareRender() {
		glBindVertexArray(id)
		for (i in vbos.indices) {
			glEnableVertexAttribArray(i)
		}
	}

	/**
	 * draws the object, assuming that the VAO represents points
	 */
	open fun drawPoints() {
		glDrawElements(GL_POINTS, ibo.size, GL_UNSIGNED_INT, 0)
	}

	open fun drawPointsInstanced(count: Int) {
		glDrawElementsInstanced(GL_POINTS, ibo.size, GL_UNSIGNED_INT, 0, count)
	}

	/**
	 * draws the object, assuming that the VAO represents lines
	 */
	open fun drawLines() {
		glDrawElements(GL_LINES, ibo.size, GL_UNSIGNED_INT, 0)
	}

	open fun drawLinesInstanced(count: Int) {
		glDrawElementsInstanced(GL_LINES, ibo.size, GL_UNSIGNED_INT, 0, count)
	}

	/**
	 * draws the object, assuming that the VAO represents triangles
	 */
	open fun drawTriangles() {
		glDrawElements(GL_TRIANGLES, ibo.size, GL_UNSIGNED_INT, 0)
	}

	open fun drawTrianglesInstanced(count: Int) {
		glDrawElementsInstanced(GL_TRIANGLES, ibo.size, GL_UNSIGNED_INT, 0, count)
	}

	/**
	 * draws the object, checking how many vertices per shapes the vao holds
	 */
	open fun draw() {
		when (verticesPerShape) {
			1 -> drawPoints()
			2 -> drawLines()
			3 -> drawTriangles()
		}
	}

	open fun drawInstanced(count: Int) {
		when (verticesPerShape) {
			1 -> drawPointsInstanced(count)
			2 -> drawLinesInstanced(count)
			3 -> drawTrianglesInstanced(count)
		}
	}

	/**
	 * disables the vbos then unbinds the vao, cleans up after rendering
	 */
	open fun unbind() {
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
	open fun prepareRender(vboset: IntArray) {
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
	open fun unbind(vboset: IntArray) {
		for (i in vboset) {
			glDisableVertexAttribArray(i)
		}
		glBindVertexArray(0)
	}

	/**
	 * adds a vbo to the vao
	 * @param newVBO new vbo
	 */
	open fun addVBO(newVBO: VBO<*>) {
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
	open fun addVBO(info: FloatArray, size: Int) = addVBO(VBO(info, size))

	/**
	 * returns the number of vertices in the vao
	 * @return number of vertices in the vao
	 */
	open val vertexCount: Int get() = vbos[0].vertexCount

	open operator fun get(i : Int) = vbos[i]

	/**
	 * deletes the vao
	 */
	override fun destroy() {
		glDeleteVertexArrays(id)
	}

	/**
	 * deletes all vbos in the vao
	 */
	open fun deleteVBOS() {
		for (v in vbos) {
			v.destroy()
		}
	}

	companion object {

		operator fun invoke(path : String) = Companion(loadScene(path))
		operator fun invoke(scene : AIScene) = Array(scene.mNumMeshes()) {VAO(scene, it)}
	}
}