package engine.Platform;

import org.joml.Vector2f;

public class Simplex2v2d {
	private Vector2f pointA;
	private Vector2f pointB;
	private Vector2f pointC;
	
	float doubleAreaReciprocal;
	
	/**
	 * creates a simplex based on 3 points
	 * @param a first point
	 * @param b second point
	 * @param c third point
	 */
	public Simplex2v2d(Vector2f a, Vector2f b, Vector2f c) {
		pointA = a;
		pointB = b;
		pointC = c;
		updateDoubleAreaReciprocal();
	}
	
	/**
	 * creates a simplex based on the coordinates for 3 points
	 * @param ax first x
	 * @param ay first y
	 * @param bx second x
	 * @param by second y
	 * @param cx third x
	 * @param cy third y
	 */
	public Simplex2v2d(float ax, float ay, float bx, float by, float cx, float cy) {
		pointA = new Vector2f(ax, ay);
		pointB = new Vector2f(bx, by);
		pointC = new Vector2f(cx, cy);
		updateDoubleAreaReciprocal();
	}
	
	/**
	 * sets the double area reciprocal based on the current points (used for point testing)
	 */
	private void updateDoubleAreaReciprocal() {
		doubleAreaReciprocal = 1f/(pointA.x*pointB.y - pointA.x*pointC.y - pointB.x*pointA.y + pointB.x*pointC.y + pointC.x*pointA.y - pointC.x*pointB.y);
	}
	
	/**
	 * checks to see if another point is inside this simplex
	 * @param other point to check
	 * @return if that point is in the simplex
	 */
	public boolean containsPoint(Vector2f other) {
		return containsPoint(other.x, other.y);
	}
	
	/**
	 * checks to see if another point is inside this simplex
	 * @param ox x of the point
	 * @param oy y of the point
	 * @return if that point is in the simplex
	 */
	public boolean containsPoint(float ox, float oy) {
		float u = (ox*pointB.y - ox*pointC.y - pointB.x*oy + pointB.x*pointC.y + pointC.x*oy - pointC.x*pointB.y) * doubleAreaReciprocal;
		float v = (pointA.x*oy - pointA.x*pointC.y - ox*pointA.y + ox*pointC.y + pointC.x*pointA.y - pointC.x*oy) * doubleAreaReciprocal;
		if (u + v > 1f) {
			return false;
		}
		if (u < -0) {
			return false;
		}
		if (v < -0) {
			return false;
		}
		return true;
	}
	
	/**
	 * checks to see if another point is inside this simplex given an extra offset
	 * @param ox x of the point
	 * @param oy y of the point
	 * @param extra extra offset
	 * @return if the point is in the larget simplex
	 */
	public boolean containsPoint(float ox, float oy, float extra) {
		float u = (ox*pointB.y - ox*pointC.y - pointB.x*oy + pointB.x*pointC.y + pointC.x*oy - pointC.x*pointB.y) * doubleAreaReciprocal;
		if (u < -extra) {
			return false;
		}
		float v = (pointA.x*oy - pointA.x*pointC.y - ox*pointA.y + ox*pointC.y + pointC.x*pointA.y - pointC.x*oy) * doubleAreaReciprocal;
		if (u + v > 1f + extra) {
			return false;
		}
		if (v < -extra) {
			return false;
		}
		return true;
	}
	
	/**
	 * checks to see if a 2d box touches
	 * @param box box that could touch the simplex
	 * @return true if the box is touching the simplex
	 */
	public boolean touches(Box2d box) {
		if (box.contains(pointA)) {
			return true;
		}
		if (box.contains(pointB)) {
			return true;
		}
		if (box.contains(pointC)) {
			return true;
		}
		if (containsPoint(box.minx, box.miny)) {
			return true;
		}
		if (containsPoint(box.minx, box.maxy)) {
			return true;
		}
		if (containsPoint(box.maxx, box.miny)) {
			return true;
		}
		if (containsPoint(box.maxx, box.maxy)) {
			return true;
		}
		return false;
	}
	
	/**
	 * checks to see if a 2d box touches given an extra offset
	 * @param box box that could touch the simplex
	 * @param extra extra offset
	 * @return true if the larger box is touching the simplex
	 */
	public boolean touches(Box2d box, float extra) {
		if (box.contains(pointA, extra)) {
			return true;
		}
		if (box.contains(pointB, extra)) {
			return true;
		}
		if (box.contains(pointC, extra)) {
			return true;
		}
		if (containsPoint(box.minx, box.miny, extra)) {
			return true;
		}
		if (containsPoint(box.minx, box.maxy, extra)) {
			return true;
		}
		if (containsPoint(box.maxx, box.miny, extra)) {
			return true;
		}
		if (containsPoint(box.maxx, box.maxy)) {
			return true;
		}
		return false;
	}
	
	/**
	 * sets the first point
	 * @param pointA new first point
	 */
	public void setPointA(Vector2f pointA) {
		this.pointA = pointA;
		updateDoubleAreaReciprocal();
	}
	
	/**
	 * sets the second point
	 * @param pointB new second point
	 */
	public void setPointB(Vector2f pointB) {
		this.pointB = pointB;
		updateDoubleAreaReciprocal();
	}
	
	/**
	 * sets the third point
	 * @param pointC new third point
	 */
	public void setPointC(Vector2f pointC) {
		this.pointC = pointC;
		updateDoubleAreaReciprocal();
	}
	
	/**
	 * gets the first point
	 * @return first point
	 */
	public Vector2f getPointA() {
		return new Vector2f(pointA);
	}
	
	/**
	 * gets the second point
	 * @return second point
	 */
	public Vector2f getPointB() {
		return new Vector2f(pointB);
	}
	
	/**
	 * gets the third point
	 * @return third point
	 */
	public Vector2f getPointC() {
		return new Vector2f(pointC);
	}
}
