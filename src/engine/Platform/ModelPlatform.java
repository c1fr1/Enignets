package engine.Platform;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.VAO;
import org.joml.Vector3f;

import java.util.ArrayList;

public class ModelPlatform extends PlatformSegment {
	private VAO object;
	private Simplex2v2d[] triangles;
	private Simplex2v3d[] planes;
	private Box2d range;
	private ArrayList<Integer>[][] sections;
	private Simplex2v3d defaultPlane = new Simplex2v3d(0f, 0f, new Vector3f(0f, 0f, 0f));
	
	/**
	 * creates a platform with a given bounding box and path to a obj file to represent the platform
	 * @param path path to the obj file
	 * @param minX minimum x of the bounding box
	 * @param maxX maximum x of the bounding box
	 * @param minZ minimum z of the bounding box
	 * @param maxZ maximum z of the bounding box
	 * @param xSteps number of lines on the grid in the x direction for the grid that sorts the simplices
	 * @param zSteps number of lines on the grid in the z direction for the grid that sorts the simplices
	 */
	public ModelPlatform(String path, float minX, float maxX, float minZ, float maxZ, int xSteps, int zSteps) {
		range = new Box2d(minX, minZ, maxX, maxZ);
		object = new VAO(path);
		init(minX, maxX, minZ, maxZ, xSteps, zSteps);
	}
	
	/**
	 * creates a platform with a given bounding box and a VAO
	 * @param vao VAO the platform should represent
	 * @param minX minimum x of the bounding box
	 * @param maxX maximum x of the bounding box
	 * @param minZ minimum z of the bounding box
	 * @param maxZ maximum z of the bounding box
	 * @param xSteps number of lines on the grid in the x direction for the grid that sorts the simplices
	 * @param zSteps number of lines on the grid in the z direction for the grid that sorts the simplices
	 */
	public ModelPlatform(VAO vao, float minX, float maxX, float minZ, float maxZ, int xSteps, int zSteps) {
		range = new Box2d(minX, minZ, maxX, maxZ);
		object = vao;
		init(minX, maxX, minZ, maxZ, xSteps, zSteps);
	}
	
	/**
	 * creates a platform with a given path to an obj
	 * @param path path to the obj file
	 * @param xSteps number of lines on the grid in the x direction for the grid that sorts the simplices
	 * @param zSteps number of lines on the grid in the z direction for the grid that sorts the simplices
	 */
	public ModelPlatform(String path, int xSteps, int zSteps) {
		object = new VAO(path);
		float minX = object.boundingBox.minx;
		float minZ = object.boundingBox.minz;
		float maxX = object.boundingBox.maxx;
		float maxZ = object.boundingBox.maxz;
		range = new Box2d(minX, minZ, maxX, maxZ);
		init(minX, maxX, minZ, maxZ, xSteps, zSteps);
	}
	
	/**
	 * creates a platform given a VAO
	 * @param vao VAO the platform should represent
	 * @param xSteps number of lines on the grid in the x direction for the grid that sorts the simplices
	 * @param zSteps number of lines on the grid in the z direction for the grid that sorts the simplices
	 */
	public ModelPlatform(VAO vao, int xSteps, int zSteps) {
		object = vao;
		float minX = object.boundingBox.minx;
		float minZ = object.boundingBox.minz;
		float maxX = object.boundingBox.maxx;
		float maxZ = object.boundingBox.maxz;
		range = new Box2d(minX, minZ, maxX, maxZ);
		init(minX, maxX, minZ, maxZ, xSteps, zSteps);
	}
	
	/**
	 * creates a platform with a given path to an obj
	 * @param path path to the obj file
	 */
	public ModelPlatform(String path) {
		object = new VAO(path);
		float minX = object.boundingBox.minx;
		float minZ = object.boundingBox.minz;
		float maxX = object.boundingBox.maxx;
		float maxZ = object.boundingBox.maxz;
		range = new Box2d(minX, minZ, maxX, maxZ);
		float density = ((float)object.getIndices().length)/3f;
		init(minX, maxX, minZ, maxZ, (int)Math.sqrt(density*(maxX-minX)/(maxZ-minZ)), (int)Math.sqrt(density*(maxZ-minZ)/(maxX-minX)));
	}
	
	/**
	 * creates a platform given a VAO
	 * @param vao VAO the platform should represent
	 */
	public ModelPlatform(VAO vao) {
		object = vao;
		float minX = object.boundingBox.minx;
		float minZ = object.boundingBox.minz;
		float maxX = object.boundingBox.maxx;
		float maxZ = object.boundingBox.maxz;
		range = new Box2d(minX, minZ, maxX, maxZ);
		float density = ((float)object.getIndices().length)/3f;
		init(minX, maxX, minZ, maxZ, (int)Math.sqrt(density*(maxX-minX)/(maxZ-minZ)), (int)Math.sqrt(density*(maxZ-minZ)/(maxX-minX)));
	}
	
