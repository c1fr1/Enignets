package engine.Functions;

public class Polynomial2D extends EnigFunction {

	public float[][] coefficients;
	public float[][] nCoefficients;
	public float[][] npCoefficients;
	public float[][] pnCoefficients;

	public Polynomial2D(float[][] c, float[][] nc, float[][] npc, float[][] pnc) {
		coefficients = c;
		nCoefficients = nc;
		npCoefficients = npc;
		pnCoefficients = pnc;
	}

	@Override
	public float evaluate(float[] variables) {
		float ret = 0f;
		for (int x = 0; x < coefficients.length;++x) {
			for (int y = 0; y < coefficients[0].length;++y) {
				ret += coefficients[x][y] * Math.pow(variables[0], x) * Math.pow(variables[1], y);
			}
		}
		for (int x = 0; x < nCoefficients.length;++x) {
			for (int y = 0; y < nCoefficients[0].length;++y) {
				ret += nCoefficients[x][y] * Math.pow(variables[0], -x) * Math.pow(variables[1], -y);
			}
		}
		for (int x = 0; x < npCoefficients.length;++x) {
			for (int y = 0; y < npCoefficients[0].length;++y) {
				ret += npCoefficients[x][y] * Math.pow(variables[0], -x) * Math.pow(variables[1], y);
			}
		}
		for (int x = 0; x < pnCoefficients.length;++x) {
			for (int y = 0; y < pnCoefficients[0].length;++y) {
				ret += pnCoefficients[x][y] * Math.pow(variables[0], x) * Math.pow(variables[1], -y);
			}
		}
		return ret;
	}

	@Override
	public float evaluateDerivative(float[] variables, int variableIndex) {
		float ret = 0f;
		if (variableIndex == 0) {
			for (int x = 0; x < coefficients.length;++x) {
				for (int y = 0; y < coefficients[0].length;++y) {
					ret += ((float) (x)) * coefficients[x][y] * Math.pow(variables[0], x - 1) * Math.pow(variables[1], y);
				}
			}
			for (int x = 0; x < nCoefficients.length;++x) {
				for (int y = 0; y < nCoefficients[0].length;++y) {
					ret += ((float) (x)) * nCoefficients[x][y] * Math.pow(variables[0], -x - 1) * Math.pow(variables[1], -y);
				}
			}
			for (int x = 0; x < npCoefficients.length;++x) {
				for (int y = 0; y < npCoefficients[0].length;++y) {
					ret += ((float) (x)) * npCoefficients[x][y] * Math.pow(variables[0], -x - 1) * Math.pow(variables[1], y);
				}
			}
			for (int x = 0; x < pnCoefficients.length;++x) {
				for (int y = 0; y < pnCoefficients[0].length;++y) {
					ret += ((float) (x)) * pnCoefficients[x][y] * Math.pow(variables[0], x - 1) * Math.pow(variables[1], -y);
				}
			}
		}else {
			for (int x = 0; x < coefficients.length;++x) {
				for (int y = 0; y < coefficients[0].length;++y) {
					ret += ((float) (y)) * coefficients[x][y] * Math.pow(variables[0], x) * Math.pow(variables[1], y - 1);
				}
			}
			for (int x = 0; x < nCoefficients.length;++x) {
				for (int y = 0; y < nCoefficients[0].length;++y) {
					ret += ((float) (y)) * nCoefficients[x][y] * Math.pow(variables[0], -x) * Math.pow(variables[1], -y - 1);
				}
			}
			for (int x = 0; x < npCoefficients.length;++x) {
				for (int y = 0; y < npCoefficients[0].length;++y) {
					ret += ((float) (y)) * npCoefficients[x][y] * Math.pow(variables[0], -x) * Math.pow(variables[1], y - 1);
				}
			}
			for (int x = 0; x < pnCoefficients.length;++x) {
				for (int y = 0; y < pnCoefficients[0].length;++y) {
					ret += ((float) (y)) * pnCoefficients[x][y] * Math.pow(variables[0], x) * Math.pow(variables[1], -y - 1);
				}
			}
		}
		return ret;
	}
}
