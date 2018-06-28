package engine.Platform;

import engine.OpenGL.VAO;
import org.joml.Vector3f;

public class Box3d {

	float minx;
	float maxx;
	float miny;
	float maxy;
	float minz;
	float maxz;
	
	/**
	 * creates a box based on the maximum and minimum coordinates
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param zmin minimum z
	 * @param xmax maximum x
	 * @param ymax maximum y
	 * @param zmax maximum z
	 */
	public Box3d(float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
		minx = xmin;
		maxx = xmax;
		miny = ymin;
		maxy = ymax;
		minz = zmin;
		maxz = zmax;
	}
	
	/**
	 * creates a box based on two points at opposite corners
	 * @param a first corner
	 * @param b opposing corner
	 */
	public Box3d(Vector3f a, Vector3f b) {
		minx = Math.min(a.x, b.x);
		maxx = Math.max(a.x, b.x);
		miny = Math.min(a.y, b.y);
		maxy = Math.max(a.y, b.y);
		minz = Math.min(a.z, b.z);
		maxz = Math.max(a.z, b.z);
	}
	
	/**
	 * returns the center of the box
	 * @return center of the box
	 */
	public Vector3f getCenter() {
		return new Vector3f((minx + maxx)/2, (miny + maxy)/2, (minz + maxz)/2);
	}
	
	/**
	 * checks if a point lies within the bounds of the box
	 * @param point point to check
	 * @return if the point is in the box
	 */
	public boolean contains(Vector3f point) {
		return minx < point.x && maxx > point.x && miny < point.y && maxy > point.y && minz < point.z && maxz > point.z;
	}

	public boolean contains(float x, float y, float z) {
		return minx < x && maxx > x && miny < y && maxy > y && minz < z && maxz > z;
	}

	/**
	 * returns true if the other box is completely inside this box
	 * @param other the other box
	 * @return true if the other box is in this box
	 */
	public boolean contains(Box3d other) {
		return minx < other.minx && maxx > other.maxx && miny < other.miny && maxy > other.maxy && minz < other.minz && maxz > other.maxz;
	}

	/**
	 * returns true if any of the vertices of the other box are touching the box;
	 * @param other the other box
	 * @return true if the box is touching the other box
	 */
	public boolean touching(Box3d other) {
		return contains(other.minx, other.miny, other.minz) || contains(other.minx, other.miny, other.maxz) || contains(other.minx, other.maxy, other.maxz) || contains(other.minx, other.maxy, other.minz) || contains(other.maxx, other.miny, other.minz) || contains(other.maxx, other.miny, other.maxz) || contains(other.maxx, other.maxy, other.maxz) || contains(other.maxx, other.maxy, other.minz);
	}

	/**
	 * returns true if the box contains any of the points
	 * @param points the points
	 * @return true if any of the points are in the box
	 */
	public boolean touches(Vector3f[] points) {
		for (Vector3f point:points) {
			if (contains(point)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if any of the points are contained in the box
	 * @param points formatted [x1, y1, z1, x2, y2, z2...]
	 * @return true if any of the points are contained in the box
	 */
	public boolean touches(float[] points) {
		for (int i = 0;i<points.length;i+=3) {
			if (contains(points[i], points[i + 1], points[i + 2])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if any of the points in the vertice vbo are in the box
	 * @param object obejct to check
	 * @return true if the vbo is touching the box
	 */
	public boolean touches(VAO object) {
		return touches(object.vbos[0].getData());
	}
	
	/**
	 * gets a VAO that represents the box
	 * @return VAO representing the box
	 */
	public VAO getVAO() {
		float[] vertices = new float[] {
				minx, miny, minz,// 0
				minx, miny, maxz,// 1
				maxx, miny, maxz,// 2
				maxx, miny, minz,// 3
				minx, maxy, minz,// 4
				maxx, maxy, minz,// 5
				
				minx, miny, minz,// 6
				minx, miny, maxz,// 7
				minx, maxy, maxz,// 8
				minx, maxy, minz,// 9
				maxx, miny, maxz,// 10
				maxx, maxy, maxz,// 11
				
				maxx, miny, minz,// 12
				maxx, miny, maxz,// 13
				maxx, maxy, maxz,// 14
				maxx, maxy, minz,// 15
				minx, maxy, minz,// 16
				minx, maxy, maxz,// 17
		};
		float[] textureCoordinates = new float[] {
				0.5f, 1f/3f,// 0
				0f  , 1f/3f,// 1
				0f  , 0f   ,// 2
				0.5f, 0f   ,// 3
				1f  , 1f/3f,// 4
				1f  , 0f   ,// 5
				
				0f  , 1f/3f,// 6
				0.5f, 1f/3f,// 7
				0.5f, 2f/3f,// 8
				0f  , 2f/3f,// 9
				1f  , 1f/3f,// 10
				1f  , 2f/3f,// 11
				
				0f  , 2f/3f,// 12
				0f  , 1f   ,// 13
				0.5f, 1f   ,// 14
				0.5f, 2f/3f,// 15
				1f  , 2f/3f,// 16
				1f  , 1f   ,// 17
		};
		int[] indices = new int[] {
				0, 2, 1,
				0, 3, 2,
				4, 5, 0,
				5, 3, 0,
				
				8, 6, 7,
				9, 6, 8,
				8, 10, 11,
				8, 7, 10,
				
				15, 14, 13,
				15, 13, 12,
				17, 14, 15,
				17, 15, 16,
		};
		VAO ret = new VAO(vertices, indices);
		ret.addVBO(textureCoordinates, 2);
		return ret;
	}
	
	/**
	 * gets the string representation of the box
	 * @return string representation of the box
	 */
	public String toString() {
		return "x ranges from " + minx + " to " + maxx + "\ny ranges from " + miny + " to " + maxy + "\nz ranges from " + minz + " to " + maxz;
	}
}
