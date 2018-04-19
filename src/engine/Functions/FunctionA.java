package engine.Functions;

public class FunctionA extends EnigFunction {
	
	public FunctionA() {
		variableCount = 2;
	}
	
	@Override
	public float evaluate(float[] variables) {
		//return 5f/(float) (1f + Math.pow(Math.E, variables[0]) + Math.pow(Math.E, variables[1]));
		//return 5f/(float) (1f + Math.pow(variables[0], 2));
		return (float)1f/( variables[0] * variables[1] * variables[0] * variables[1] + 1f);
	}
	
	@Override
	public float evaluateDerivative(float[] variables, int variableIndex) {
		//return  (float) (Math.pow(Math.pow(Math.E, variables[0]) + Math.pow(Math.E, variables[1]) + 1, -2) * Math.pow(Math.E, variables[variableIndex]));
		return  (float) (Math.pow(Math.pow(Math.E, variables[0]) + Math.pow(Math.E, variables[1]) + 1, -2) * 1);
	}
}
