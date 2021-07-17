package engine.functions

@Suppress("unused")
abstract class EnigFunction {
	var variableCount = 0

	/**
	 * returns the value given the input variables
	 * @param variables input variables
	 * @return the value given the input variables
	 */
	abstract fun evaluate(variables: FloatArray?): Float

	/**
	 * evaluate the partial derivative of the function given the input variables
	 * @param variables input variables
	 * @param variableIndex variable that you are taking a partial derivative
	 * @return partial derivative given the input
	 */
	abstract fun evaluateDerivative(variables: FloatArray?, variableIndex: Int): Float
}