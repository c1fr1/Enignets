package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EnigUtils {
	
	/**
	 * prints out the matrix
	 * @param m
	 * matrix to print out
	 */
	public static void printMatrix(Matrix4f m) {
		String retval = "[";
		retval += m.m00() + ", ";
		retval += m.m01() + ", ";
		retval += m.m02() + ", ";
		retval += m.m03() + "]\n[";

		retval += m.m10() + ", ";
		retval += m.m11() + ", ";
		retval += m.m12() + ", ";
		retval += m.m13() + "]\n[";

		retval += m.m20() + ", ";
		retval += m.m21() + ", ";
		retval += m.m22() + ", ";
		retval += m.m23() + "]\n[";

		retval += m.m30() + ", ";
		retval += m.m31() + ", ";
		retval += m.m32() + ", ";
		retval += m.m33() + "]\n";
		System.out.println(retval);
	}

	/**
	 * find the axis of rotations based on a matrix
	 * @param mat
	 * matrix to reverse engineer
	 * @return
	 * x = rotation around x axis, same for y and z
	 */
	public static Vector3f getEulerAngles(Matrix4f mat){
		float ysin = mat.m20();
		if (ysin < -0.99999f) {
			ysin = -0.99999f;
		}else if (ysin > 0.99999f) {
			ysin = 0.99999f;
		}
		float y = (float) Math.asin(ysin);
		float ycos = (float) Math.cos(y);
		float xcos = mat.m22()/ycos;
		float xsin = -mat.m21()/ycos;
		float zcos = mat.m00()/ycos;
		float zsin = -mat.m10()/ycos;
		if (xcos < -0.99999f) {
			xcos = -0.99999f;
		}else if (xcos > 0.99999f) {
			xcos = 0.99999f;
		}
		if (zcos < -0.99999f) {
			zcos = -0.99999f;
		}else if (zcos > 0.99999f) {
			zcos = 0.99999f;
		}
		float x = (float) Math.acos(xcos);//getAngleFromTrig(xcos, xsin);
		float z = (float) Math.acos(zcos);//getAngleFromTrig(zcos, zsin);*/
		if (xsin < 0) {
			x = 2*(float) Math.PI - x;
		}
		if (zsin < 0) {
			z = 2*(float) Math.PI - z;
		}
/* 
	 if (Float.isNaN(getAngle(x))) {
			throw new RuntimeException("x NaN " + xcos);
		}
		if (Float.isNaN(getAngle(y))) {
			throw new RuntimeException("y NaN " + ycos);
		}
		if (Float.isNaN(getAngle(z))) {
			throw new RuntimeException("z NaN " + zcos);
		}
															*/
		return new Vector3f(getAngle(x), getAngle(y), getAngle(z));
	}

	/**
	 * finds the angle withing 0 and tau
	 * @param a
	 * value
	 * @return
	 * angle value
	 */
	public static float getAngle(float a) {
		float ret = a;
		while (ret < 0) {
			ret += 2*Math.PI;
		}
		while (ret > 2*Math.PI) {
			ret -= 2*Math.PI;
		}
		return ret;
	}
	
	public static void printFloatArray(float[] in) {
		System.out.print("[");
		for (float f:in) {
			System.out.print(Math.round(f * 100)/100 + ", ");
		}
		System.out.println("]");
	}
	
	public static void printIntArray(int[] in) {
		System.out.print("[");
		for (int f:in) {
			System.out.print(f + ", ");
		}
		System.out.println("]");
	}
	
	public static Vector3f resizeVector(Vector3f vec, float target) {
		return vec.mul(target/vec.length());
	}
	
	public static Vector2f resizeVector(Vector2f vec, float target) {
		return vec.mul(target/vec.length());
	}
	
	public static Vector2f resizeVector(float x, float z, float target) {
		float length = (float) Math.sqrt(x*x + z*z);
		return new Vector2f(x * target/length, z * target/length);
	}
}
/*

[cosy*cosz, -cosy*sinz, siny]
[sinx*siny*cosz + cosx*sinz, -sinx*siny*sinz+cosx*cosz, -sinx*cosy]
[-cosz*cosx*siny + sinx*sinz, cosx*siny*sinz+sinx*cosz, cosx*cosy]
[ yz,  yz,  y ]
[xyz, xyz, xy ]
[xyz, xyz, xy ]

																	*/