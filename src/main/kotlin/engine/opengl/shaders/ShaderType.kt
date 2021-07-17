package engine.opengl.shaders

import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER

enum class ShaderType(val index : Int, val glID : Int) {
	VERTEX_SHADER(0, GL_VERTEX_SHADER),
	GEOMETRY_SHADER(1, GL_GEOMETRY_SHADER),
	FRAGMENT_SHADER(2, GL_FRAGMENT_SHADER)
}