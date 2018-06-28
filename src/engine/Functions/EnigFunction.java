package engine.Functions;

public abstract class EnigFunction {

	int variableCount;
	
	/**
	 * returns the value given the input variables
	 * @param variables input variables
	 * @return the value given the input variables
	 */
	public abstract float evaluate(float[] variables);
	
	/**
	 * evaluate the partial derivative of the function given the input variables
	 * @param variables input variables
	 * @param variableIndex variable that you are taking a partial derivative
	 * @return partial derivative given the input
	 */
	public abstract float evaluateDerivative(float[] variables, int variableIndex);
}


/*


I would like to make a recursive version of this so
that the user can input equations from a string and it
will use sin/cos etc, and you could also take nth
derivatives... blah blah blah for now you have to make
new classes on a case-by-care basis. this might also
be way too expensive to be practical.


														 */