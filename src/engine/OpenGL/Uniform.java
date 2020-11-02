package engine.OpenGL;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

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
	 * sets the value of a float array in a shader.
	 * @param info array to be set in the shader
	 */
	public void set(float[] info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform1fv(pos, info);
	}

	/**
	 * sets the value of a int array in a shader.
	 * @param info array to be set in the shader
	 */
	public void set(int[] info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform1iv(pos, info);
	}

	public void set(Vector2f[] info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		float[] value = new float[info.length * 2];
		for (int i = 0; i < info.length; ++i) {
			value[2 * i] = info[i].x;
			value[2 * i + 1] = info[i].y;
		}
		glUniform2fv(pos, value);
	}

	public void set(Vector3f[] info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		float[] value = new float[info.length * 3];
		for (int i = 0; i < info.length; ++i) {
			value[3 * i] = info[i].x;
			value[3 * i + 1] = info[i].y;
			value[3 * i + 2] = info[i].z;
		}
		glUniform3fv(pos, value);
	}

	public void set(Vector4f[] info) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		float[] value = new float[info.length * 4];
		for (int i = 0; i < info.length; ++i) {
			value[4 * i] = info[i].x;
			value[4 * i + 1] = info[i].y;
			value[4 * i + 2] = info[i].z;
			value[4 * i + 3] = info[i].z;
		}
		glUniform4fv(pos, value);
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

	public void set(float infoa, float infob) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform2f(pos, infoa, infob);
	}

	public void set(float infoa, float infob, float infoc) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform3f(pos, infoa, infob, infoc);
	}

	public void set(float infoa, float infob, float infoc, float infod) {
		int pos = glGetUniformLocation(ShaderProgram.currentShaderProgram.getID(), name);
		glUniform4f(pos, infoa, infob, infoc, infod);
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
