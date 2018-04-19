package engine;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Uniform {
	
	private String name;
	
	private int vecSize;
	
	private boolean isMatrix = false;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	/**
	 * Creates a new uniform object
	 * @param uname
	 * name of the uniform as it is in the shader
	 * @param size
	 * size of the uniform (1 is float, 2 is vec2, 3 is vec3...)
	 */
	public Uniform(String uname, int size) {
		if (size > 4 || size < 1) {
			throw new RuntimeException("invalid vector size");
		}
		name = uname;
		vecSize = size;
	}

	/**
	 * Creates a new uniform object that can be a matrix
	 * @param uname
	 * name of the uniform as it is in the shader
	 * @param size
	 * size of the uniform, if it is a matrix, then the width/height of the matrix
	 * @param ism
	 * if (ism) then this object is a matrix
	 */
	public Uniform(String uname, int size, boolean ism) {
		if (size > 4 || size < 1) {
			throw new RuntimeException("invalid vector size");
		}
		name = uname;
		vecSize = size;
		isMatrix = ism;
	}

	public String getName() {
		return name;
	}

	/**
	 * sets the uniform for the current shader assuming that the uniform object is a vector, if it is a matrix, this will crash
	 * @param info
	 * vector input to be set in the shader
	 */
	public void set(float[] info) {
		if (isMatrix) {
			throw new RuntimeException("cannot set a matrix uniform with vector information");
		}
		int pos = glGetUniformLocation(Program.currentShaderProgram.getProgramID(), name);
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
	 * @param info
	 * matrix input to be set in the shader
	 */
	public void set(Matrix4f info) {
		if (!isMatrix) {
			throw new RuntimeException("cannot set a vector uniform to a matrix");
		}
		int pos = glGetUniformLocation(Program.currentShaderProgram.getProgramID(), name);

		if (vecSize == 4) {
			glUniformMatrix4fv(pos, false, info.get(new float[16]));
		}else {
			throw new RuntimeException("mat" + vecSize + " is not yet supported");
		}
	}
}
