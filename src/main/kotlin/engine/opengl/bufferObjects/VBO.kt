@file:Suppress("LeakingThis")

package engine.opengl.bufferObjects

import engine.entities.animations.fuckBlender
import engine.opengl.GLResource
import org.joml.*
import org.lwjgl.assimp.AIFace
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AIVector2D
import org.lwjgl.assimp.AIVector3D
import org.lwjgl.opengl.GL20.glGenBuffers
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindBufferBase
import org.lwjgl.opengl.GL30.glVertexAttribIPointer
import org.lwjgl.opengl.GL41.glVertexAttribLPointer
import org.lwjgl.opengl.GL43.*


sealed class VBO<T>(
		data : T,
		protected val vectorSize : Int,
		protected val type : Int,
		protected val usage : Int = GL_STATIC_DRAW
	) : GLResource(glGenBuffers()) {

	open var data : T = data
		set(value) {
			bind()
			storeGLData(value)
			field = value
		}

	protected abstract fun storeGLData(value : T)

	open fun bind() {
		glBindBuffer(GL_ARRAY_BUFFER, id)
	}

	init {
		this.data = data
	}

	val vertexCount : Int get() {return size / vectorSize}

	open fun assignToVAO(index : Int) {
		bind()
		when (type) {
			GL_DOUBLE -> glVertexAttribLPointer(index, vectorSize, GL_DOUBLE, 0, 0)
			GL_INT, GL_SHORT -> glVertexAttribIPointer(index, vectorSize, type, 0, 0)
			else -> glVertexAttribPointer(index, vectorSize, GL_FLOAT, false, 0, 0)
		}
	}

	open val size : Int
		get() {
			return when (data) {
				is DoubleArray -> (data as DoubleArray).size
				is FloatArray -> (data as FloatArray).size
				is IntArray -> (data as IntArray).size
				is ShortArray -> (data as ShortArray).size
				else -> throw InvalidVBOException()
			}
		}

	override fun destroy() {
		glDeleteBuffers(id)
	}

	fun updateData(offset : Long, value : T) {
		bind()
		updateDataRange(offset, value)
	}

	protected abstract fun updateDataRange(offset : Long, value : T)

	companion object {
		val squareTC : FloatArray = floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f)

		operator fun invoke(data : DoubleArray, vectorSize : Int, dynamic : Boolean = false) : VBOd {
			return when(vectorSize) {
				1 -> VBO1d(data, dynamic)
				2 -> VBO2d(data, dynamic)
				3 -> VBO3d(data, dynamic)
				4 -> VBO4d(data, dynamic)
				else -> throw InvalidVBOException()
			}
		}

		operator fun invoke(data : FloatArray, vectorSize : Int, dynamic : Boolean = false) : VBOf {
			return when(vectorSize) {
				1 -> VBO1f(data, dynamic)
				2 -> VBO2f(data, dynamic)
				3 -> VBO3f(data, dynamic)
				4 -> VBO4f(data, dynamic)
				else -> throw InvalidVBOException()
			}
		}

		operator fun invoke(data : AIVector2D.Buffer, vertexCount : Int, dynamic : Boolean = false) =
			VBO2f(data, dynamic)

		operator fun invoke(data : AIVector3D.Buffer, vertexCount : Int, dynamic : Boolean = false,
		                    rotate : Boolean = false) =
			VBO3f(data, dynamic, rotate)

		operator fun invoke(data : IntArray, vectorSize : Int, dynamic : Boolean = false) : VBOi {
			return when(vectorSize) {
				1 -> VBO1i(data, dynamic)
				2 -> VBO2i(data, dynamic)
				3 -> VBO3i(data, dynamic)
				4 -> VBO4i(data, dynamic)
				else -> throw InvalidVBOException()
			}
		}

		operator fun invoke(data : ShortArray, vectorSize : Int, dynamic : Boolean = false) : VBOs {
			return when(vectorSize) {
				1 -> VBO1s(data, dynamic)
				2 -> VBO2s(data, dynamic)
				3 -> VBO3s(data, dynamic)
				4 -> VBO4s(data, dynamic)
				else -> throw InvalidVBOException()
			}
		}
	}
}

