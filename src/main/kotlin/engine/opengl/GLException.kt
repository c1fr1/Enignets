package engine.opengl

import org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION

class GLInvalidEnumException : GLException("Invalid Enum", GL_INVALID_ENUM)
class GLInvalidValueException : GLException("Invalid Value", GL_INVALID_ENUM)
class GLInvalidOperationException : GLException("Invalid Operation", GL_INVALID_ENUM)
class GLInvalidFramebufferOperationException : GLException("Invalid Framebuffer Operation", GL_INVALID_ENUM)
class GLStackUnderflowException : GLException("Stack Underflow", GL_INVALID_ENUM)
class GLStackOverflowException : GLException("Stack Overflow", GL_INVALID_ENUM)
class GLOutOfMemoryException : GLException("Out of Memory", GL_INVALID_ENUM)
class GLTableTooLargeException : GLException("Table Too Large", GL_INVALID_ENUM)
class GLIRLYDIKException(id : Int) : GLException("I rly dik", id)
sealed class GLException(name : String, val id : Int) : Exception("GL Exception $name (id: $id) has occurred") {
	companion object {
		operator fun invoke(id : Int) : GLException {
			return when (id) {
				GL_INVALID_ENUM -> GLInvalidEnumException()
				GL_INVALID_VALUE -> GLInvalidValueException()
				GL_INVALID_OPERATION -> GLInvalidOperationException()
				GL_INVALID_FRAMEBUFFER_OPERATION -> GLInvalidFramebufferOperationException()
				GL_STACK_UNDERFLOW -> GLStackUnderflowException()
				GL_STACK_OVERFLOW -> GLStackOverflowException()
				GL_OUT_OF_MEMORY -> GLOutOfMemoryException()
				GL_TABLE_TOO_LARGE -> GLTableTooLargeException()
				else -> GLIRLYDIKException(id)
			}
		}
	}
}

fun checkGLError() {
	var error: Int
	while (glGetError().also { error = it } != GL_NO_ERROR) {
		throw GLException(error)
	}
}