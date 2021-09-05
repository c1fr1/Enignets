package engine.opengl.shaders

import engine.getResource
import engine.opengl.GLResource
import engine.opengl.checkGLError
import org.joml.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER
import org.lwjgl.opengl.GL43.glDispatchCompute
import java.lang.RuntimeException

open class ComputeProgram : GLResource {

	open val uniforms : Array<Uniform>

	open val shaderID = glCreateShader(GL_COMPUTE_SHADER)

	open val workGroupSize : Vector3i

	constructor(path : String) : super(glCreateProgram()) {
		var shaderSource = ""
		val reader = getResource(path)
		var line = reader.readLine()
		val tempUniformList = ArrayList<ShaderUniform>()
		val constArray = ArrayList<Pair<String, Int>>()

		workGroupSize = Vector3i(1, 1, 1)

		shaderSource += "#version 430 core\nlayout (std430) buffer;\n"

		while (line != null) {
			if (line.startsWith("#")) {
				if (line == "#import <gnoise>") {
					val br = getResource("engine/noise.glsl")
					line = br.readText()
				}
				if (line.startsWith("#import \"")) {
					val br = getResource(line.substring(9, line.length - 1))
					line = br.readText()
				}
			} else if (line.contains("uniform ")) {
				for (const in constArray) {
					if (line.contains(const.first)) {
						line = line.replace(const.first, "${const.second}")
					}
				}
				tempUniformList.add(ShaderUniform(line.substring(line.indexOf("uniform"))))
			} else if (line.startsWith("layout") && line.contains("local_size")) {
				val woSpaces = line.replace(" ", "")
				val inBrackets = woSpaces.substringAfter('(').substringBefore(')')
				val properties = inBrackets.split(',')
				for (property in properties) {
					when (property.substringBefore('=')) {
						"local_size_x" -> workGroupSize.x = property.substringAfter('=').toInt()
						"local_size_y" -> workGroupSize.y = property.substringAfter('=').toInt()
						"local_size_z" -> workGroupSize.z = property.substringAfter('=').toInt()
					}
				}
			} else if (line.startsWith("const int")) {
				val name = line.substringBefore('=').split(' ')[2]
				val value = line.substringAfter('=').substringBefore(';').trim().toInt()
				constArray.add(Pair(name, value))
			}
			shaderSource += "$line\n"
			line = reader.readLine()
		}

		glShaderSource(shaderID, shaderSource)
		glCompileShader(shaderID)

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) != 1) {
			throw GLShaderCompileException(path, shaderID)
		}

		glAttachShader(id, shaderID)

		glLinkProgram(id)
		if (glGetProgrami(id, GL_LINK_STATUS) != 1) {
			System.err.println("failed to compile shader")
			checkGLError()
			throw RuntimeException(glGetProgramInfoLog(id))//TODO create new exception for this
		}

		glDetachShader(id, shaderID)

		uniforms = Array(tempUniformList.size) {Uniform(tempUniformList[it], id)}
	}

	open fun enable() {
		glUseProgram(id)
	}

	open fun run(tasksX : Int, tasksY : Int = 1, tasksZ : Int = 1) {
		glDispatchCompute(tasksX / workGroupSize.x, tasksY / workGroupSize.y, tasksZ / workGroupSize.z)
	}

	open fun run(tasks : Vector3ic) = run(tasks.x(), tasks.y(), tasks.z())

	override fun destroy() {
		glDeleteProgram(id)
		glDeleteShader(shaderID)
	}

	open operator fun get(index : Int) = uniforms[index]

	open operator fun set(index : Int, value : Float) = this[index].set(value)
	open operator fun set(index : Int, value : FloatArray) = this[index].set(value)
	open operator fun set(index : Int, value : Double) = this[index].set(value)
	open operator fun set(index : Int, value : DoubleArray) = this[index].set(value)
	open operator fun set(index : Int, value : Int) = this[index].set(value)
	open operator fun set(index : Int, value : IntArray) = this[index].set(value)
	open operator fun set(index : Int, value : UInt) = this[index].set(value)
	open operator fun set(index : Int, value : UIntArray) = this[index].set(value)
	open operator fun set(index : Int, value : Boolean) = this[index].set(value)
	open operator fun set(index : Int, value : BooleanArray) = this[index].set(value)
	open operator fun set(index : Int, value : Vector2fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector2f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector2fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector2dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector2d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector2dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector2ic) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector2i>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector2ic>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector3fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector3f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector3fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector3dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector3d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector3dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector3ic) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector3i>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector3ic>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector4fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector4f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector4fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector4dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector4d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector4dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Vector4ic) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector4i>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Vector4ic>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix2fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix2f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix2fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix3fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix3x2fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3x2f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3x2fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix4fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix4x3fc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4x3f>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4x3fc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix2dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix2d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix2dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix3dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix3x2dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3x2d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix3x2dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix4dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4dc>) = this[index].set(value)
	open operator fun set(index : Int, value : Matrix4x3dc) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4x3d>) = this[index].set(value)
	open operator fun set(index : Int, value : Array<Matrix4x3dc>) = this[index].set(value)
}