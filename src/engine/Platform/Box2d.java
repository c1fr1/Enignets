package engine.Platform;

import engine.OpenGL.VAO;
import org.joml.Vector2f;

public class Box2d {
	
	float minx;
	float maxx;
	float miny;
	float maxy;
	
	/**
	 * creates a new box given the minimums and maximum coordinates
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param xmax maximum x
	 * @param ymax maximum y
	 */
	public Box2d(float xmin, float ymin, float xmax, float ymax) {
		minx = xmin;
		maxx = xmax;
		miny = ymin;
		maxy = ymax;
	}
	
	/**
	 * creates a new box given two vectors on opposing ends
	 * @param a first corner
	 * @param b opposing corner
	 */
	public Box2d(Vector2f a, Vector2f b) {
		minx = Math.min(a.x, b.x);
		maxx = Math.max(a.x, b.x);
		miny = Math.min(a.y, b.y);
		maxy = Math.max(a.y, b.y);
	}
	
	/**
	 * returns the center of the rectangle
	 * @return center of the triangle
	 */
	public Vector2f getCenter() {
		return new Vector2f((minx + maxx)/2, (miny + maxy)/2);
	}
	
	/**
	 * checks to see if a point ins in the rectangle
	 * @param point point to check
	 * @return if the point is in the rectangle
	 */
	public boolean contains(Vector2f point) {
		return contains(point.x, point.y);
	}
	
	/**
	 * checks to see if a point is in a rectangle that is sligtly larger than
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @param extra extra size that the box is when testing the point
	 * @return if the point is in the larger rectangle
	 */
	public boolean contains(float x, float y, float extra) {
		return minx < x + extra && maxx > x - extra && miny < y + extra && maxy > y - extra;
	}
	
	/**
	 * checks to see if a point is in a rectangle that is sligtly larger than
	 * @param point point to check
	 * @param extra extra size that the box is when testing the point
	 * @return if the point is in the larger rectangle
	 */
	public boolean contains(Vector2f point, float extra) {
		return contains(point.x, point.y, extra);
	}
	
	/**
	 * checks to see if a point ins in the rectangle
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return if the point is in the rectangle
	 */
	public boolean contains(float x, float y) {
		return minx < x && maxx > x && miny < y && maxy > y;
	}
	
	/**
	 * returns true if the other box is completely inside this box
	 * @param other the other box
	 * @return true if the other box is in this box
	 */
	public boolean contains(Box2d other) {
		return minx < other.minx && maxx > other.maxx && miny < other.miny && maxy > other.maxy;
	}
	
	/**
	 * returns true if any of the vertices of the other box are touching the box;
	 * @param other the other box
	 * @return true if the box is touching the other box
	 */
	public boolean touching(Box2d other) {
		return contains(other.minx, other.miny) || contains(other.minx, other.miny) || contains(other.minx, other.maxy) || contains(other.minx, other.maxy) || contains(other.maxx, other.miny) || contains(other.maxx, other.miny) || contains(other.maxx, other.maxy) || contains(other.maxx, other.maxy);
	}
	
	/**
	 * returns true if the box contains any of the points
	 * @param points the points
	 * @return true if any of the points are in the box
	 */
	public boolean touches(Vector2f[] points) {
		for (Vector2f point:points) {
			if (contains(point)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns true if any of the points are contained in the box
	 * @param points formatted [x1, y1, x2, y2...]
	 * @return true if any of the points are contained in the box
	 */
	public boolean touches(float[] points) {
		for (int i = 0;i<points.length;i+=2) {
			if (contains(points[i], points[i + 1])) {
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
	 * creates a new VAO based on the coordinates
	 * @return new VAO based on this object
	 */
	public VAO getVAO() {
		return new VAO(minx, miny, width(), height());
	}
	
	/**
	 * gets the width
	 * @return width
	 */
	public float width() {
		return maxx - minx;
	}
	
	/**
	 * gets the height
	 * @return height
	 */
	public float height() {
		return maxy - miny;
	}
}
