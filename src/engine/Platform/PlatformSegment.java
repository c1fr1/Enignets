package engine.Platform;

import engine.EnigUtils;
import engine.VAO;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class PlatformSegment {

	/**
	 * finds the offset based on the tangent plane so that distance traveled is a constant
	 * finds the magnitude of the velocity, then adds the y component, and resizes it
	 * @param xPos
	 * current x position
	 * @param zPos
	 * current z position
	 * @param xVelocity
	 * x velocity magnitude
	 * @param zVelocity
	 * y velocity magnitude
	 * @return
	 * the offset based on the tangent plane so that the distance traveled is a constant
	 */
	public Vector2f getOffset(float xPos, float zPos, float xVelocity, float zVelocity) {
		Vector3f fullVector = EnigUtils.resizeVector(new Vector3f(xVelocity, getTangentPlane(xPos, zPos).gety(xVelocity, zVelocity), zVelocity), (float) Math.sqrt(xVelocity * xVelocity + zVelocity * zVelocity));
		return new Vector2f(fullVector.x, fullVector.z);
	}

	public Vector2f getOffset(Vector2f pos, Vector2f velocity) {
		return getOffset(pos.x, pos.y, velocity.x, velocity.y);
	}

	public Vector2f getOffset(float posx, float posy, Vector2f velocity) {
		return getOffset(posx, posy, velocity.x, velocity.y);
	}

	public Vector2f getOffset(Vector2f pos, float xvelocity, float yvelocity) {
		return getOffset(pos.x, pos.y, xvelocity, yvelocity);
	}

	/**
	 * finds the height of the object on the plane given the x and z positions
	 * @param xPos
	 * x position
	 * @param zPos
	 * z position
	 * @return
	 * y value that corresponds to the x and z positions
	 */
	public abstract float getHeight(float xPos, float zPos);

	public float getHeight(Vector2f pos) {
		return getHeight(pos.x, pos.y);
	}

	/**
	 * returns the plane tangent to the platform at a given x an z position
	 * @param xPos
	 * x positon
	 * @param zPos
	 * z position
	 * @return
	 * the plane tangent to the platform at a given x and z position
	 */
	public abstract Plane getTangentPlane(float xPos, float zPos);

	public Plane getTangentPlane(Vector2f pos) {
		return getTangentPlane(pos.x, pos.y);
	}

	public VAO getSurfaceModel(float minx, float maxx, float minz, float maxz, int pointsX, int pointsY) {
		float[] xVals = new float[pointsX * pointsY];
		float[] yVals = new float[pointsX * pointsY];
		float[] zVals = new float[pointsX * pointsY];
		int pointNum = 0;
		float xRange = maxx - minx;
		float zRange = maxz - minz;
		float xSegment = xRange/((float) pointsX - 1);
		float zSegment = zRange/((float) pointsY - 1);
		for (int x = 0; x < pointsX;++x) {
			for (int y = 0; y < pointsY;++y) {
				float yPos = minz + y * zSegment;
				xVals[pointNum] = minx + x * xSegment;
				zVals[pointNum] = minz + y * zSegment;
				yVals[pointNum] = getHeight(xVals[pointNum], zVals[pointNum]);
				++pointNum;
			}
		}
		float[] combined = new float[3 * pointsX * pointsY];
		int[] indices = new int[6 * (pointsX - 1) * (pointsX - 1)];
		int indNum = 0;
		for (int i = 0; i < xVals.length;++i) {
			combined[3*i] = xVals[i];
			combined[3*i + 1] = yVals[i];
			combined[3*i + 2] = zVals[i];
			if ((i + 1) % pointsX != 0 && indNum < indices.length) {
				indices[indNum] = i;
				++indNum;
				indices[indNum] = i + 1;
				++indNum;
				indices[indNum] = pointsX + i;
				++indNum;
				indices[indNum] = i + 1;
				++indNum;
				indices[indNum] = pointsX + i + 1;
				++indNum;
				indices[indNum] = pointsX + i;
				++indNum;
			}
		}
		return new VAO(combined, indices, 3);
	}

	public VAO getSurfaceModel(Vector2f min, Vector2f max, int pointsX, int pointsY) {
		return getSurfaceModel(min.x, max.x, min.y, max.y, pointsX, pointsY);
	}
}
