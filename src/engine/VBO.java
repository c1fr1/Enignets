package engine;

import static org.lwjgl.opengl.GL15.*;

public class VBO {
	
	private int vecSize;
	
	private float[] data;
	
	private int id;
	
	public VBO(float[] array, int size) {
		vecSize = size;
		id = glGenBuffers();
		setData(array);
	}
	
	public int getVecSize() {
		return vecSize;
	}
	
	public int getId() {
		return id;
	}
	
	public float[] getData() {
		return data;
	}

	public void setData(float[] ndata) {
		data = ndata;
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}
	
	public void delete() {
		glDeleteBuffers(id);
	}
	
	public int getVertexCount() {
		return data.length/vecSize;
	}
}
