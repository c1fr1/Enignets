@file:Suppress("LeakingThis")

package engine.opengl.bufferObjects

import engine.opengl.GLResource
import engine.opengl.checkGLError
import org.joml.Vector2fc
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glGenBuffers
import org.lwjgl.opengl.GL20.glVertexAttribPointer

sealed class VBO<T>(
		data : T,
		private val vectorSize : Int,
		private val type : Int,
		private val dynamic : Boolean = false
	) : GLResource(glGenBuffers()) {

	open var data : T = data
		set(value) {
			glBindBuffer(GL_ARRAY_BUFFER, id)
			when (value) {
				is DoubleArray -> glBufferData(GL_ARRAY_BUFFER, value, if (dynamic) GL_DYNAMIC_DRAW else GL_STATIC_DRAW)
				is FloatArray -> glBufferData(GL_ARRAY_BUFFER, value, if (dynamic) GL_DYNAMIC_DRAW else GL_STATIC_DRAW)
				is IntArray -> glBufferData(GL_ARRAY_BUFFER, value, if (dynamic) GL_DYNAMIC_DRAW else GL_STATIC_DRAW)
				is ShortArray -> glBufferData(GL_ARRAY_BUFFER, value, if (dynamic) GL_DYNAMIC_DRAW else GL_STATIC_DRAW)
				else -> throw InvalidVBOException()
			}
			field = value
		}

	init {
		this.data = data
	}

	val vertexCount : Int get() {return size / vectorSize}

	fun assignToVAO(index: Int) {
		glBindBuffer(GL_ARRAY_BUFFER, id)
		glVertexAttribPointer(index, vectorSize, type, false, 0, 0)
	}

	val size : Int
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
		when (value) {
			is DoubleArray -> {
				glBufferSubData(GL_ARRAY_BUFFER, offset, value)
				for (i in value.indices) (data as DoubleArray)[i + offset.toInt()] = value[i]
			}
			is FloatArray -> {
				glBufferSubData(GL_ARRAY_BUFFER, offset, value)
				for (i in value.indices) (data as FloatArray)[i + offset.toInt()] = value[i]
			}
			is IntArray -> {
				glBufferSubData(GL_ARRAY_BUFFER, offset, value)
				for (i in value.indices) (data as IntArray)[i + offset.toInt()] = value[i]
			}
			is ShortArray -> {
				glBufferSubData(GL_ARRAY_BUFFER, offset, value)
				for (i in value.indices) (data as ShortArray)[i + offset.toInt()] = value[i]
			}
			else -> throw InvalidVBOException()
		}
	}

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

sealed class VBOd(data : DoubleArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<DoubleArray>(data, vectorSize, GL_DOUBLE, dynamic)
class VBO1d(data : DoubleArray, dynamic: Boolean = false) : VBOd(data, 1, dynamic)
class VBO2d(data : DoubleArray, dynamic: Boolean = false) : VBOd(data, 2, dynamic)
class VBO3d(data : DoubleArray, dynamic: Boolean = false) : VBOd(data, 3, dynamic)
class VBO4d(data : DoubleArray, dynamic: Boolean = false) : VBOd(data, 4, dynamic)

sealed class VBOf(data : FloatArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<FloatArray>(data, vectorSize, GL_FLOAT, dynamic)
class VBO1f(data : FloatArray, dynamic: Boolean = false) : VBOf(data, 1, dynamic)
class VBO2f(data : FloatArray, dynamic: Boolean = false) : VBOf(data, 2, dynamic)
class VBO3f(data : FloatArray, dynamic: Boolean = false) : VBOf(data, 3, dynamic)
class VBO4f(data : FloatArray, dynamic: Boolean = false) : VBOf(data, 4, dynamic)

sealed class VBOi(data : IntArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<IntArray>(data, vectorSize, GL_INT, dynamic)
class VBO1i(data : IntArray, dynamic: Boolean = false) : VBOi(data, 1, dynamic)
class VBO2i(data : IntArray, dynamic: Boolean = false) : VBOi(data, 2, dynamic)
class VBO3i(data : IntArray, dynamic: Boolean = false) : VBOi(data, 3, dynamic)
class VBO4i(data : IntArray, dynamic: Boolean = false) : VBOi(data, 4, dynamic)

sealed class VBOs(data : ShortArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<ShortArray>(data, vectorSize, GL_SHORT, dynamic)
class VBO1s(data : ShortArray, dynamic: Boolean = false) : VBOs(data, 1, dynamic)
class VBO2s(data : ShortArray, dynamic: Boolean = false) : VBOs(data, 2, dynamic)
class VBO3s(data : ShortArray, dynamic: Boolean = false) : VBOs(data, 3, dynamic)
class VBO4s(data : ShortArray, dynamic: Boolean = false) : VBOs(data, 4, dynamic)

class IBO(data : IntArray) : VBOi(data, 1, false) {

	override var data: IntArray
		get() = super.data
		set(value) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, value, GL_STATIC_DRAW)
		}
}

class InvalidVBOException : RuntimeException("invalid VBO storage class")