interface VertexBuffer<T> {
	fun internalGet(i : Int) : T
	fun internalSet(i : Int, value : T)
}

interface Vertex2DBuffer : VertexBuffer<Double> {
	operator fun get(i : Int) : Vector2d {
		return Vector2d(internalGet(i / 2), internalGet(i / 2 + 1))
	}
	operator fun set(i : Int, value : Vector2dc) {
		internalSet(i / 2, value.x())
		internalSet(i / 2 + 1, value.y())
	}
}
interface Vertex3DBuffer : VertexBuffer<Double> {
	operator fun get(i : Int) : Vector3d {
		return Vector3d(internalGet(i / 3), internalGet(i / 3 + 1), internalGet(i / 3 + 2))
	}
	operator fun set(i : Int, value : Vector3dc) {
		internalSet(i / 3, value.x())
		internalSet(i / 3 + 1, value.y())
		internalSet(i / 3 + 2, value.z())
	}
}
interface Vertex4DBuffer : VertexBuffer<Double> {
	operator fun get(i : Int) : Vector4d {
		return Vector4d(internalGet(i / 4), internalGet(i / 4 + 1), internalGet(i / 4 + 2), internalGet(i / 4 + 3))
	}
	operator fun set(i : Int, value : Vector4dc) {
		internalSet(i / 4, value.x())
		internalSet(i / 4 + 1, value.y())
		internalSet(i / 4 + 2, value.z())
		internalSet(i / 4 + 3, value.w())
	}
}

interface Vertex2FBuffer : VertexBuffer<Float> {
	operator fun get(i : Int) : Vector2f {
		return Vector2f(internalGet(i / 2), internalGet(i / 2 + 1))
	}
	operator fun set(i : Int, value : Vector2fc) {
		internalSet(i / 2, value.x())
		internalSet(i / 2 + 1, value.y())
	}
}
interface Vertex3FBuffer : VertexBuffer<Float> {
	operator fun get(i : Int) : Vector3f {
		return Vector3f(internalGet(i / 3), internalGet(i / 3 + 1), internalGet(i / 3 + 2))
	}
	operator fun set(i : Int, value : Vector3fc) {
		internalSet(i / 3, value.x())
		internalSet(i / 3 + 1, value.y())
		internalSet(i / 3 + 2, value.z())
	}
}
interface Vertex4FBuffer : VertexBuffer<Float> {
	operator fun get(i : Int) : Vector4f {
		return Vector4f(internalGet(i / 4), internalGet(i / 4 + 1), internalGet(i / 4 + 2), internalGet(i / 4 + 3))
	}
	operator fun set(i : Int, value : Vector4fc) {
		internalSet(i / 4, value.x())
		internalSet(i / 4 + 1, value.y())
		internalSet(i / 4 + 2, value.z())
		internalSet(i / 4 + 3, value.w())
	}
}

interface Vertex2IBuffer : VertexBuffer<Int> {
	operator fun get(i : Int) : Vector2i {
		return Vector2i(internalGet(i / 2), internalGet(i / 2 + 1))
	}
	operator fun set(i : Int, value : Vector2ic) {
		internalSet(i / 2, value.x())
		internalSet(i / 2 + 1, value.y())
	}
}
interface Vertex3IBuffer : VertexBuffer<Int> {
	operator fun get(i : Int) : Vector3i {
		return Vector3i(internalGet(i / 3), internalGet(i / 3 + 1), internalGet(i / 3 + 2))
	}
	operator fun set(i : Int, value : Vector3ic) {
		internalSet(i / 3, value.x())
		internalSet(i / 3 + 1, value.y())
		internalSet(i / 3 + 2, value.z())
	}
}
interface Vertex4IBuffer : VertexBuffer<Int> {
	operator fun get(i : Int) : Vector4i {
		return Vector4i(internalGet(i / 4), internalGet(i / 4 + 1), internalGet(i / 4 + 2), internalGet(i / 4 + 3))
	}
	operator fun set(i : Int, value : Vector4ic) {
		internalSet(i / 4, value.x())
		internalSet(i / 4 + 1, value.y())
		internalSet(i / 4 + 2, value.z())
		internalSet(i / 4 + 3, value.w())
	}
}

