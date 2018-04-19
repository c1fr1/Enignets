package engine.Functions;

public class MountainFunction extends EnigFunction {
	private float xPosition;
	private float zPosition;
	private float[] pos;
	private float height;
	public MountainFunction(float x, float z, float h) {
		xPosition = x;
		zPosition = z;
		pos = new float[] {xPosition, zPosition};
		height = h;
		variableCount = 2;
	}
	@Override
	public float evaluate(float[] variables) {
		return (float) (height/(Math.pow(Math.E, variables[0] - xPosition) + Math.pow(Math.E, -variables[0] + xPosition) + Math.pow(Math.E, variables[1] - zPosition) + Math.pow(Math.E, -variables[1] + zPosition) - 3));
	}
	
	@Override
	public float evaluateDerivative(float[] variables, int variableIndex) {
		return (float) (height * Math.pow((Math.pow(Math.E, variables[0] - xPosition) + Math.pow(Math.E, -variables[0] + xPosition) + Math.pow(Math.E, variables[1] - zPosition) + Math.pow(Math.E, -variables[1] + zPosition)) - 3, -2) * (Math.pow(Math.E, variables[variableIndex] - pos[variableIndex]) + Math.pow(Math.E, -variables[variableIndex] + pos[variableIndex])));
	}
}