	/**
	 * creates a platform with a given bounding box and a VAO
	 * @param path path to the obj file
	 * @param minX minimum x of the bounding box
	 * @param maxX maximum x of the bounding box
	 * @param minZ minimum z of the bounding box
	 * @param maxZ maximum z of the bounding box
	 */
	public ModelPlatform(String path, float minX, float maxX, float minZ, float maxZ) {
		object = new VAO(path);
		range = new Box2d(minX, minZ, maxX, maxZ);
		float density = ((float)object.getIndices().length)/3f;
		init(minX, maxX, minZ, maxZ, (int)Math.sqrt(density*(maxX-minX)/(maxZ-minZ)), (int)Math.sqrt(density*(maxZ-minZ)/(maxX-minX)));
	}
	
	/**
	 * creates a platform with a given bounding box and a VAO
	 * @param vao VAO the platform should represent
	 * @param minX minimum x of the bounding box
	 * @param maxX maximum x of the bounding box
	 * @param minZ minimum z of the bounding box
	 * @param maxZ maximum z of the bounding box
	 */
	public ModelPlatform(VAO vao, float minX, float maxX, float minZ, float maxZ) {
		object = vao;
		range = new Box2d(minX, minZ, maxX, maxZ);
		float density = ((float)object.getIndices().length)/3f;
		init(minX, maxX, minZ, maxZ, (int)Math.sqrt(density*(maxX-minX)/(maxZ-minZ)), (int)Math.sqrt(density*(maxZ-minZ)/(maxX-minX)));
	}
	
	/**
	 * performs basic setup for the platform
	 * @param minX minimum x of the bounding box
	 * @param maxX maximum x of the bounding box
	 * @param minZ minimum z of the bounding box
	 * @param maxZ maximum z of the bounding box
	 * @param xSteps number of lines on the grid in the x direction for the grid that sorts the simplices
	 * @param ySteps number of lines on the grid in the z direction for the grid that sorts the simplices
	 */
	private void init(float minX, float maxX, float minZ, float maxZ, int xSteps, int ySteps) {
		float[] vertexInfo = object.vbos[0].getData();
		int[] indices = object.getIndices();
		triangles = new Simplex2v2d[indices.length/3];
		planes = new Simplex2v3d[indices.length/3];
		for (int i = 0; i < indices.length; i+=3) {
			int vertA = indices[i];
			int vertB = indices[i+1];
			int vertC = indices[i+2];
			triangles[i/3] = new Simplex2v2d(vertexInfo[vertA*3], vertexInfo[vertA*3 + 2], vertexInfo[vertB*3], vertexInfo[vertB*3 + 2], vertexInfo[vertC*3], vertexInfo[vertC*3 + 2]);
			planes[i/3] = new Simplex2v3d(vertexInfo[vertA*3], vertexInfo[vertA*3 + 1], vertexInfo[vertA*3 + 2], vertexInfo[vertB*3], vertexInfo[vertB*3 + 1], vertexInfo[vertB*3 + 2], vertexInfo[vertC*3], vertexInfo[vertC*3 + 1], vertexInfo[vertC*3 + 2]);
		}
		float xRange = maxX - minX;
		float yRange = maxZ - minZ;
		float xWidth = xRange/(float)xSteps;
		float yWidth = yRange/(float)ySteps;
		sections = new ArrayList[xSteps][ySteps];//int[xSteps][ySteps];//x and y might need to be switched somewhere here
		for (int x = 0; x < sections.length; ++x) {
			for (int y = 0; y < sections[x].length; ++y) {
				Box2d area = new Box2d((float)x*xWidth + minX, (float)y*yWidth + minZ, (float)x*xWidth + minX + xWidth, (float)y*yWidth + minZ + yWidth);//minxy maxxy
				sections[x][y] = new ArrayList<>();
				for (int i = 0; i < triangles.length;++i) {
					if (triangles[i].touches(area)) {
						sections[x][y].add(i);
					}
				}
			}
		}
	}
	
