package engine.opengl.shaders

import engine.getResource
import engine.opengl.GLResource
import org.lwjgl.opengl.GL20.*
import kotlin.collections.ArrayList

@Suppress("ConvertSecondaryConstructorToPrimary", "unused", "MemberVisibilityCanBePrivate")
class Shader : GLResource {

	val inAttributes: Array<Attribute>
	val outAttributes: Array<Attribute>
	val uniforms: Array<ShaderUniform>

	/**
	 * creates a new shader
	 * @param path path to the shader
	 * @param shaderType type of shader
	 */
	constructor(path : String, shaderType : ShaderType) : super(glCreateShader(shaderType.glID)) {
		var shaderSource = ""
		val reader = getResource(path)
		var line = reader.readLine()
		val tempInAttribList = ArrayList<Attribute>()
		val tempOutAttribList = ArrayList<Attribute>()
		val tempUniformList = ArrayList<ShaderUniform>()

		var lastInAttribPos = -1
		var lastOutAttribPos = -1

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
			} else if (line.startsWith("in") ||
				(line.startsWith("layout") && line.contains(" in "))) {
				val attrib = Attribute(line, lastInAttribPos, lastOutAttribPos)
				lastInAttribPos = attrib.pos
			} else if (line.startsWith("out") ||
				(line.startsWith("layout") && line.contains(" out "))) {
				val attrib = Attribute(line, lastInAttribPos, lastOutAttribPos)
				lastOutAttribPos = attrib.pos
			} else if (line.startsWith("uniform") ||
				(line.startsWith("layout") && line.contains(" uniform "))) {
				tempUniformList.add(ShaderUniform(line))
			}

			shaderSource += "$line\n"
			line = reader.readLine()
		}
		inAttributes = tempInAttribList.toTypedArray()
		outAttributes = tempOutAttribList.toTypedArray()
		uniforms = tempUniformList.toTypedArray()
		glShaderSource(id, shaderSource)
		glCompileShader(id)

		checkCompileStatus(path)
	}

	private fun checkCompileStatus(path : String) {
		if (glGetShaderi(id, GL_COMPILE_STATUS) != 1) {
			throw GLShaderCompileException(path, id)
		}
	}

	private constructor() : super(0) {
		inAttributes = emptyArray()
		outAttributes = emptyArray()
		uniforms = emptyArray()
	}

	val uniformCount : Int get() {return uniforms.size}

	/**
	 * deletes the shader
	 */
	override fun destroy() {
		glDeleteShader(id)
		for (i in shaderIDs.indices) {
			if (shaderIDs[i] == id) {
				shaderIDs.removeAt(i)
			}
		}
	}

	companion object {
		var shaderIDs = ArrayList<Int>()

		val defaultShader = Shader()
	}
}

@Suppress("unused")
class GLShaderCompileException(identifier : String, glID : Int) :
		Exception("Failed to compile shader $identifier!\n${glGetShaderInfoLog(glID)}")