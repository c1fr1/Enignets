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
		type = ShaderDataType(words[1].trim())
		name = words[2].trim()
	}
}