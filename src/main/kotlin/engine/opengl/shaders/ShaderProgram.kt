package engine.opengl.shaders

import engine.opengl.GLResource
import engine.opengl.checkGLError
import java.lang.RuntimeException
import org.lwjgl.opengl.GL20.*
import java.util.ArrayList

import engine.opengl.shaders.ShaderType.*
import engine.resourceExists
import org.joml.*

private fun path(path : String) : String? = if (resourceExists(path)) path else null

@Suppress("EXPERIMENTAL_API_USAGE", "unused", "MemberVisibilityCanBePrivate")
class ShaderProgram : GLResource {
	val shaders : Array<Shader> = Array(3) {Shader.defaultShader}
	val uniforms : Array<Array<Uniform>>

	/**
	 * creates a new shader program
	 * @param vertPath path of the vertex shader
	 * @param fragPath path of the fragment shader
	 */
	constructor(vertPath: String?, geomPath : String?, fragPath: String?) : super(glCreateProgram()) {
		shaderProgramIDs.add(id)
		if (vertPath != null) {
			shaders[VERTEX_SHADER.index] = Shader(vertPath, VERTEX_SHADER)
			glAttachShader(id, shaders[VERTEX_SHADER.index].id)
		}
		if (geomPath != null) {
			shaders[GEOMETRY_SHADER.index] = Shader(geomPath, GEOMETRY_SHADER)
			glAttachShader(id, shaders[GEOMETRY_SHADER.index].id)
		}
		if (fragPath != null) {
			shaders[FRAGMENT_SHADER.index] = Shader(fragPath, FRAGMENT_SHADER)
			glAttachShader(id, shaders[FRAGMENT_SHADER.index].id)
		}
		glLinkProgram(id)
		if (glGetProgrami(id, GL_LINK_STATUS) != 1) {
			System.err.println("failed to compile shader")
			checkGLError()
			throw RuntimeException(glGetProgramInfoLog(id))//TODO create new exception for this
		}
		if (vertPath != null) glDetachShader(id, shaders[VERTEX_SHADER.index].id)
		if (geomPath != null) glDetachShader(id, shaders[GEOMETRY_SHADER.index].id)
		if (fragPath != null) glDetachShader(id, shaders[FRAGMENT_SHADER.index].id)
		uniforms = Array(shaders.size) {
			Array(shaders[it].uniformCount) {uniformIndex -> Uniform(shaders[it].uniforms[uniformIndex], id)}
		}
	}

	/**
	 * creates a new shader program from a file name that includes a vertex and fragment shader
	 * @param folderName name of the folder in res/shaders
	 */
	constructor(folderName: String) :
			this(path("res/shaders/$folderName/vert.glsl"),
				path("res/shaders/$folderName/geom.glsl"),
				path("res/shaders/$folderName/frag.glsl"))

	/**
	 * enables the shader program
	 */
	fun enable() {
		glUseProgram(id)
	}

	/**
	 * deletes the shader
	 */
	override fun destroy() {
		glDeleteProgram(id)
		for (i in shaderProgramIDs.indices) {
			if (shaderProgramIDs[i] == id) {
				shaderProgramIDs.removeAt(i)
				return
			}
		}
	}

	operator fun get(type : Int, index : Int) = uniforms[type][index]

	operator fun get(type : ShaderType, index : Int) = this[type.index, index]

	operator fun get(type : Int) = uniforms[type]

	operator fun get(type : ShaderType) = this[type.index]

	operator fun set(type : Int, index : Int, value : Float) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : FloatArray) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Double) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : DoubleArray) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Int) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : IntArray) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : UInt) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : UIntArray) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Boolean) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : BooleanArray) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector2fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector2fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector2dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector2dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector2ic) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector2ic>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector3fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector3fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector3dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector3dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector3ic) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector3ic>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector4fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector4fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector4dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector4dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Vector4ic) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Vector4ic>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix2fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix2fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix3fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix3fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix3x2fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix3x2fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix4fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix4fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix4x3fc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix4x3fc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix2dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix2dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix3dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix3dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix3x2dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix3x2dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix4dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix4dc>) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Matrix4x3dc) = this[type, index].set(value)
	operator fun set(type : Int, index : Int, value : Array<Matrix4x3dc>) = this[type, index].set(value)

	operator fun set(type : ShaderType, index : Int, value : Float) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : FloatArray) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Double) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : DoubleArray) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Int) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : IntArray) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : UInt) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : UIntArray) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Boolean) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : BooleanArray) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector2fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector2fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector2dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector2dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector2ic) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector2ic>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector3fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector3fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector3dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector3dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector3ic) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector3ic>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector4fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector4fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector4dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector4dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Vector4ic) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Vector4ic>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix2fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix2fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix3fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix3fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix3x2fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix3x2fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix4fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix4fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix4x3fc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix4x3fc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix2dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix2dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix3dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix3dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix3x2dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix3x2dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix4dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix4dc>) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Matrix4x3dc) = this[type, index].set(value)
	operator fun set(type : ShaderType, index : Int, value : Array<Matrix4x3dc>) = this[type, index].set(value)

	companion object {
		var shaderProgramIDs = ArrayList<Int>()

		/**
		 * binds the default shader
		 */
		fun disable() {
			glUseProgram(0)
		}
	}
}