package engine;

import org.joml.Vector3f;

public class Box {

	float minx;
	float maxx;
	float miny;
	float maxy;
	float minz;
	float maxz;

	public Box(float xmin, float ymin, float zmin, float xmax, float ymax, float zmax) {
		minx = xmin;
		maxx = xmax;
		miny = ymin;
		maxy = ymax;
		minz = zmin;
		maxz = zmax;
	}

	public Box(Vector3f a, Vector3f b) {
		minx = Math.min(a.x, b.x);
		maxx = Math.max(a.x, b.x);
		miny = Math.min(a.y, b.y);
		maxy = Math.max(a.y, b.y);
		minz = Math.min(a.z, b.z);
		maxz = Math.max(a.z, b.z);
	}

	public Vector3f getCenter() {
		return new Vector3f((minx + maxx)/2, (miny + maxy)/2, (minz + maxz)/2);
	}

	public boolean contains(Vector3f point) {
		return minx < point.x && maxx > point.x && miny < point.y && maxy > point.y && minz < point.z && maxz > point.z;
	}

	public boolean contains(float x, float y, float z) {
		return minx < x && maxx > x && miny < y && maxy > y && minz < z && maxz > z;
	}

	/**
	 * returns true if the other box is completely inside this box
	 * @param other
	 * the other box
	 * @return
	 * true if the other box is in this box
	 */
	public boolean contains(Box other) {
		return minx < other.minx && maxx > other.maxx && miny < other.miny && maxy > other.maxy && minz < other.minz && maxz > other.maxz;
	}

	/**
	 * returns true if any of the vertices of the other box are touching the box;
	 * @param other
	 * the other box
	 * @return
	 * true if the box is touching the other box
	 */
	public boolean touching(Box other) {
		return contains(other.minx, other.miny, other.minz) || contains(other.minx, other.miny, other.maxz) || contains(other.minx, other.maxy, other.maxz) || contains(other.minx, other.maxy, other.minz) || contains(other.maxx, other.miny, other.minz) || contains(other.maxx, other.miny, other.maxz) || contains(other.maxx, other.maxy, other.maxz) || contains(other.maxx, other.maxy, other.minz);
	}

	/**
	 * returns true if the box contains any of the points
	 * @param points
	 * the points
	 * @return
	 * true if any of the points are in the box
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
	 * @param points
	 * formatted [x1, y1, z1, x2, y2, z2...]
	 * @return
	 * true if any of the points are contained in the box
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
	 * @param object
	 * obejct to check
	 * @return
	 * true if the vbo is touching the box
	 */
	public boolean touches(VAO object) {
		return touches(object.vbos[0].getData());
	}
}
