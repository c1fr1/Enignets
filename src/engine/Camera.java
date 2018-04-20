package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {
	public Matrix4f projectionMatrix;
	public Matrix4f rotationMatrix;

	private Vector3f pos = new Vector3f(0f, 0f, 0f);

	public Vector3f calculatedRotations = new Vector3f();

	private Vector4f directionVector = new Vector4f(0f, 0f, 1f, 1f);

	public boolean usingStaticRotation = true;

	private float pitch;
	private float yaw;
	private float roll;

	public int[] orderOfTransformations = new int[] {0, 1, 2};//0 = projection matrix, 1 = rotation matrix, 2 = translation matrix;

	/**
	 * Creates a new Camera object
	 * @param fieldOfView
	 * Field of view (75 is a good place to start, maybe 70)
	 * @param minDistance
	 * minimum render distance
	 * @param renderDistance
	 * maximum render distance
	 */
	public Camera(float fieldOfView, float minDistance, float renderDistance) {//70, 0.1, 1000
		//creating projection matrix
		float aspectRatio = (float) EnigWindow.width / (float) EnigWindow.height;
		projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, minDistance, renderDistance);
		rotationMatrix = new Matrix4f();
		setPos(0f, 0f, 0f);
		
	}

	/**
	 * Creates a new Camera object
	 * @param fieldOfView
	 * Field of view ( 70 is a good place to start, maybe 75)
	 * @param minDistance
	 * minimum render distance
	 * @param renderDistance
	 * maximum render distance
	 * @param first
	 * first matrix in the camera matrix product
	 * @param second
	 * second matrix in the camera matrix product
	 * @param third
	 * third matrix in the camera matrix product
	 */
	public Camera(float fieldOfView, float minDistance, float renderDistance, int first, int second, int third) {
		//creating projection matrix
		float aspectRatio = (float) EnigWindow.width / (float) EnigWindow.height;
		projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, minDistance, renderDistance);
		rotationMatrix= new Matrix4f();
		orderOfTransformations = new int[] {first, second, third};
		setPos(0f, 0f, 0f);
	}

	public Vector3f getPos() {
		return new Vector3f(pos);
	}

	public void setPos(Vector3f npos) {
		pos = npos;
	}

	public void setPos(float x, float y, float z) {
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}
	
	public void translate(Vector3f tpos) {
		pos.x += tpos.x;
		pos.y += tpos.y;
		pos.z += tpos.z;
	}

	public void translate(float x, float y, float z) {
		pos.x += x;
		pos.y += y;
		pos.z += z;
	}

	/**
	 * changes how far up/down the camera is looking
	 * @param v
	 * offset
	 */
	public void pitch(float v) {
		if (usingStaticRotation) {
			pitch += v;//up down
			rotationMatrix.setRotationXYZ(pitch, yaw, roll);
		} else {
			Matrix4f tempRotationMatrix = new Matrix4f().rotateX(v);
			directionVector.rotateX(v);
			tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		}
	}
	/**
	 * changes how far left/right the camera is looking
	 * @param v
	 * offset
	 */
	public void yaw(float v) {
		if (usingStaticRotation) {
			yaw += v; // left right
			rotationMatrix.setRotationXYZ(pitch, yaw, roll);
		}else {
			Matrix4f tempRotationMatrix = new Matrix4f().rotateY(v);
			directionVector.rotateY(v);
			tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		}
	}

	/**
	 * changes how twisted the camera is
	 * @param v
	 * offset
	 */
	public void roll(float v) {
		if (usingStaticRotation) {
			roll += v; // do a barrel roll
			rotationMatrix.setRotationXYZ(pitch, yaw, roll);
		}else {
			Matrix4f tempRotationMatrix = new Matrix4f().rotateZ(v);
			directionVector.rotateZ(v);
			tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		}
	}

	public Vector4f getDirectionVector() {
		return directionVector;
	}

	/**
	 * get the camera matrix that will apply transformations to individual vectors
	 * @return
	 * the camera matrix based on perspective, rotation, and translation
	 */
	public Matrix4f getCameraMatrix() {
		Matrix4f retVal = new Matrix4f();
		for (int m:orderOfTransformations) {
			if (m == 0) {
				retVal = retVal.mul(projectionMatrix);
			}else if (m==1) {
				retVal = retVal.mul(rotationMatrix);
			}else if (m==2) {
				retVal = retVal.translate(pos);
			}
		}
		return retVal;
	}

	/**
	 * finds the angles of rotation based on the current rotation matrix
	 * @return
	 * x = pitch, y = yaw, z = roll
	 */
	public Vector3f angles() {
		float sy = (float) Math.sqrt(rotationMatrix.m00()*rotationMatrix.m00()+rotationMatrix.m10()*rotationMatrix.m10());

		boolean singular = sy < 1e-6; // If

		float x, y, z;
		if (!singular) {
			x = (float) Math.atan2(rotationMatrix.m21(), rotationMatrix.m22());
			y = (float) Math.atan2(-rotationMatrix.m20(), sy);
			z = (float) Math.atan2(rotationMatrix.m10(), rotationMatrix.m00());
		}
		else {
			x = (float) Math.atan2(-rotationMatrix.m12(), rotationMatrix.m11());
			y = (float) Math.atan2(-rotationMatrix.m20(), sy);
			z = 0;
		}
		return new Vector3f(x, y, z);
	}

	public float getPitch() {
		if (usingStaticRotation) {
			return pitch;
		}else {
			return calculatedRotations.x;
		}
	}

	public float getYaw() {
		if (usingStaticRotation) {
			return yaw;
		}else {
			return calculatedRotations.y;
		}
	}

	public float getRoll() {
		if (usingStaticRotation) {
			return roll;
		}else {
			return calculatedRotations.z;
		}
	}

	/**
	 * sets the x rotation to something specific, if the camera doesn't use static rotations, then it simply rotates by v
	 * @param v
	 * new value
	 */
	public void setPitch(float v) {
		if (usingStaticRotation) {
			pitch = v;//up down
			rotationMatrix.setRotationXYZ(pitch, yaw, roll);
		}else {
			Matrix4f tempRotationMatrix = new Matrix4f().rotateX(v);
			directionVector.rotateX(v);
			tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		}
	}

	/**
	 * sets the y rotation to something specific, if the camera doesn't use static rotations, then it simply rotates by v
	 * @param v
	 * new value
	 */
	public void setYaw(float v) {
		if (usingStaticRotation) {
			yaw = v;//up down
			rotationMatrix.setRotationXYZ(pitch, yaw, roll);
		}else {
			Matrix4f tempRotationMatrix = new Matrix4f().rotateY(v);
			directionVector.rotateY(v);
			tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		}
	}

	/**
	 * sets the z rotation to something specific, if the camera doesn't use static rotations, then it simply rotates by v
	 * @param v
	 * new value
	 */
	public void setRoll(float v) {
		if (usingStaticRotation) {
			roll = v;//up down
			rotationMatrix.setRotationXYZ(pitch, yaw, roll);
		}else {
			Matrix4f tempRotationMatrix = new Matrix4f().rotateZ(v);
			directionVector.rotateZ(v);
			tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		}
	}

	public void updateRotations() {
		calculatedRotations = EnigUtils.getEulerAngles(rotationMatrix);
	}
	
	public Vector3f getRotatedVector(float x, float y, float z, float s) {
		return new Vector3f(x, y, z).rotateX(-2*pitch).rotateY(-2*yaw).rotateZ(-2*roll).mul(s);
	}
	
	public Vector2f getRotated2DVector(float x, float z, float s) {
		Vector3f ret = new Vector3f(x, 0f, z).rotateY(-2*yaw);
		return EnigUtils.resizeVector(ret.x, ret.z, s);
	}
}
/*
//[cosy*cosz, -cosy*sinz, siny]
//[sinx*siny*cosz + cosx*sinz, -sinx*siny*sinz+cosx*cosz, -sinx*cosy]
//[-cosz*cosx*siny + sinx*sinz, cosx*siny*sinz+sinx*cosz, cosx*cosy]

//[y2*z2, -y2*z1, y1]
//[x1*y1*z2 + x2*z1, -x1*y1*z1 + x2*z2, -x1*y2]
//[-x2*y1*z2 + x1*z1, x2*y1*z1 + x1*z2, x2*y2]

//x: cos:

//(a + b)(c + d)
//ac + ad + bc + bd

//(sinx*siny*cosz + cosx*sinz)(-cosz*cosx*siny + sinx*sinz)
//---------------------------------------------------------
//(cosx*siny*sinz + sinx*cosz)(-sinx*siny*sinz + cosx*cosz)

//-sinx*siny^2*cosz^2*cosx + sinx^2*siny*cosz*sinz + -cosx^2*sinz*cosz*siny + cosx*sinz^2*sinx
																							   */