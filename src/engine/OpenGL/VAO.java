package engine.OpenGL;

import engine.Entities.PositionInfo;
import engine.Platform.Box3d;
import org.joml.Matrix4f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class VAO {
	
	public static ArrayList<Integer> vaoIDs = new ArrayList<>();
	
	private int id;
	private int ibo;
	
	private int[] indices;
	
	public VBO[] vbos;
	
	private int verticesPerShape = 3;
	
	public Box3d boundingBox;
	
	/**
	 * create a new VAO with vertices, and indices
	 * @param vertices vertices of the object (3 dimensional)
	 * @param ind indices of the faces (3 per face)
	 */
	public VAO(float[] vertices, int[] ind) {
		vbos = new VBO[1];
		id = glGenVertexArrays();
		vaoIDs.add(id);
		glBindVertexArray(id);
		vbos[0] = new VBO(vertices, 3);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		ibo = glGenBuffers();
		VBO.vboIDs.add(ibo);
		indices = ind;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		verticesPerShape = 3;
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY;
		float maxZ = Float.NEGATIVE_INFINITY;
		for (int vertStart = 0; vertStart < vertices.length;vertStart += 3) {
			if (vertices[vertStart] < minX) {
				minX = vertices[vertStart];
			}
			if (vertices[vertStart] > maxX) {
				maxX = vertices[vertStart];
			}
			if (vertices[vertStart + 1] < minY) {
				minY = vertices[vertStart + 1];
			}
			if (vertices[vertStart + 1] > maxY) {
				maxY = vertices[vertStart];
			}
			if (vertices[vertStart + 2] < minZ) {
				minZ = vertices[vertStart];
			}
			if (vertices[vertStart + 2] > maxZ) {
				maxZ = vertices[vertStart];
			}
		}
		boundingBox = new Box3d(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * create a new VAO with vertices, and indices specifying the number of vertices per shape
	 * @param vertices vertices fo the obejct (3 dimensional)
	 * @param ind indices of the faces (vps per face)
	 * @param vps number of vertices associated with each individual shape
	 */
	public VAO(float[] vertices, int[] ind, int vps) {
		vbos = new VBO[1];
		id = glGenVertexArrays();
		vaoIDs.add(id);
		glBindVertexArray(id);
		vbos[0] = new VBO(vertices, 3);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		ibo = glGenBuffers();
		VBO.vboIDs.add(ibo);
		indices = ind;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		verticesPerShape = vps;
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY;
		float maxZ = Float.NEGATIVE_INFINITY;
		for (int vertStart = 0; vertStart < vertices.length;vertStart += 3) {
			if (vertices[vertStart] < minX) {
				minX = vertices[vertStart];
			}
			if (vertices[vertStart] > maxX) {
				maxX = vertices[vertStart];
			}
			if (vertices[vertStart + 1] < minY) {
				minY = vertices[vertStart + 1];
			}
			if (vertices[vertStart + 1] > maxY) {
				maxY = vertices[vertStart];
			}
			if (vertices[vertStart + 2] < minZ) {
				minZ = vertices[vertStart];
			}
			if (vertices[vertStart + 2] > maxZ) {
				maxZ = vertices[vertStart];
			}
		}
		boundingBox = new Box3d(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * create a square object
	 * @param x corner xpos
	 * @param y corner ypos
	 * @param width width of the box
	 * @param height height of the box
	 */
	public VAO(float x, float y, float width, float height) {
		vbos = new VBO[1];
		id = glGenVertexArrays();
		vaoIDs.add(id);
		glBindVertexArray(id);
		vbos[0] = new VBO(new float[] {
				x, y + height, 0f,
				x, y, 0f,
				x + width, y, 0f,
				x + width, y + height, 0f
		}, 3);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		addVBO(VBO.squareTCBO());
		ibo = glGenBuffers();
		VBO.vboIDs.add(ibo);
		indices = squareIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		verticesPerShape = 3;
		float minX = x;
		float maxX = x + width;
		float minY = y;
		float maxY = y + height;
		float minZ = 0f;
		float maxZ = 0f;
		boundingBox = new Box3d(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * creates a vao (and vbos) from a obj file
	 * @param path path to the file
	 */
	public VAO(String path) {
		OBJInformation info = OBJInformation.getInfo(path);
		vbos = new VBO[1];
		id = glGenVertexArrays();
		vaoIDs.add(id);
		glBindVertexArray(id);
		vbos[0] = new VBO(info.vertices, 3);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		ibo = glGenBuffers();
		VBO.vboIDs.add(ibo);
		indices = info.indexArray;
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		verticesPerShape = 3;
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY;
		float maxZ = Float.NEGATIVE_INFINITY;
		for (int vertStart = 0; vertStart < info.vertices.length;vertStart += 3) {
			if (info.vertices[vertStart] < minX) {
				minX = info.vertices[vertStart];
			}
			if (info.vertices[vertStart] > maxX) {
				maxX = info.vertices[vertStart];
			}
			if (info.vertices[vertStart + 1] < minY) {
				minY = info.vertices[vertStart + 1];
			}
			if (info.vertices[vertStart + 1] > maxY) {
				maxY = info.vertices[vertStart + 1];
			}
			if (info.vertices[vertStart + 2] < minZ) {
				minZ = info.vertices[vertStart + 2];
			}
			if (info.vertices[vertStart + 2] > maxZ) {
				maxZ = info.vertices[vertStart + 2];
			}
		}
		boundingBox = new Box3d(minX, minY, minZ, maxX, maxY, maxZ);
		addVBO(info.textCoords, 2);
		addVBO(info.normals, 3);
	}
	
	/**
	 * fully prepares and renders the object, only use this if rendering a single object that looks like this
	 */
	public void fullRender() {
		prepareRender();
		draw();
		unbind();
	}
	
	/**
	 * binds the vao and enables all of the vbos, prepares the vao to be drawn
	 */
	public void prepareRender() {
		glBindVertexArray(id);
		for(int i = 0; i < vbos.length; ++i) {
			glEnableVertexAttribArray(i);
		}
	}
	
	/**
	 * draws the object, assuming that the VAO represents points
	 */
	public void drawPoints() {
		glDrawElements(GL_POINTS, indices.length, GL_UNSIGNED_INT, 0);
	}
	
	/**
	 * draws the object, assuming that the VAO represents lines
	 */
	public void drawLines() {
		glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
	}
	
	/**
	 * draws the object, assuming that the VAO represents triangles
	 */
	public void drawTriangles() {
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
	
	/**
	 * draws the object, checking how many vertices per shapes the vao holds
	 */
	public void draw() {
		if (verticesPerShape == 1) {
			glDrawElements(GL_POINTS, indices.length, GL_UNSIGNED_INT, 0);
		}else if (verticesPerShape == 2) {
			glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
		}else if (verticesPerShape == 3) {
			glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
		}
	}
	
	/**
	 * disables the vbos then unbinds the vao, cleans up after rendering
	 */
	public void unbind() {
		for(int i = 0; i < vbos.length; ++i) {
			glDisableVertexAttribArray(i);
		}
		glBindVertexArray(0);
	}
	
	/**
	 * binds the vao and enables the specified vbos, prepares the vao to be drawn
	 * @param vboset
	 * the vbos to be enabled
	 */
	public void prepareRender(int[] vboset) {
		glBindVertexArray(id);
		for(int i:vboset) {
			glEnableVertexAttribArray(i);
		}
	}
	
	/**
	 * disables the vbos specified then unbinds the vao, cleans up after rendering
	 * @param vboset
	 * the vbos that should be disabled
	 */
	public void unbind(int[] vboset) {
		for(int i:vboset) {
			glDisableVertexAttribArray(i);
		}
		glBindVertexArray(0);
	}
	
	/**
	 * renders a the vbo in each given position
	 * @param camMatrix camera of the user
	 * @param info list of positions the objects exist at
	 * @param uniform uniform that represents the matrix in the shader
	 */
	public void fullRender(Matrix4f camMatrix, PositionInfo[] info, Uniform uniform) {
		glBindVertexArray(id);
		for(int i = 0; i < vbos.length; ++i) {
			glEnableVertexAttribArray(i);
		}
		if (verticesPerShape == 1) {
			for (PositionInfo p:info) {
				uniform.set(p.translateMatrix(camMatrix));
				glDrawElements(GL_POINTS, indices.length, GL_UNSIGNED_INT, 0);
			}
		}else if (verticesPerShape == 2) {
			for (PositionInfo p:info) {
				uniform.set(p.translateMatrix(camMatrix));
				glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
			}
		}else if (verticesPerShape == 3) {
			for (PositionInfo p:info) {
				uniform.set(p.translateMatrix(camMatrix));
				glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
			}
		}
		for(int i = 0; i < vbos.length; ++i) {
			glDisableVertexAttribArray(i);
		}
	}
	
	/**
	 * renders the vbo given a set of different information
	 * @param camMatrix camera of the user
	 * @param info list of translation matrices
	 * @param matUniform uniform that represents the matrix in the shader
	 * @param uniformInfo info that is to be set to each of the uniforms
	 * @param uniforms list of uniforms where the other uniform information should be stored
	 */
	public void fullRender(Matrix4f camMatrix, PositionInfo[] info, Uniform matUniform, float[][][] uniformInfo, Uniform[] uniforms) {
		glBindVertexArray(id);
		for(int i = 0; i < vbos.length; ++i) {
			glEnableVertexAttribArray(i);
		}
		if (verticesPerShape == 1) {
			for (int number = 0; number < info.length; ++number) {
				matUniform.set(info[number].translateMatrix(camMatrix));
				for (int unif = 0; unif < uniforms.length;++unif) {
					uniforms[unif].set(uniformInfo[number][unif]);
				}
				glDrawElements(GL_POINTS, indices.length, GL_UNSIGNED_INT, 0);
			}
		}else if (verticesPerShape == 2) {
			for (int number = 0; number < info.length; ++number) {
				matUniform.set(info[number].translateMatrix(camMatrix));
				for (int unif = 0; unif < uniforms.length;++unif) {
					uniforms[unif].set(uniformInfo[number][unif]);
				}
				glDrawElements(GL_LINES, indices.length, GL_UNSIGNED_INT, 0);
			}
		}else if (verticesPerShape == 3) {
			for (int number = 0; number < info.length; ++number) {
				matUniform.set(info[number].translateMatrix(camMatrix));
				for (int unif = 0; unif < uniforms.length;++unif) {
					uniforms[unif].set(uniformInfo[number][unif]);
				}
				glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
			}
		}
		for(int i = 0; i < vbos.length; ++i) {
			glDisableVertexAttribArray(i);
		}
	}
	
	/**
	 * adds a vbo to the vao
	 * @param newVBO new vbo
	 */
	public void addVBO(VBO newVBO) {
		VBO[] tempVBOL = new VBO[vbos.length + 1];
		for (int i = 0;i<vbos.length;i++) {
			tempVBOL[i] = vbos[i];
		}
		tempVBOL[vbos.length] = newVBO;
		glBindVertexArray(id);
		glVertexAttribPointer(vbos.length, newVBO.getVecSize(), GL_FLOAT, false, 0, 0);
		vbos = tempVBOL;
	}
	
	/**
	 * adds a vbo to the vbos in the vao
	 * @param info information for the new vbo
	 * @param size vector size of the vbo
	 */
	public void addVBO(float[] info, int size) {
		addVBO(new VBO(info, size));
	}
	
	/**
	 * returns the id of the vao
	 * @return id of the vao
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * gets the indices
	 * @return indices of the indices
	 */
	public int[] getIndices() {
		return indices;
	}
	
	/**
	 * returns the number of vertices per each shape
	 * @return the number of vertices per shape
	 */
	public int getVerticesPerShape() {
		return verticesPerShape;
	}
	
	/**
	 * returns the number of vertices in the vao
	 * @return number of vertices in the vao
	 */
	public int getVertxCount() {
		return vbos[0].getVertexCount();
	}
	
	/**
	 * delets the vao, and ibo
	 */
	public void delete() {
		glDeleteVertexArrays(id);
		glDeleteBuffers(ibo);
		for (int i = 0; i < vaoIDs.size(); ++i) {
			if (vaoIDs.get(i) == id) {
				vaoIDs.remove(i);
			}
		}
		for (int i = 0;i < VBO.vboIDs.size();++i) {
			if (VBO.vboIDs.get(i) == ibo) {
				VBO.vboIDs.remove(i);
				break;
			}
		}
	}
	
	/**
	 * deletes all vbos in the vao
	 */
	public void deleteVBOS() {
		for (VBO v:vbos) {
			v.delete();
		}
	}
	
	/**
	 * indices for a square
	 * @return indices for a square
	 */
	public static int[] squareIndices() {
		return new int[] {
				0, 1, 3,
				3, 1, 2
		};
	}
	
	/**
	 * texture coordinates for a square vao
	 * @return texture coordinates for a square
	 */
	public static float[] squareTC() {
		return new float[] {0f, 0f,
				0f, 1f,
				1f, 1f,
				1f, 0f
		};
	}
}