fun AIVector2D.Buffer.getArray() = FloatArray(limit() * 2) {
	when (it % 2) {
		0 -> get(it / 2).x()
		1 -> get(it / 2).y()
		else -> Float.NaN
	}
}
fun AIVector3D.Buffer.getArray(rotate : Boolean = false) = FloatArray(limit() * 3) {
	when (it % 3) {
		0 -> get(it / 3).x()
		1 -> if (rotate) get(it / 3).z() else get(it / 3).y()
		2 -> if (rotate) -get(it / 3).y() else get(it / 3).z()
		else -> Float.NaN
	}
}

private fun getUsage(dynamic : Boolean = false, readable : Boolean = false) = when {
	readable && dynamic -> GL_DYNAMIC_READ
	readable && !dynamic -> GL_STATIC_READ
	!readable && dynamic -> GL_DYNAMIC_DRAW
	else -> GL_STATIC_DRAW
}

sealed class VBOd(data : DoubleArray, vectorSize : Int, dynamic : Boolean = false) :
		VBO<DoubleArray>(data, vectorSize, GL_DOUBLE, getUsage(dynamic)), VertexBuffer<Double> {
	override fun storeGLData(value : DoubleArray) {
		glBufferData(GL_ARRAY_BUFFER, value, usage)
	}

	override fun updateDataRange(offset : Long, value : DoubleArray) {
		glBufferSubData(GL_ARRAY_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]

	override fun internalSet(i : Int, value : Double) {
		data[i] = value
	}
}
class VBO1d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 1, dynamic)
class VBO2d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 2, dynamic), Vertex2DBuffer
class VBO3d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 3, dynamic), Vertex3DBuffer
class VBO4d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 4, dynamic), Vertex4DBuffer

sealed class VBOf(data : FloatArray, vectorSize : Int, dynamic: Boolean = false) :
		VBO<FloatArray>(data, vectorSize, GL_FLOAT, getUsage(dynamic)), VertexBuffer<Float> {
	override fun storeGLData(value: FloatArray) {
		glBufferData(GL_ARRAY_BUFFER, value, usage)
	}

	override fun updateDataRange(offset: Long, value: FloatArray) {
		glBufferSubData(GL_ARRAY_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]

	override fun internalSet(i : Int, value : Float) {
		data[i] = value
	}
}
class VBO1f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 1, dynamic)
class VBO2f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 2, dynamic), Vertex2FBuffer {
	constructor(data : AIVector2D.Buffer, dynamic : Boolean = false) : this(data.getArray(), dynamic)
}
class VBO3f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 3, dynamic), Vertex3FBuffer {
	constructor(data : AIVector3D.Buffer, dynamic : Boolean = false, rotate : Boolean = false) :
			this(data.getArray(rotate), dynamic)
	}
class VBO4f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 4, dynamic), Vertex4FBuffer

sealed class VBOi(data : IntArray, vectorSize : Int, dynamic: Boolean = false) :
		VBO<IntArray>(data, vectorSize, GL_INT, getUsage(dynamic)), VertexBuffer<Int> {
	override fun storeGLData(value: IntArray) {
		glBufferData(GL_ARRAY_BUFFER, value, usage)
	}

	override fun updateDataRange(offset: Long, value: IntArray) {
		glBufferSubData(GL_ARRAY_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]
	override fun internalSet(i : Int, value : Int) {
		data[i] = value
	}
}
class VBO1i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 1, dynamic)
class VBO2i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 2, dynamic), Vertex2IBuffer
class VBO3i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 3, dynamic), Vertex3IBuffer
class VBO4i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 4, dynamic), Vertex4IBuffer

