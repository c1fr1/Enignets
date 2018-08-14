package engine.OpenGL;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;

public class VBO {
	
	public static ArrayList<Integer> vboIDs = new ArrayList<>();
	
	private int vecSize;
	
	private float[] data;
	
	private int id;
	
	/**
	 * creates a new vbo based on a list of floats
	 * @param array information
	 * @param size size of each vector
	 */
	public VBO(float[] array, int size) {
		vecSize = size;
		id = glGenBuffers();
		vboIDs.add(id);
		setData(array);
	}
	
	/**
	 * get the size of the vector
	 * @return size of the vector
	 */
	public int getVecSize() {
		return vecSize;
	}
	
	/**
	 * get the vbo handle
	 * @return vbo handle
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * get the information
	 * @return info
	 */
	public float[] getData() {
		return data;
	}
	
	/**
	 * sets the values of the vbos
	 * @param ndata new information
	 */
	public void setData(float[] ndata) {
		data = ndata;
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
	}
	
	/**
	 * deletes the vbo
	 */
	public void delete() {
		glDeleteBuffers(id);
		for (int i = 0;i<vboIDs.size();++i) {
			if (vboIDs.get(i) == id) {
				vboIDs.remove(i);
				break;
			}
		}
	}
	
	/**
	 * get the number of vertices in the vbo
	 * @return number of vertices
	 */
	public int getVertexCount() {
		return data.length/vecSize;
	}
	
	/**
	 * square texture coordinate buffer object
	 * @return creates a new vbo with square texture coordinates
	 */
	public static VBO squareTCBO() {
		return new VBO(VAO.squareTC(), 2);
	}
}
