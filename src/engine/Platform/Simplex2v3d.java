package engine.Platform;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Simplex2v3d {
	private Vector3f pointA;
	private Vector3f pointB;
	private Vector3f pointC;
	
	private Vector3f normal;
	private Vector3f xProduct;
	
	/**
	 * creates a new simplex given 3 points
	 * @param a first point
	 * @param b second point
	 * @param c third point
	 */
	public Simplex2v3d(Vector3f a, Vector3f b, Vector3f c) {
		Vector3f na = new Vector3f(a.x - c.x, a.y - c.y, a.z - c.z);
		Vector3f nb = new Vector3f(b.x - c.x, b.y - c.y, b.z - c.z);
		xProduct = na.cross(nb).normalize();
		normal = xProduct.normalize(new Vector3f());
		pointA = a;
		pointB = b;
		pointC = c;
	}
	
	/**
	 * creates a new simplex given the coordinates for 3 points
	 * @param ax first x
	 * @param ay first y
	 * @param az first z
	 * @param bx second x
	 * @param by second y
	 * @param bz second z
	 * @param cx third x
	 * @param cy third y
	 * @param cz third z
	 */
	public Simplex2v3d(float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz) {
		Vector3f na = new Vector3f(ax - cx, ay - cy, az - cz);
		Vector3f nb = new Vector3f(bx - cx, by - cy, bz - cz);
		xProduct = na.cross(nb).normalize();
		normal = xProduct.normalize(new Vector3f());
		pointA = new Vector3f(ax, ay, az);
		pointB = new Vector3f(bx, by, bz);
		pointC = new Vector3f(cx, cy, cz);
	}
	
	/**
	 * creates a new simplex from normal and plots the points around a given point
	 * @param norm normal of the new simplex
	 * @param point point to base the other 3 around them
	 */
	public Simplex2v3d(Vector3f norm, Vector3f point) {
		normal = norm;
		xProduct = normal;
		
		pointA = new Vector3f(-1f, (-norm.x + norm.z)/norm.y + point.y, 1f);
		pointB = new Vector3f(1f, (norm.x + norm.z)/norm.y + point.y, 1f);
		pointC = new Vector3f(-1f, (-norm.x - norm.z)/norm.y + point.y, -1f);
		Vector3f na = new Vector3f(pointA.x - pointC.x, pointA.y - pointC.y, pointA.z - pointC.z);
		Vector3f nb = new Vector3f(pointB.x - pointC.x, pointB.y - pointC.y, pointB.z - pointC.z);
		xProduct = na.cross(nb).normalize();
		
		normal = xProduct.normalize(new Vector3f());
	}
	
	/**
	 * creates a new simplex from a x and z coefficient for an equation that represents the plane
	 * @param xCoeff x coefficient
	 * @param zCoeff z coefficient
	 * @param point point to base the points around
	 */
	public Simplex2v3d(float xCoeff, float zCoeff, Vector3f point) {
		pointA = new Vector3f(-1f + point.x, -xCoeff + zCoeff + point.y, 1f + point.z);
		pointB = new Vector3f(1f + point.x, xCoeff + zCoeff + point.y, 1f + point.z);
		pointC = new Vector3f(-1f + point.x, -xCoeff - zCoeff + point.y, -1f + point.z);
		Vector3f na = new Vector3f(pointA.x - pointC.x, pointA.y - pointC.y, pointA.z - pointC.z);
		Vector3f nb = new Vector3f(pointB.x - pointC.x, pointB.y - pointC.y, pointB.z - pointC.z);
		xProduct = na.cross(nb).normalize();
		
		normal = xProduct.normalize(new Vector3f());
	}
	
	/**
	 * gets the corresponding x of a y and z position
	 * @param y y position
	 * @param z z position
	 * @return x coordinate
	 */
	public float getx(float y, float z) {
		return -(xProduct.y*(y-pointC.y) + xProduct.z*(z-pointC.z))/xProduct.x + pointC.x;
	}
	
	/**
	 * gets the corresponding x of a y and z position in a vector
	 * @param v vector holding the y and z position
	 * @return x coordinate
	 */
	public float getx(Vector2f v) {
		return getx(v.x, v.y);
	}
	
	/**
	 * gets the corresponding y of a x and z position
	 * @param x x position
	 * @param z z position
	 * @return y coordinate
	 */
	public float gety(float x, float z) {
		return -(xProduct.x*(x-pointC.x) + xProduct.z*(z-pointC.z))/xProduct.y + pointC.y;
	}
	
	/**
	 * gets the corresponding y of a x and z position in a vector
	 * @param v vector holding the x and z position
	 * @return y coordinate
	 */
	public float gety(Vector2f v) {
		return gety(v.x, v.y);
	}
	
	/**
	 * gets the corresponding z of a x and y position
	 * @param x x position
	 * @param y y position
	 * @return z coordinate
	 */
	public float getz(float x, float y) {
		return -(xProduct.x*(x-pointC.x) + xProduct.y*(y-pointC.y))/xProduct.z + pointC.z;
	}
	
	/**
	 * gets the corresponding z of a x and y position in a vector
	 * @param v vector holding the x and y position
	 * @return z coordinate
	 */
	public float getz(Vector2f v) {
		return getz(v.x, v.y);
	}
}
