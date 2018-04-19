package engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class VAO {

	private int vao;
	private int ibo;

	private int[] indices;

	public VBO[] vbos;

	private int verticesPerShape = 3;

	/**
	 * create a new VAO with vertices, and indices
	 * @param vertices
	 * vertices of the object (3 dimensional)
	 * @param ind
	 * indices of the faces (3 per face)
	 */
	public VAO(float[] vertices, int[] ind) {
		vbos = new VBO[1];
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbos[0] = new VBO(vertices, 3);
		ibo = glGenBuffers();
		indices = ind;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		indices = ind;
	}

	/**
	 * create a new VAO with vertices, and indices specifying the number of vertices per shape
	 * @param vertices
	 * vertices fo the obejct (3 dimensional)
	 * @param ind
	 * indices of the faces (vps per face)
	 * @param vps
	 * number of vertices associated with each individual shape
	 */
	public VAO(float[] vertices, int[] ind, int vps) {
		vbos = new VBO[1];
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbos[0] = new VBO(vertices, 3);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		ibo = glGenBuffers();
		indices = ind;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		verticesPerShape = vps;
	}

	/**
	 * create a square object
	 * @param x
	 * corner x
	 * @param y
	 * corner y
	 * @param width
	 * width of the box
	 * @param height
	 * height of the box
	 */
	public VAO(float x, float y, float width, float height) {
		vbos = new VBO[1];
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbos[0] = new VBO(new float[] {
				x, y + height, 0f,
				x, y, 0f,
				x + width, y, 0f,
				x + width, y + height//, 0f
		}, 3);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		ibo = glGenBuffers();
		indices = squareIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		verticesPerShape = 3;
	}

	/**
	 * renders the object
	 */
	public void render() {
		glBindVertexArray(vao);
		for(int i = 0; i < vbos.length; ++i) {
			glEnableVertexAttribArray(i);
		}
		if (verticesPerShape == 1) {
			glDrawElements(GL_POINTS, indices.length, GL_UNSIGNED_INT, 0);
		}else if (verticesPerShape == 2) {
			glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
		}else if (verticesPerShape == 3) {
			glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
		}
		for(int i = 0; i < vbos.length; ++i) {
			glDisableVertexAttribArray(i);
		}
	}

	/**
	 * adds a vbo to the vao
	 * @param newVBO
	 * new vbo
	 */
	public void addVBO(VBO newVBO) {
		VBO[] tempVBOL = new VBO[vbos.length + 1];
		for (int i = 0;i<vbos.length;i++) {
			tempVBOL[i] = vbos[i];
		}
		tempVBOL[vbos.length] = newVBO;
		glBindVertexArray(vao);
		glVertexAttribPointer(vbos.length, newVBO.getVecSize(), GL_FLOAT, false, 0, 0);
		vbos = tempVBOL;
	}

	public int getVerticesPerShape() {
		return verticesPerShape;
	}
	
	public int getVertxCount() {
		return vbos[0].getVertexCount();
	}

	public void delete() {
		glDeleteVertexArrays(vao);
	}
	
	public void deleteVBOS() {
		for (VBO v:vbos) {
			glDeleteBuffers(v.getId());
		}
	}
	
	public static int[] squareIndices() {
		return new int[] {
				0, 1, 3,
				3, 1, 2
		};
	}
	
	public static float[] squareTC() {
		return new float[] {0f, 0f,
				0f, 1f,
				1f, 1f,
				1f, 0f
		};
	}
}