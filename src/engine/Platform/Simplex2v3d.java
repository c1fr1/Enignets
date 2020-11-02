package engine.Platform;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Simplex2v3d {
	public Vector3f a;
	public Vector3f b;
	public Vector3f c;

	public Vector3f normal;

	/**
	 * creates a new simplex given 3 points
	 *
	 * @param a first point
	 * @param b second point
	 * @param c third point
	 */
	public Simplex2v3d(Vector3f a, Vector3f b, Vector3f c) {
		Vector3f na = new Vector3f(a.x - c.x, a.y - c.y, a.z - c.z);
		Vector3f nb = new Vector3f(b.x - c.x, b.y - c.y, b.z - c.z);
		normal = na.cross(nb).normalize();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 * creates a new simplex given the coordinates for 3 points
	 *
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
		normal = na.cross(nb).normalize();
		a = new Vector3f(ax, ay, az);
		b = new Vector3f(bx, by, bz);
		c = new Vector3f(cx, cy, cz);
	}

	/**
	 * creates a new simplex from normal and plots the points around a given point
	 *
	 * @param norm  normal of the new simplex
	 * @param point point to base the other 3 around them
	 */
	public Simplex2v3d(Vector3f norm, Vector3f point) {
		normal = norm;

		a = new Vector3f(-1f, (-norm.x + norm.z) / norm.y + point.y, 1f);
		b = new Vector3f(1f, (norm.x + norm.z) / norm.y + point.y, 1f);
		c = new Vector3f(-1f, (-norm.x - norm.z) / norm.y + point.y, -1f);
	}

	/**
	 * creates a new simplex from a x and z coefficient for an equation that represents the plane
	 *
	 * @param xCoeff x coefficient
	 * @param zCoeff z coefficient
	 * @param point  point to base the points around
	 */
	public Simplex2v3d(float xCoeff, float zCoeff, Vector3f point) {
		a = new Vector3f(-1f + point.x, -xCoeff + zCoeff + point.y, 1f + point.z);
		b = new Vector3f(1f + point.x, xCoeff + zCoeff + point.y, 1f + point.z);
		c = new Vector3f(-1f + point.x, -xCoeff - zCoeff + point.y, -1f + point.z);
		Vector3f na = new Vector3f(a.x - c.x, a.y - c.y, a.z - c.z);
		Vector3f nb = new Vector3f(b.x - c.x, b.y - c.y, b.z - c.z);

		normal = na.cross(nb).normalize();
	}

	/**
	 * gets the corresponding x of a y and z position
	 *
	 * @param y y position
	 * @param z z position
	 * @return x coordinate
	 */
	public float getx(float y, float z) {
		return -(normal.y * (y - c.y) + normal.z * (z - c.z)) / normal.x + c.x;
	}

	/**
	 * gets the corresponding x of a y and z position in a vector
	 *
	 * @param v vector holding the y and z position
	 * @return x coordinate
	 */
	public float getx(Vector2f v) {
		return getx(v.x, v.y);
	}

	/**
	 * gets the corresponding y of a x and z position
	 *
	 * @param x x position
	 * @param z z position
	 * @return y coordinate
	 */
	public float gety(float x, float z) {
		return -(normal.x * (x - c.x) + normal.z * (z - c.z)) / normal.y + c.y;
	}

	/**
	 * gets the corresponding y of a x and z position in a vector
	 *
	 * @param v vector holding the x and z position
	 * @return y coordinate
	 */
	public float gety(Vector2f v) {
		return gety(v.x, v.y);
	}

	/**
	 * gets the corresponding z of a x and y position
	 *
	 * @param x x position
	 * @param y y position
	 * @return z coordinate
	 */
	public float getz(float x, float y) {
		return -(normal.x * (x - c.x) + normal.y * (y - c.y)) / normal.z + c.z;
	}

	/**
	 * gets the corresponding z of a x and y position in a vector
	 *
	 * @param v vector holding the x and y position
	 * @return z coordinate
	 */
	public float getz(Vector2f v) {
		return getz(v.x, v.y);
	}

	public float getTIntersect(Ray3f l) {
		Vector3f k = l.start.sub(a, new Vector3f());
		return -k.dot(normal) / l.delta.dot(normal);
	}

	public Vector3f getIntersectPoint(Ray3f l) {
		return l.intersectionPoint(this);
	}

	public Ray3f getReflected(Ray3f ray) {
		Vector3f intersection = getIntersectPoint(ray);
		if (intersection == null) {
			return null;
		}
		return new Ray3f(intersection, normal.mul(2 * normal.dot(ray.delta) / normal.lengthSquared(), new Vector3f()).add(ray.delta));
	}
}