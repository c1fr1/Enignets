package engine.Platform;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Plane {

	private Vector3f normal;

	public Plane(Vector3f a, Vector3f b, Vector3f c) {
		Vector3f na = new Vector3f(a.x - c.x, a.y - c.y, a.z - c.z);
		Vector3f nb = new Vector3f(b.x - c.x, b.y - c.y, b.z - c.z);
		normal = na.cross(nb).normalize();
	}

	public Plane(Vector3f norm) {
		normal = norm;
	}

	public Plane(float xCoeff, float zCoeff) {
		normal = new Vector3f(xCoeff, 1f, zCoeff).normalize();
	}

	public float getx(float y, float z) {
		return (normal.y * y + normal.z * z)/normal.x;
	}

	public float getx(Vector2f v) {
		return getx(v.x, v.y);
	}

	public float gety(float x, float z) {
		return (normal.x * x + normal.z * z)/normal.y;
	}

	public float gety(Vector2f v) {
		return gety(v.x, v.y);
	}

	public float getz(float x, float y) {
		return (normal.x * x + normal.y * y)/normal.z;
	}

	public float getz(Vector2f v) {
		return getz(v.x, v.y);
	}
}
