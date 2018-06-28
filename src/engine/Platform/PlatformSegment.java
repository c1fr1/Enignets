package engine.Platform;

import engine.EnigUtils;
import engine.OpenGL.VAO;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class PlatformSegment {

	/**
	 * finds the offsetY based on the tangent plane so that distance traveled is a constant
	 * finds the magnitude of the velocity, then adds the y component, and resizes it
	 * @param xPos current x position
	 * @param zPos current z position
	 * @param xVelocity x velocity magnitude
	 * @param zVelocity y velocity magnitude
	 * @return the offsetY based on the tangent plane so that the distance traveled is a constant
	 */
	public Vector2f getOffset(float xPos, float zPos, float xVelocity, float zVelocity) {
		Simplex2v3d tangentPlane = getTangentPlane(xPos, zPos);
		Vector3f unscaledOffset = new Vector3f(xVelocity, tangentPlane.gety(xVelocity, zVelocity), zVelocity);
		unscaledOffset.y -= tangentPlane.gety(0f, 0f);
		Vector3f fullVector = EnigUtils.resizeVector(unscaledOffset, (float) Math.sqrt(xVelocity * xVelocity + zVelocity * zVelocity));
		Vector2f ret = new Vector2f(fullVector.x, fullVector.z);
		return ret;
	}
	
	/**
	 * gets the offset that would have a magnitude similar to the original when including the y component
	 * @param pos position to find the tangent plane
	 * @param velocity original velocity for the output vector
	 * @return new position offset
	 */
	public Vector2f getOffset(Vector2f pos, Vector2f velocity) {
		return getOffset(pos.x, pos.y, velocity.x, velocity.y);
	}
	
	/**
	 * gets the offset that would have a magnitude similar to the original when including the y component
	 * @param posx x position to find the tangent plane
	 * @param posy y position to find the tangent plane
	 * @param velocity original velocity for the output vector
	 * @return new position offset
	 */
	public Vector2f getOffset(float posx, float posy, Vector2f velocity) {
		return getOffset(posx, posy, velocity.x, velocity.y);
	}
	
	/**
	 * gets the offset that would have a magnitude similar to the original when including the y component
	 * @param pos position to find the tangent plane
	 * @param xvelocity x component of the original velocity for the output vector
	 * @param yvelocity y component of the original velocity for the output vector
	 * @return new position offst
	 */
	public Vector2f getOffset(Vector2f pos, float xvelocity, float yvelocity) {
		return getOffset(pos.x, pos.y, xvelocity, yvelocity);
	}

	/**
	 * finds the height of the object on the plane given the x and z positions
	 * @param xPos x position
	 * @param zPos z position
	 * @return y value that corresponds to the x and z positions
	 */
	public abstract float getHeight(float xPos, float zPos);

	public float getHeight(Vector2f pos) {
		return getHeight(pos.x, pos.y);
	}

	/**
	 * returns the plane tangent to the platform at a given x an z position
	 * @param xPos x positon
	 * @param zPos z position
	 * @return the plane tangent to the platform at the position
	 */
	public abstract Simplex2v3d getTangentPlane(float xPos, float zPos);
	
	/**
	 * returns the plane tangent to the platform at a given x and z position
	 * @param pos position (y = z)
	 * @return the plane tangent to the platform at the position
	 */
	public Simplex2v3d getTangentPlane(Vector2f pos) {
		return getTangentPlane(pos.x, pos.y);
	}
	
	/**
	 * uses heights to find a grid of points and creates a VAO based off of that
	 * @param minx minimum x for the grid
	 * @param maxx maximum x for the grid
	 * @param minz minimum z for the grid
	 * @param maxz maximum z for the grid
	 * @param pointsX number of x samples
	 * @param pointsZ number of z samples
	 * @return VAO that represents the platform
	 */
	public VAO getSurfaceModel(float minx, float maxx, float minz, float maxz, int pointsX, int pointsZ) {
		float[] xVals = new float[pointsX * pointsZ];
		float[] yVals = new float[pointsX * pointsZ];
		float[] zVals = new float[pointsX * pointsZ];
		int pointNum = 0;
		float xRange = maxx - minx;
		float zRange = maxz - minz;
		float xSegment = xRange/((float) pointsX - 1);
		float zSegment = zRange/((float) pointsZ - 1);
		for (int x = 0; x < pointsX;++x) {
			for (int y = 0; y < pointsZ;++y) {
				float yPos = minz + y * zSegment;
				xVals[pointNum] = minx + x * xSegment;
				zVals[pointNum] = minz + y * zSegment;
				yVals[pointNum] = getHeight(xVals[pointNum], zVals[pointNum]);
				++pointNum;
			}
		}
		float[] combined = new float[3 * pointsX * pointsZ];
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
	
	/**
	 * uses heights to find a grid of points and creates a VAO based off of that
	 * @param a first point
	 * @param b point in the opposing corner
	 * @param pointsX number of x samples
	 * @param pointsZ number of z samples
	 * @return VAO that represents the platform
	 */
	public VAO getSurfaceModel(Vector2f a, Vector2f b, int pointsX, int pointsZ) {
		return getSurfaceModel(Math.min(a.x, b.x), Math.max(a.x, b.x), Math.min(a.y, b.y), Math.max(a.y, b.y), pointsX, pointsZ);
	}
}
