package engine.opengl.shaders

open class ShaderUniform {

	val name: String
	val type : ShaderDataType

	constructor(name : String, type : ShaderDataType) {
		this.name = name
		this.type = type
	}

	constructor(declaration : String) {
		val words : List<String> = if (declaration.startsWith("layout")) {
			declaration.substringAfter(')').trim().removeSuffix(";").split(' ')
		} else {
			declaration.trim().removeSuffix(";").split(' ')
		}
		type = if (words[2].contains('[')) {
			name = words[2].trim().substring(0, words[2].indexOf('['))
			ShaderDataType("${words[1].trim()}${words[2].substring(words[2].indexOf('['))}")
		} else {
			name = words[2].trim()
			ShaderDataType(words[1].trim())
		}
	}
}