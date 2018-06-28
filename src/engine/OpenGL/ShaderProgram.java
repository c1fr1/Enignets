package engine.OpenGL;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderProgram {
	
	public static ArrayList<Integer> shaderProgramIDs = new ArrayList<>();
	
	private int id;
	
	public Shader[] shaders = new Shader[3];
	
	public static ShaderProgram currentShaderProgram;
	
	/**
	 * creates a new shader program
	 * @param vertPath path of the vertex shader
	 * @param fragPath path of the fragment shader
	 */
	public ShaderProgram(String vertPath, String fragPath) {
		id = glCreateProgram();
		shaderProgramIDs.add(id);
		
		shaders[0] = new Shader(vertPath, GL_VERTEX_SHADER);
		shaders[2] = new Shader(fragPath, GL_FRAGMENT_SHADER);
		
		glAttachShader(id, shaders[0].getID());
		glAttachShader(id, shaders[2].getID());
		
		glLinkProgram(id);
		
		if (glGetProgrami(id, GL_LINK_STATUS) != 1) {
			throw new RuntimeException("Failed to compile shader: ! " + glGetProgramInfoLog(id));
		}
		
		glDetachShader(id, shaders[0].getID());
		glDetachShader(id, shaders[2].getID());
	}
	
	/**
	 * creates a new shader program from a file name that includes a vertex and fragment shader
	 * @param folderName name of the folder in res/shaders
	 */
	public ShaderProgram(String folderName) {
		id = glCreateProgram();
		shaderProgramIDs.add(id);
		
		shaders[0] = new Shader("res/shaders/" + folderName + "/vert", GL_VERTEX_SHADER);
		shaders[2] = new Shader("res/shaders/" + folderName + "/frag", GL_FRAGMENT_SHADER);
		
		glAttachShader(id, shaders[0].getID());
		glAttachShader(id, shaders[2].getID());
		
		glLinkProgram(id);
		
		if (glGetProgrami(id, GL_LINK_STATUS) != 1) {
			throw new RuntimeException("Failed to compile shader: ! " + glGetProgramInfoLog(id));
		}
		
		glDetachShader(id, shaders[0].getID());
		glDetachShader(id, shaders[2].getID());
	}
	
	/**
	 * creates a new shader program
	 * @param shaderL list of the shaders, 0 represents the vertex shader, 1 represents the geometry shader, 2 represents the fragment shader
	 * @param exists marks if the corresponding shader exists or not, so if the vertex and fragment shader are used, but geometry shader is not, then the input should be [true, false, true]
	 */
	public ShaderProgram(Shader[] shaderL, boolean[] exists) {
		id = glCreateProgram();
		shaderProgramIDs.add(id);
		
		shaders = shaderL;
		
		if (exists[0]) {
			glAttachShader(id, shaders[0].getID());
		}
		if (exists[1]) {
			glAttachShader(id, shaders[1].getID());
		}
		if (exists[2]) {
			glAttachShader(id, shaders[2].getID());
		}
		
		glLinkProgram(id);
		
		if (exists[0]) {
			glDetachShader(id, shaders[0].getID());
		}
		if (exists[1]) {
			glDetachShader(id, shaders[1].getID());
		}
		if (exists[2]) {
			glDetachShader(id, shaders[2].getID());
		}
	}
	
	/**
	 * get the shader program handle
	 * @return shader program handle
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * enables the shader program
	 */
	public void enable() {
		currentShaderProgram = this;
		glUseProgram(id);
	}
	
	/**
	 * binds the default shader
	 */
	public static void disable() {
		glUseProgram(0);
	}
	
	/**
	 * deletes the shader
	 */
	public void destroy() {
		glDeleteProgram(id);
		for (int i = 0; i < shaderProgramIDs.size(); ++i) {
			if (shaderProgramIDs.get(i) == id) {
				shaderProgramIDs.remove(i);
			}
		}
	}
}
