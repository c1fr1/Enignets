package engine;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Program {

	private int programID;

	public Shader[] shaders = new Shader[3];

	public static Program currentShaderProgram;

	/**
	 * creates a new shader program
	 * @param vertPath
	 * path of the vertex shader
	 * @param fragPath
	 * path of the fragment shader
	 */
	public Program(String vertPath, String fragPath) {
		programID = glCreateProgram();

		shaders[0] = new Shader(vertPath, GL_VERTEX_SHADER);
		shaders[2] = new Shader(fragPath, GL_FRAGMENT_SHADER);

		glAttachShader(programID, shaders[0].getID());
		glAttachShader(programID, shaders[2].getID());

		glLinkProgram(programID);

		if (glGetProgrami(programID, GL_LINK_STATUS) != 1) {
			throw new RuntimeException("Failed to compile shader: ! " + glGetProgramInfoLog(programID));
		}

		glDetachShader(programID, shaders[0].getID());
		glDetachShader(programID, shaders[2].getID());


	}

	/**
	 * creates a new shader program
	 * @param shaderL
	 * list of the shaders, 0 represents the vertex shader, 1 represents the geometry shader, 2 represents the fragment shader
	 * @param exists
	 * marks if the corresponding shader exists or not, so if the vertex and fragment shader are used, but geometry shader is not, then the input should be [true, false, true]
	 */
	public Program(Shader[] shaderL, boolean[] exists) {
		programID = glCreateProgram();

		shaders = shaderL;

		if (exists[0]) {
			glAttachShader(programID, shaders[0].getID());
		}
		if (exists[1]) {
			glAttachShader(programID, shaders[1].getID());
		}
		if (exists[2]) {
			glAttachShader(programID, shaders[2].getID());
		}

		glLinkProgram(programID);

		if (exists[0]) {
			glDetachShader(programID, shaders[0].getID());
		}
		if (exists[1]) {
			glDetachShader(programID, shaders[1].getID());
		}
		if (exists[2]) {
			glDetachShader(programID, shaders[2].getID());
		}
	}


	public int getProgramID() {
		return programID;
	}

	public void enable() {
		currentShaderProgram = this;
		glUseProgram(programID);
	}

	public static void disable() {
		glUseProgram(0);
	}

	public void destroy() {
		glDeleteProgram(programID);
	}
}
