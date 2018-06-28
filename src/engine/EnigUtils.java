package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EnigUtils {
	
	/**
	 * prints out the matrix
	 * @param m matrix to print out
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
	 * @param mat matrix to reverse engineer
	 * @return xpos = rotation around xpos axis, same for ypos and z
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
		float x = (float) Math.acos(xcos);
		float z = (float) Math.acos(zcos);
		if (xsin < 0) {
			x = 2*(float) Math.PI - x;
		}
		if (zsin < 0) {
			z = 2*(float) Math.PI - z;
		}
		return new Vector3f(getAngle(x), getAngle(y), getAngle(z));
	}
	
	/**
	 * finds the angle withing 0 and tau
	 * @param a value
	 * @return angle value
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
	
	/**
	 * prints out an array of floats
	 * @param in array of floats
	 */
	public static void printFloatArray(float[] in) {
		System.out.print("[");
		for (float f:in) {
			System.out.print(f + ", ");
		}
		System.out.println("]");
	}
	
	/**
	 * prints out an integer array
	 * @param in
	 */
	public static void printIntArray(int[] in) {
		System.out.print("[");
		for (int f:in) {
			System.out.print(f + ", ");
		}
		System.out.println("]");
	}
	
	/**
	 * changes the size of a vector to a new value
	 * @param vec vector to change
	 * @param target target size
	 * @return the vector
	 */
	public static Vector3f resizeVector(Vector3f vec, float target) {
		return vec.mul(target/vec.length());
	}
	
	/**
	 * changes the size of a vector to a new value
	 * @param vec vector to change
	 * @param target target size
	 * @return the vector
	 */
	public static Vector2f resizeVector(Vector2f vec, float target) {
		return vec.mul(target/vec.length());
	}
	
	/**
	 * creates a vector with a target size proportional to a current size
	 * @param x x value of the vector
	 * @param y y value of the vector
	 * @param target target size
	 * @return new vector
	 */
	public static Vector2f resizeVector(float x, float y, float target) {
		float length = (float) Math.sqrt(x*x + y*y);
		return new Vector2f(x * target/length, y * target/length);
	}
	
	/**
	 * checks to see if an array of x and y coordinates has a certain x and y
	 * @param arrx array of x coordinates
	 * @param arry array of y coordinates
	 * @param tx target x
	 * @param ty target y
	 * @return index of the coordinates, -1 if it doesn't have it
	 */
	public static int containsPoint(int[] arrx, int[] arry, int tx, int ty) {
		for (int i = 0; i < arrx.length;++i) {
			if (arrx[i] == tx && arry[i] == ty) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * adds two arrays to a larger array
	 * @param a first array
	 * @param b second array
	 * @return new array
	 */
	public static int[] addArrays(int[] a, int[] b) {
		int[] ret = new int[a.length + b.length];
		for (int i = 0; i < a.length;i++) {
			ret[i] = a[i];
		}
		for (int i = 0; i < b.length;i++) {
			ret[i + a.length] = b[i];
		}
		return ret;
	}
	
	/**
	 * rounds a float to a certain amount of decimal places
	 * @param in original float
	 * @param decPlaces number of decimal places
	 * @return new float
	 */
	public static float round(float in, int decPlaces) {
		float factor = (float) Math.pow(10, decPlaces);
		return Math.round(in * factor) / factor;
	}
	
	/**
	 * rounds a double to a certain amount of decimal places
	 * @param in original double
	 * @param decPlaces number of decimal places
	 * @return new double
	 */
	public static double round(double in, int decPlaces) {
		double factor = Math.pow(10, decPlaces);
		return Math.round(in * factor) / (factor);
	}
	
	/**
	 * formats a float so that it fits into a certain amount of characters asa string
	 * @param in original float
	 * @param room number of characters
	 * @return string representing the float input
	 */
	public static String format(float in, int room) {
		String ret = in < 0 ? "" + round((double) in, room - 1) : "" + round((double) in, room);
		while (ret.length() < room + 1) {
			if (ret.contains(".")) {
				ret += "0";
			}else {
				ret += ".";
			}
		}
		while (ret.length() > room + 1) {
			ret = ret.substring(0, ret.length() - 1);
		}
		if (ret.endsWith(".")) {
			ret = ret.substring(0, ret.length() - 1) + " ";
		}
		return ret;
	}
}
/*

[cosy*cosz, -cosy*sinz, siny]
[sinx*siny*cosz + cosx*sinz, -sinx*siny*sinz+cosx*cosz, -sinx*cosy]
[-cosz*cosx*siny + sinx*sinz, cosx*siny*sinz+sinx*cosz, cosx*cosy]
[ yz,  yz,  ypos ]
[xyz, xyz, xy ]
[xyz, xyz, xy ]

																	*/