package engine.OpenGL;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Uniform {
	
	private String name;
	
	private int vecSize;
	
	private int type;// 0 = float, 1 = int, 2 = matrix;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Creates a new uniform object
	 * @param uname name of the uniform as it is in the shader
	 * @param size size of the uniform (1 is float, 2 is vec2, 3 is vec3...)
	 */
	public Uniform(String uname, int size) {
		if (size > 4 || size < 1) {
			throw new RuntimeException("invalid vector size");
		}
		name = uname;
		vecSize = size;
		type = 0;
	}
	
	/**
	 * Creates a new uniform object that can be a matrix
	 * @param uname name of the uniform as it is in the shader
	 * @param size size of the uniform, if it is a matrix, then the width/height of the matrix
	 * @param dt specifies the data type, 0 is float, 1 is int, 2 is matrix
	 */
	public Uniform(String uname, int size, int dt) {
		if (size > 4 || size < 1) {
			throw new RuntimeException("invalid vector size");
		}
		name = uname;
		vecSize = size;
		type = dt;
	}
	
	/**
	 * gets the name of the uniform as written in the shader
	 * @return name of the uniform as written in the shader
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * sets the uniform for the current shader assuming that the uniform object is a vector, if it is a matrix, this will crash
	 * @param info vector input to be set in the shader
	 */
	public void set(float[] info) {
		if (type == 2) {
			throw new RuntimeException("cannot set a matrix uniform with vector information");
		}else if (type == 1) {
			throw new RuntimeException("cannot set an integer uniform with vector information");
		}
		int pos = GL20.glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		if (vecSize == 1) {
			glUniform1f(pos, info[0]);
		}else if (vecSize == 2) {
			glUniform2f(pos, info[0], info[1]);
		}else if (vecSize == 3) {
			glUniform3f(pos, info[0], info[1], info[2]);
		}else if (vecSize == 4) {
			glUniform4f(pos, info[0], info[1], info[2], info[3]);
		}
	}
	
	/**
	 * sets the uniform for the current shader assuming that the uniform object is a matrix, if it is a vector, this will crash
	 * @param info matrix input to be set in the shader
	 */
	public void set(Matrix4f info) {
		if (type == 0) {
			throw new RuntimeException("cannot set a vector uniform to a matrix");
		}else if (type == 1) {
			throw new RuntimeException("cannot set an integer uniform with matrix information");
		}
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		
		if (vecSize == 4) {
			glUniformMatrix4fv(pos, false, info.get(new float[16]));
		}else {
			throw new RuntimeException("mat" + vecSize + " is not yet supported");
		}
	}
	
	/**
	 * sets the vec4 uniform to the info
	 * @param info input
	 */
	public void set(Vector4f info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform4f(pos, info.x, info.y, info.z, info.w);
	}
	
	/**
	 * sets the vec3 uniform to the info
	 * @param info input
	 */
	public void set(Vector3f info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform3f(pos, info.x, info.y, info.z);
	}
	
	/**
	 * sets the vec2 uniform to the info
	 * @param info input
	 */
	public void set(Vector2f info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform2f(pos, info.x, info.y);
	}
	
	/**
	 * sets the float uniform to the info
	 * @param info input
	 */
	public void set(float info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform1f(pos, info);
	}
	
	/**
	 * sets the int uniform to the info
	 * @param info input
	 */
	public void set(int info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform1i(pos, info);
	}
}
