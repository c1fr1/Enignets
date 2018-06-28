package engine.Platform;

import engine.Functions.EnigFunction;
import org.joml.Vector3f;

public class FunctionPlatform extends PlatformSegment {

	public EnigFunction func;
	
	/**
	 * creates a new platform that follows the height of a function
	 * @param function function that represents the height of the platform
	 */
	public FunctionPlatform(EnigFunction function) {
		func = function;
	}
	
	/**
	 * returns the plane tangent to the platform at a given x an z position
	 * @param xPos x positon
	 * @param zPos z position
	 * @return the plane tangent to the platform at the position
	 */
	@Override
	public Simplex2v3d getTangentPlane(float xPos, float zPos) {
		return new Simplex2v3d(func.evaluateDerivative(new float[] {xPos, zPos}, 0), func.evaluateDerivative(new float[] {xPos, zPos}, 1), new Vector3f(xPos, getHeight(xPos, zPos), zPos));
	}
	
	/**
	 * finds the height of the object on the plane given the x and z positions
	 * @param xPos x position
	 * @param zPos z position
	 * @return y value that corresponds to the x and z positions
	 */
	@Override
	public float getHeight(float xPos, float zPos) {
		//return getTangentPlane(xPos, zPos).gety(xPos, zPos);
		return func.evaluate(new float[] {xPos, zPos});
	}
}
