package engine.opengl.shaders

import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Attribute {

	val type : ShaderDataType
	val name: String
	val pos: Int

	constructor(type : ShaderDataType, name : String, pos : Int) {
		this.type = type
		this.name = name
		this.pos = pos
	}

	constructor(declaration : String, lastInAttribPos : Int, lastOutAttribPos : Int) {
		val words : List<String>
		var specifiedPos : Int? = null
		if (declaration.startsWith("layout")) {
			val layoutParams = declaration
				.substringAfter('(')
				.substringBefore(')')
				.split(',')
			specifiedPos = layoutParams.find { it.trim().startsWith("location") }
				?.substringAfter('=')?.toIntOrNull()
			words = declaration.substringAfter(')').trim()
				.removeSuffix(";").split(' ')
		} else {
			words = declaration.trim().removeSuffix(";").split(' ')
		}
		pos = specifiedPos ?: when (words[0]) {
			"in" -> lastInAttribPos + 1
			"out" -> lastOutAttribPos + 1
			else -> -1
		}

		type = ShaderDataType(words[1].trim())
		name = words[2].trim()
	}

	/**
	 * enables the vbo at the position of the attribute
	 */
	fun enable() {
		glEnableVertexAttribArray(pos)
	}

	/**
	 * disables the vbo at the position of the attribute
	 */
	fun disable() {
		glDisableVertexAttribArray(pos)
	}
}