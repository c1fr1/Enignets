@file:Suppress("LeakingThis")

package engine.opengl.bufferObjects

import engine.opengl.GLResource
import org.joml.*
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
class VBO1d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 1, dynamic)
class VBO2d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 2, dynamic) {
	operator fun get(index : Int) : Vector2d = Vector2d(data[index / 2], data[index / 2 + 1])
	operator fun set(index : Int, value : Vector2dc) {
		data[index / 2] = value.x()
		data[index / 2 + 1] = value.y()
	}
}
class VBO3d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 3, dynamic) {
	operator fun get(index: Int) : Vector3d = Vector3d(data[index / 3], data[index / 3 + 1], data[index / 3 + 2])
	operator fun set(index : Int, value : Vector3dc) {
		data[index / 3] = value.x()
		data[index / 3 + 1] = value.y()
		data[index / 3 + 2] = value.z()
	}
}
class VBO4d(data : DoubleArray, dynamic : Boolean = false) : VBOd(data, 4, dynamic) {
	operator fun get(index: Int) : Vector4d =
		Vector4d(data[index / 4], data[index / 4 + 1], data[index / 4 + 2], data[index / 4 + 3])
	operator fun set(index : Int, value : Vector4dc) {
		data[index / 4] = value.x()
		data[index / 4 + 1] = value.y()
		data[index / 4 + 2] = value.z()
		data[index / 4 + 3] = value.w()
	}
}

sealed class VBOf(data : FloatArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<FloatArray>(data, vectorSize, GL_FLOAT, dynamic)
class VBO1f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 1, dynamic)
class VBO2f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 2, dynamic) {
	operator fun get(index : Int) : Vector2f = Vector2f(data[index / 2], data[index / 2 + 1])
	operator fun set(index : Int, value : Vector2fc) {
		data[index / 2] = value.x()
		data[index / 2 + 1] = value.y()
	}
}
class VBO3f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 3, dynamic) {
	operator fun get(index: Int) : Vector3f = Vector3f(data[index / 3], data[index / 3 + 1], data[index / 3 + 2])
	operator fun set(index : Int, value : Vector3fc) {
		data[index / 3] = value.x()
		data[index / 3 + 1] = value.y()
		data[index / 3 + 2] = value.z()
	}
}
class VBO4f(data : FloatArray, dynamic : Boolean = false) : VBOf(data, 4, dynamic) {
	operator fun get(index: Int) : Vector4f =
		Vector4f(data[index / 4], data[index / 4 + 1], data[index / 4 + 2], data[index / 4 + 3])
	operator fun set(index : Int, value : Vector4fc) {
		data[index / 4] = value.x()
		data[index / 4 + 1] = value.y()
		data[index / 4 + 2] = value.z()
		data[index / 4 + 3] = value.w()
	}
}

sealed class VBOi(data : IntArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<IntArray>(data, vectorSize, GL_INT, dynamic)
class VBO1i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 1, dynamic)
class VBO2i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 2, dynamic) {
	operator fun get(index : Int) : Vector2i = Vector2i(data[index / 2], data[index / 2 + 1])
	operator fun set(index : Int, value : Vector2ic) {
		data[index / 2] = value.x()
		data[index / 2 + 1] = value.y()
	}
}
class VBO3i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 3, dynamic) {
	operator fun get(index: Int) : Vector3i = Vector3i(data[index / 3], data[index / 3 + 1], data[index / 3 + 2])
	operator fun set(index : Int, value : Vector3ic) {
		data[index / 3] = value.x()
		data[index / 3 + 1] = value.y()
		data[index / 3 + 2] = value.z()
	}
}
class VBO4i(data : IntArray, dynamic : Boolean = false) : VBOi(data, 4, dynamic) {
	operator fun get(index: Int) : Vector4i =
		Vector4i(data[index / 4], data[index / 4 + 1], data[index / 4 + 2], data[index / 4 + 3])
	operator fun set(index : Int, value : Vector4ic) {
		data[index / 4] = value.x()
		data[index / 4 + 1] = value.y()
		data[index / 4 + 2] = value.z()
		data[index / 4 + 3] = value.w()
	}
}

sealed class VBOs(data : ShortArray, vectorSize : Int, dynamic: Boolean = false) :
	VBO<ShortArray>(data, vectorSize, GL_SHORT, dynamic)
class VBO1s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 1, dynamic)
class VBO2s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 2, dynamic)
class VBO3s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 3, dynamic)
class VBO4s(data : ShortArray, dynamic : Boolean = false) : VBOs(data, 4, dynamic)

class IBO(data : IntArray) : VBOi(data, 1, false) {

	override var data : IntArray
		get() = super.data
		set(value) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, value, GL_STATIC_DRAW)
		}
}

class InvalidVBOException : RuntimeException("invalid VBO storage class")