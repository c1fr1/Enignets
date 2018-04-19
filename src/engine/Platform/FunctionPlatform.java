package engine.Platform;

import engine.Functions.EnigFunction;

public class FunctionPlatform extends PlatformSegment {

	public EnigFunction func;
	
	public FunctionPlatform(EnigFunction function) {
		func = function;
	}

	@Override
	public Plane getTangentPlane(float xPos, float zPos) {
		return new Plane(func.evaluateDerivative(new float[] {xPos, zPos}, 0), func.evaluateDerivative(new float[] {xPos, zPos}, 1));
	}

	@Override
	public float getHeight(float xPos, float zPos) {
		return func.evaluate(new float[] {xPos, zPos});
	}
}