	/**
	 * finds the height of the object on the plane given the x and z positions
	 * @param xPos x position
	 * @param zPos z position
	 * @return y value that corresponds to the x and z positions
	 */
	public float getHeight(float xPos, float zPos) {
		int x = (int) Math.round((xPos - range.minx)*((float) sections.length)/range.width() - 0.5f);
		int y = (int) Math.round((zPos - range.miny)*((float) sections[0].length)/range.height() - 0.5f);
		if (x < 0) {
			x = 0;
		}else if (x >= sections.length) {
			x = sections.length - 1;
		}
		if (y < 0) {
			y = 0;
		}else if (y >= sections[0].length) {
			y = sections[0].length - 1;
		}
		ArrayList<Integer> section = sections[x][y];
		for (int i = 0;i < section.size();++i) {
			if (triangles[section.get(i)].containsPoint(xPos, zPos)) {
				int[] ind = object.getIndices();
				ShaderProgram.currentShaderProgram.shaders[0].uniforms[1].set(ind[section.get(i)*3]);
				ShaderProgram.currentShaderProgram.shaders[0].uniforms[2].set(ind[section.get(i)*3 + 1]);
				ShaderProgram.currentShaderProgram.shaders[0].uniforms[3].set(ind[section.get(i)*3 + 2]);
				return planes[section.get(i)].gety(xPos, zPos);
			}
		}
		for (int xoff = -1; xoff <= 1; ++xoff) {
			for (int yoff = -1; yoff <= 1; ++yoff) {
				if ((xoff != 0 || yoff != 0) && x + xoff >= 0 && x + xoff < sections.length && y + yoff >= 0 && y + yoff < sections[0].length) {
					section = sections[x + xoff][y + yoff];
					for (int i = 0; i < section.size(); ++i) {
						if (triangles[section.get(i)].containsPoint(xPos, zPos)) {
							int[] ind = object.getIndices();
							ShaderProgram.currentShaderProgram.shaders[0].uniforms[1].set(ind[section.get(i) * 3]);
							ShaderProgram.currentShaderProgram.shaders[0].uniforms[2].set(ind[section.get(i) * 3 + 1]);
							ShaderProgram.currentShaderProgram.shaders[0].uniforms[3].set(ind[section.get(i) * 3 + 2]);
							return planes[section.get(i)].gety(xPos, zPos);
						}
					}
				}
			}
		}
		return defaultPlane.gety(xPos, zPos);
	}
	
	/**
	 * returns the plane tangent to the platform at a given x an z position
	 * @param xPos x positon
	 * @param zPos z position
	 * @return the plane tangent to the platform at the position
	 */
	public Simplex2v3d getTangentPlane(float xPos, float zPos) {
		int x = (int) Math.round((xPos - range.minx)*((float) sections.length)/range.width() - 0.5f);
		int y = (int) Math.round((zPos - range.miny)*((float) sections[0].length)/range.height() - 0.5f);
		if (x < 0) {
			x = 0;
		}else if (x >= sections.length) {
			x = sections.length - 1;
		}
		if (y < 0) {
			y = 0;
		}else if (y >= sections[0].length) {
			y = sections[0].length - 1;
		}
		ArrayList<Integer> section = sections[x][y];
		for (int i = 0;i < section.size();++i) {
			if (triangles[section.get(i)].containsPoint(xPos, zPos, 0.001f)) {
				return planes[section.get(i)];
			}
		}
		for (int xoff = -1; xoff <= 1; ++xoff) {
			for (int yoff = -1; yoff <= 1; ++yoff) {
				if ((xoff != 0 || yoff != 0) && x + xoff >= 0 && x + xoff < sections.length && y + yoff >= 0 && y + yoff < sections[0].length) {
					section = sections[x + xoff][y + yoff];
					for (int i = 0;i < section.size();++i) {
						if (triangles[section.get(i)].containsPoint(xPos, zPos, 0.001f)) {
							return planes[section.get(i)];
						}
					}
				}
			}
		}
		return defaultPlane;
	}
	
	/**
	 * returns the object that is represented by this platform
	 * @return object represented by this platform
	 */
	public VAO getObject() {
		return object;
	}
	
	/**
	 * returns the 2d 2-simplices that are part of the VAO (excludes y coordinate)
	 * @return 2d 2-simplices that make up the VAO
	 */
	public Simplex2v2d[] getTriangles() {
		return triangles;
	}
	
	/**
	 * returns the planes that are extensions of the 2-simplices that are part of the VAO
	 * @return planes
	 */
	public Simplex2v3d[] getPlanes() {
		return planes;
	}
	
	/**
	 * gets the x-z range of the platform
	 * @return
	 */
	public Box2d getRange() {
		return range;
	}
	
	/**
	 * gets grid of indices of simplices
	 * @return grid of indices of simplices
	 */
	public ArrayList<Integer>[][] getSections() {
		return sections;
	}
	
	/**
	 * gets the plane that will be used if no simplices on the VAO cover an x-z position
	 * @return current default plane
	 */
	public Simplex2v3d getDefaultPlane() {
		return defaultPlane;
	}
	
	/**
	 * changes the plane that will be used if no simplices on the VAO cover an x-z position
	 * @param defaultPlane new plane
	 */
	public void setDefaultPlane(Simplex2v3d defaultPlane) {
		this.defaultPlane = defaultPlane;
	}
	
	/**
	 * checks to see if a plane is the default plane
	 * @param other other plane
	 * @return if the other plane is same as the default plane
	 */
	public boolean isDefaultPlane(Simplex2v3d other) {
		return other == defaultPlane;
	}
}
