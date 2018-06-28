package engine.OpenGL;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class Attribute {

	private int vecSize;

	private String name;

	private int shaderPosition;
	
	/**
	 * creates a new attribute
	 * @param size vector size of the attribute
	 * @param nam name of the attribute as used in the shader
	 * @param pos position of the attribute in the shader
	 */
	public Attribute(int size, String nam, int pos) {
		vecSize = size;
		name = nam;
		shaderPosition = pos;
	}
	
	/**
	 * returns the size of the vector
	 * @return size of the vector
	 */
	public int getVecSize() {
		return vecSize;
	}
	
	/**
	 * returns the name of the attribute as used in the shader
	 * @return name of the attribute as named in the shader
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * position in the shader
	 * @return position in the shader
	 */
	public int getPos() {
		return  shaderPosition;
	}
	
	/**
	 * enables the vbo at the position of the attribute
	 */
	public void enable() {
		glEnableVertexAttribArray(shaderPosition);
	}
	
	/**
	 * disables the vbo at the position of the attribute
	 */
	public void disable() {
		glDisableVertexAttribArray(shaderPosition);
	}
}