sealed class VBOs(data : ShortArray, vectorSize : Int, dynamic: Boolean = false) :
		VBO<ShortArray>(data, vectorSize, GL_SHORT, getUsage(dynamic)) {
	override fun storeGLData(value: ShortArray) {
		glBufferData(GL_ARRAY_BUFFER, value, usage)
	}

	override fun updateDataRange(offset: Long, value: ShortArray) {
		glBufferSubData(GL_ARRAY_BUFFER, offset, value)
	}
}
class VBO1s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 1, dynamic)
class VBO2s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 2, dynamic)
class VBO3s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 3, dynamic)
class VBO4s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 4, dynamic)

class IBO(data : IntArray) : VBOi(data, 1, false) {

	constructor(faces : AIFace.Buffer) : this(IntArray(faces.limit() * 3) {faces[it / 3].mIndices()[it % 3]})

	override fun bind() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
	}

	override fun storeGLData(value : IntArray) {
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, value, GL_STATIC_DRAW)
	}
}

sealed class SSBO<T>(value : T, vectorSize : Int, type : Int, dynamic : Boolean = false, readable : Boolean = false) :
		VBO<T>(value, vectorSize, type, getUsage(dynamic, readable)) {

	override var data : T = value

	init {
		bind()
		storeGLData(value)
	}

	abstract fun retrieveGLData() : T

	override fun bind() {
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, id)
	}

	fun bindToPosition(pos : Int) {
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, pos, id)
	}

	companion object {
		operator fun invoke(data : DoubleArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) : SSBOd {
			return when(vectorSize) {
				1 -> SSBO1d(data, dynamic, readable)
				2 -> SSBO2d(data, dynamic, readable)
				3 -> SSBO3d(data, dynamic, readable)
				4 -> SSBO4d(data, dynamic, readable)
				else -> throw InvalidVBOException()
			}
		}
		operator fun invoke(data : FloatArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) : SSBOf {
			return when(vectorSize) {
				1 -> SSBO1f(data, dynamic, readable)
				2 -> SSBO2f(data, dynamic, readable)
				3 -> SSBO3f(data, dynamic, readable)
				4 -> SSBO4f(data, dynamic, readable)
				else -> throw InvalidVBOException()
			}
		}
		operator fun invoke(value : AIVector2D.Buffer, dynamic : Boolean = false, readable : Boolean = false) = SSBO2f(value, dynamic, readable)
		operator fun invoke(value : AIVector3D.Buffer, dynamic : Boolean = false, readable : Boolean = false, rotate : Boolean = fuckBlender) = SSBO3f(value, dynamic, readable, rotate)

		operator fun invoke(data : IntArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) : SSBOi {
			return when(vectorSize) {
				1 -> SSBO1i(data, dynamic, readable)
				2 -> SSBO2i(data, dynamic, readable)
				3 -> SSBO3i(data, dynamic, readable)
				4 -> SSBO4i(data, dynamic, readable)
				else -> throw InvalidVBOException()
			}
		}
		operator fun invoke(data : ShortArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) : SSBOs {
			return when(vectorSize) {
				1 -> SSBO1s(data, dynamic, readable)
				2 -> SSBO2s(data, dynamic, readable)
				3 -> SSBO3s(data, dynamic, readable)
				4 -> SSBO4s(data, dynamic, readable)
				else -> throw InvalidVBOException()
			}
		}

		fun syncSSBOs() {
			glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT)
		}
	}
}

