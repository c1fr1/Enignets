package engine;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class Attribute {

	private int vecSize;

	private String name;

	private int shaderPosition;

	public Attribute(int size, String nam, int pos) {
		vecSize = size;
		name = nam;
		shaderPosition = pos;
	}

	public int getVecSize() {
		return vecSize;
	}

	public String getName() {
		return name;
	}

	public int getPos() {
		return  shaderPosition;
	}

	public void enable() {
		glEnableVertexAttribArray(shaderPosition);
	}

	public void disable() {
		glDisableVertexAttribArray(shaderPosition);
	}
}
