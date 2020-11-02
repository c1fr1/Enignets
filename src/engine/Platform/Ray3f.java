package engine.Platform;

import org.joml.Vector3f;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class Ray3f {

	public Vector3f start;
	public Vector3f delta;

	public Ray3f(float sx, float sy, float sz, float dx, float dy, float dz) {
		start = new Vector3f(sx, sy, sz);
		delta = new Vector3f(dx, dy, dz);
	}

	public Ray3f(Vector3f start, Vector3f delta) {
		this.start = new Vector3f(start);
		this.delta = new Vector3f(delta);
	}

	public Ray3f(Ray3f other) {
		start = new Vector3f(other.start);
		delta = new Vector3f(other.delta);
	}

	public Vector3f getPointAt(float t) {
		return new Vector3f(start.x + delta.x * t, start.y + delta.y * t, start.z + delta.z * t);
	}

	public float getXAt(float t) {
		return start.x + delta.x * t;
	}

	public float getYAt(float t) {
		return start.y + delta.y * t;
	}

	public float getZAt(float t) {
		return start.z + delta.z * t;
	}

	public boolean intersects(Simplex2v3d simplex) {
		float t = simplex.getTIntersect(this);
		if (t > 1) {
			return false;
		}
		if (t < 0) {
			return false;
		}

		if (Math.abs(simplex.normal.dot(0, 0, 1)) < 0.00000001) {
			float ox = getXAt(t);
			float oz = getZAt(t);
			float doubleAreaReciprocal = 1f / (simplex.a.x * simplex.b.z - simplex.a.x * simplex.c.z - simplex.b.x * simplex.a.z + simplex.b.x * simplex.c.z + simplex.c.x * simplex.a.z - simplex.c.x * simplex.b.z);
			float u = (ox * simplex.b.z - ox * simplex.c.z - simplex.b.x * oz + simplex.b.x * simplex.c.z + simplex.c.x * oz - simplex.c.x * simplex.b.z) * doubleAreaReciprocal;
			if (u < 0) {
				return false;
			}
			float v = (simplex.a.x * oz - simplex.a.x * simplex.c.z - ox * simplex.a.z + ox * simplex.c.z + simplex.c.x * simplex.a.z - simplex.c.x * oz) * doubleAreaReciprocal;
			if (v < 0) {
				return false;
			}
			if (u + v > 1f) {
				return false;
			}
			return true;
		}
		float ox = getXAt(t);
		float oy = getYAt(t);
		float doubleAreaReciprocal = 1f / (simplex.a.x * simplex.b.y - simplex.a.x * simplex.c.y - simplex.b.x * simplex.a.y + simplex.b.x * simplex.c.y + simplex.c.x * simplex.a.y - simplex.c.x * simplex.b.y);
		float u = (ox * simplex.b.y - ox * simplex.c.y - simplex.b.x * oy + simplex.b.x * simplex.c.y + simplex.c.x * oy - simplex.c.x * simplex.b.y) * doubleAreaReciprocal;
		if (u < 0) {
			return false;
		}
		float v = (simplex.a.x * oy - simplex.a.x * simplex.c.y - ox * simplex.a.y + ox * simplex.c.y + simplex.c.x * simplex.a.y - simplex.c.x * oy) * doubleAreaReciprocal;
		if (v < 0) {
			return false;
		}
		if (u + v > 1f) {
			return false;
		}
		return true;
	}

	public Vector3f intersectionPoint(Simplex2v3d simplex) {
		float t = simplex.getTIntersect(this);
		if (t > 1) {
			return null;
		}
		if (t < 0) {
			return null;
		}

		if (Math.abs(simplex.normal.dot(0, 0, 1)) < 0.00000001) {
			float ox = getXAt(t);
			float oz = getZAt(t);
			float doubleAreaReciprocal = 1f / (simplex.a.x * simplex.b.z - simplex.a.x * simplex.c.z - simplex.b.x * simplex.a.z + simplex.b.x * simplex.c.z + simplex.c.x * simplex.a.z - simplex.c.x * simplex.b.z);
			float u = (ox * simplex.b.z - ox * simplex.c.z - simplex.b.x * oz + simplex.b.x * simplex.c.z + simplex.c.x * oz - simplex.c.x * simplex.b.z) * doubleAreaReciprocal;
			if (u < 0) {
				return null;
			}
			float v = (simplex.a.x * oz - simplex.a.x * simplex.c.z - ox * simplex.a.z + ox * simplex.c.z + simplex.c.x * simplex.a.z - simplex.c.x * oz) * doubleAreaReciprocal;
			if (v < 0) {
				return null;
			}
			if (u + v > 1f) {
				return null;
			}
			return getPointAt(t);
		}
		float ox = getXAt(t);
		float oy = getYAt(t);
		float doubleAreaReciprocal = 1f / (simplex.a.x * simplex.b.y - simplex.a.x * simplex.c.y - simplex.b.x * simplex.a.y + simplex.b.x * simplex.c.y + simplex.c.x * simplex.a.y - simplex.c.x * simplex.b.y);
		float u = (ox * simplex.b.y - ox * simplex.c.y - simplex.b.x * oy + simplex.b.x * simplex.c.y + simplex.c.x * oy - simplex.c.x * simplex.b.y) * doubleAreaReciprocal;
		if (u < 0) {
			return null;
		}
		float v = (simplex.a.x * oy - simplex.a.x * simplex.c.y - ox * simplex.a.y + ox * simplex.c.y + simplex.c.x * simplex.a.y - simplex.c.x * oy) * doubleAreaReciprocal;
		if (v < 0) {
			return null;
		}
		if (u + v > 1f) {
			return null;
		}
		return getPointAt(t);
	}

	public void rotateX(float theta) {
		start.rotateX(theta);
		delta.rotateX(theta);
	}

	public void rotateY(float theta) {
		start.rotateY(theta);
		delta.rotateY(theta);
	}

	public void rotateZ(float theta) {
		start.rotateZ(theta);
		delta.rotateZ(theta);
	}

	public void translate(float x, float y, float z) {
		start.add(x, y, z);
	}

	public void translate(Vector3f offset) {
		start.add(offset);
	}

	public float closestTTo(Vector3f point) {
		return - (delta.x * (start.x - point.x) + delta.y * (start.y - point.y) + delta.z * (start.z - point.z)) / (delta.lengthSquared());
	}

	public Vector3f closestPointTo(Vector3f other) {
		return getPointAt(closestTTo(other));
	}

	public float closestDistanceSquared(Vector3f other) {
		return closestPointTo(other).distanceSquared(other);
	}

	public float closestDistance(Vector3f other) {
		return (float) sqrt(closestDistanceSquared(other));
	}

	public Vector3f closestPointInRangeTo(Vector3f other) {
		float tval = closestTTo(other);
		if (tval > 0 && tval < 1) {
			return getPointAt(tval);
		} else {
			if (start.distanceSquared(other) < getPointAt(1f).distanceSquared(other)) {
				return new Vector3f(start);
			} else {
				return getPointAt(1f);
			}
		}
	}

	public float closestDistanceInRangeSquared(Vector3f other) {
		float tval = closestTTo(other);
		if (tval > 0 && tval < 1) {
			return getPointAt(tval).distanceSquared(other);
		} else {
			return min(start.distanceSquared(other), getPointAt(1).distanceSquared(other));
		}
	}

	public float closestDistanceInRange(Vector3f other) {
		return (float) sqrt(closestDistanceInRangeSquared(other));
	}

}