sealed class SSBOd(value : DoubleArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) :
		SSBO<DoubleArray>(value, vectorSize, GL_DOUBLE, dynamic, readable), VertexBuffer<Double> {

	override fun retrieveGLData() : DoubleArray {
		bind()
		glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)!!.asDoubleBuffer().get(data)
		glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
		return data
	}

	override fun storeGLData(value : DoubleArray) {
		glBufferData(GL_SHADER_STORAGE_BUFFER, value, usage)
	}

	override fun updateDataRange(offset : Long, value : DoubleArray) {
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]
	override fun internalSet(i : Int, value : Double) {
		data[i] = value
	}
}
sealed class SSBOf(value : FloatArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) :
		SSBO<FloatArray>(value, vectorSize, GL_FLOAT, dynamic, readable), VertexBuffer<Float> {

	override fun retrieveGLData() : FloatArray {
		bind()
		glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)!!.asFloatBuffer().get(data)
		glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
		return data
	}

	override fun storeGLData(value : FloatArray) {
		glBufferData(GL_SHADER_STORAGE_BUFFER, value, usage)
	}

	override fun updateDataRange(offset : Long, value : FloatArray) {
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]
	override fun internalSet(i : Int, value : Float) {
		data[i] = value
	}
}
sealed class SSBOi(value : IntArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) :
		SSBO<IntArray>(value, vectorSize, GL_INT, dynamic, readable), VertexBuffer<Int> {

	override fun retrieveGLData() : IntArray {
		bind()
		glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)!!.asIntBuffer().get(data)
		glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
		return data
	}

	override fun storeGLData(value : IntArray) {
		glBufferData(GL_SHADER_STORAGE_BUFFER, value, usage)
	}

	override fun updateDataRange(offset : Long, value : IntArray) {
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]
	override fun internalSet(i : Int, value : Int) {
		data[i] = value
	}
}
sealed class SSBOs(value : ShortArray, vectorSize : Int, dynamic : Boolean = false, readable : Boolean = false) :
		SSBO<ShortArray>(value, vectorSize, GL_SHORT, dynamic, readable), VertexBuffer<Short> {

	override fun retrieveGLData() : ShortArray {
		bind()
		glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)!!.asShortBuffer().get(data)
		glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
		return data
	}

	override fun storeGLData(value : ShortArray) {
		glBufferData(GL_SHADER_STORAGE_BUFFER, value, usage)
	}

	override fun updateDataRange(offset : Long, value : ShortArray) {
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, offset, value)
	}

	override fun internalGet(i : Int) = data[i]
	override fun internalSet(i : Int, value : Short) {
		data[i] = value
	}
}

class SSBO1d(data : DoubleArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOd(data, 1, dynamic, readable)
class SSBO2d(data : DoubleArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOd(data, 2, dynamic, readable), Vertex2DBuffer
class SSBO3d(data : DoubleArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOd(data, 3, dynamic, readable), Vertex3DBuffer
class SSBO4d(data : DoubleArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOd(data, 4, dynamic, readable), Vertex4DBuffer

class SSBO1f(data : FloatArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOf(data, 1, dynamic, readable)
class SSBO2f(data : FloatArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOf(data, 2, dynamic, readable), Vertex2FBuffer {
	constructor(value : AIVector2D.Buffer, dynamic : Boolean = false, readable : Boolean = false) : this(value.getArray(), dynamic, readable)
}
class SSBO3f(data : FloatArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOf(data, 3, dynamic, readable), Vertex3FBuffer {
	constructor(value : AIVector3D.Buffer, dynamic : Boolean = false, readable : Boolean = false, rotate : Boolean = false) : this(value.getArray(rotate), dynamic, readable)
}
class SSBO4f(data : FloatArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOf(data, 4, dynamic, readable), Vertex4FBuffer

class SSBO1i(data : IntArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOi(data, 1, dynamic, readable)
class SSBO2i(data : IntArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOi(data, 2, dynamic, readable), Vertex2IBuffer
class SSBO3i(data : IntArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOi(data, 3, dynamic, readable), Vertex3IBuffer
class SSBO4i(data : IntArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOi(data, 4, dynamic, readable), Vertex4IBuffer

class SSBO1s(data : ShortArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOs(data, 1, dynamic, readable)
class SSBO2s(data : ShortArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOs(data, 2, dynamic, readable)
class SSBO3s(data : ShortArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOs(data, 3, dynamic, readable)
class SSBO4s(data : ShortArray, dynamic : Boolean = false, readable : Boolean = false) : SSBOs(data, 4, dynamic, readable)

class InvalidVBOException : RuntimeException("invalid VBO storage class")