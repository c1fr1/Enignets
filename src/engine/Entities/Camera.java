package engine.Entities;

import engine.EnigUtils;
import engine.OpenGL.EnigWindow;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera extends Vector3f {
	public Matrix4f projectionMatrix;
	public Matrix4f rotationMatrix;
	
	public Vector3f calculatedRotations = new Vector3f();
	
	private Vector4f directionVector = new Vector4f(0f, 0f, 1f, 1f);
	
	public boolean usingStaticRotation = true;
	
	private float pitch;
	private float yaw;
	private float roll;
	
	public int[] orderOfTransformations = new int[] {0, 1, 2};//0 = projection matrix, 1 = rotation matrix, 2 = translation matrix;
	
	/**
	 * Creates a new Camera object
	 * @param fieldOfView field of view 0.25*pi, is standard
	 * @param minDistance minimum fullRender distance
	 * @param renderDistance maximum fullRender distance
	 */
	public Camera(float fieldOfView, float minDistance, float renderDistance) {//70, 0.1, 1000
		//creating projection matrix
		float aspectRatio = (float) EnigWindow.mainWindow.getWidth() / (float) EnigWindow.mainWindow.getHeight();
		projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, minDistance, renderDistance);
		rotationMatrix = new Matrix4f();
		setPos(0f, 0f, 0f);
	}
	
	/**
	 * Creates a new Camera object
	 * @param fieldOfView field of view, 0.25*pi is standard
	 * @param minDistance minimum fullRender distance
	 * @param renderDistance maximum fullRender distance
	 * @param window window that can be used to form the aspect ratio
	 */
	public Camera(float fieldOfView, float minDistance, float renderDistance, EnigWindow window) {//70, 0.1, 1000
		//creating projection matrix
		float aspectRatio = (float) window.getWidth() / (float) window.getHeight();
		projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, minDistance, renderDistance);
		rotationMatrix = new Matrix4f();
		setPos(0f, 0f, 0f);
	}
	
	/**
	 * Creates a new Camera object
	 * @param fieldOfView field of view, 0.25*pi is standard
	 * @param minDistance minimum fullRender distance
	 * @param renderDistance maximum fullRender distance
	 * @param first first matrix in the camera matrix product
	 * @param second second matrix in the camera matrix product
	 * @param third third matrix in the camera matrix product
	 */
	public Camera(float fieldOfView, float minDistance, float renderDistance, int first, int second, int third) {
		//creating projection matrix
		float aspectRatio = (float) EnigWindow.mainWindow.getWidth() / (float) EnigWindow.mainWindow.getHeight();
		projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, minDistance, renderDistance);
		rotationMatrix= new Matrix4f();
		orderOfTransformations = new int[] {first, second, third};
		setPos(0f, 0f, 0f);
	}
	
	/**
	 * Creates a new Camera object
	 * @param fieldOfView field of view, 0.25*pi is standard
	 * @param minDistance minimum fullRender distance
	 * @param renderDistance maximum fullRender distance
	 * @param window window that can be used to form the aspect ratio
	 * @param first first matrix in the camera matrix product
	 * @param second second matrix in the camera matrix product
	 * @param third third matrix in the camera matrix product
	 */
	public Camera(float fieldOfView, float minDistance, float renderDistance, EnigWindow window, int first, int second, int third) {
		//creating projection matrix
		float aspectRatio = (float) window.getWidth() / (float) window.getHeight();
		projectionMatrix = new Matrix4f().perspective(fieldOfView, aspectRatio, minDistance, renderDistance);
		rotationMatrix= new Matrix4f();
		orderOfTransformations = new int[] {first, second, third};
		setPos(0f, 0f, 0f);
	}
	
	/**
	 * creates a new camera with an orthographic perspective matrix
	 * @param minDistance minimum render distance
	 * @param farDistance maximum render distance
	 */
	public Camera(float minDistance, float farDistance) {
		projectionMatrix = new Matrix4f().ortho(-1f, 1, -1f, 1, minDistance, farDistance);
		rotationMatrix= new Matrix4f();
		setPos(0f, 0f, 0f);
	}
	
	/**
	 * returns a copy of the position
	 * @return copy of this position
	 */
	public Vector3f getPos() {
		return new Vector3f(this);
	}
	
	/**
	 * changes the perspective matrix to fit the new requirements
	 * @param fov field of view, 0.25*pi is standard
	 * @param minDistance minimum render distance
	 * @param renderDistance maximum render distance
	 */
	public void setPerspective(float fov, float minDistance, float renderDistance) {
		float aspectRatio = (float) EnigWindow.mainWindow.getWidth() / (float) EnigWindow.mainWindow.getHeight();
		projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, minDistance, renderDistance);
	}
	
	/**
	 * sets the position of the camera
	 * @param npos new position
	 */
	public void setPos(Vector3f npos) {
		x = npos.x;
		y = npos.y;
		z = npos.z;
	}
	
	/**
	 * sets the position of the camera
	 * @param x new x
	 * @param y new y
	 * @param z new z
	 */
	public void setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * adds the given vector to the cameras position
	 * @param tpos translation
	 */
	public void translate(Vector3f tpos) {
		super.add(tpos);
	}
	
	/**
	 * adds the given vector to the cameras position
	 * @param x x offset
	 * @param y y offset
	 * @param z z offset
	 */
	public void translate(float x, float y, float z) {
		super.add(x, y, z);
	}
	
	/**
	 * changes how far up/down the camera is looking
	 * @param v offset
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
	 * @param v offset
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
	 * @param v offset
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
	 * @return the camera matrix based on perspective, rotation, and translation
	 */
	public Matrix4f getCameraMatrix() {
		Matrix4f retVal = new Matrix4f();
		for (int m:orderOfTransformations) {
			if (m == 0) {
				retVal = retVal.mul(projectionMatrix);
			}else if (m==1) {
				retVal = retVal.mul(rotationMatrix);
			}else if (m==2) {
				retVal = retVal.translate(-this.x, -this.y, -this.z);
			}
		}
		return retVal;
	}
	
	/**
	 * finds the angles of rotation based on the current rotation matrix
	 * @return x = pitch, y = yaw, z = roll
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
	
	/**
	 * fetches current pitch of the camera
	 * @return current pitch
	 */
	public float getPitch() {
		if (usingStaticRotation) {
			return pitch;
		}else {
			return calculatedRotations.x;
		}
	}
	
	/**
	 * fetches current yaw of the camera
	 * @return current yaw
	 */
	public float getYaw() {
		if (usingStaticRotation) {
			return yaw;
		}else {
			return calculatedRotations.y;
		}
	}
	
	/**
	 * fetches current roll of the camera
	 * @return current roll
	 */
	public float getRoll() {
		if (usingStaticRotation) {
			return roll;
		}else {
			return calculatedRotations.z;
		}
	}
	
	/**
	 * sets the xpos rotation to something specific, if the camera doesn't use static rotations, then it simply rotates by v
	 * @param v new value
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
	 * sets the ypos rotation to something specific, if the camera doesn't use static rotations, then it simply rotates by v
	 * @param v new value
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
	 * @param v new value
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
	
	/**
	 * updates the rotation vector that is stored on the camera
	 */
	public void updateRotations() {
		calculatedRotations = EnigUtils.getEulerAngles(rotationMatrix);
	}
	
	/**
	 * rotates a given vector by the rotation matrix of the camera, then scales it by a factor
	 * @param x x position of the vector
	 * @param y y position of the vector
	 * @param z z position of the vector
	 * @param s scale
	 * @return new rotated vector
	 */
	public Vector3f getRotatedVector(float x, float y, float z, float s) {
		return new Vector3f(x, y, z).rotateX(-2*pitch).rotateY(-2*yaw).rotateZ(-2*roll).mul(s);
	}
	
	/**
	 * rotates a 2d vector by the yaw of the camera, then sets the magnitude to a scale
	 * @param x x position
	 * @param z y position
	 * @param s size of the output vector
	 * @return new rotated vector
	 */
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

//xpos: cos:

//(a + b)(c + d)
//ac + ad + bc + bd

//(sinx*siny*cosz + cosx*sinz)(-cosz*cosx*siny + sinx*sinz)
//---------------------------------------------------------
//(cosx*siny*sinz + sinx*cosz)(-sinx*siny*sinz + cosx*cosz)

//-sinx*siny^2*cosz^2*cosx + sinx^2*siny*cosz*sinz + -cosx^2*sinz*cosz*siny + cosx*sinz^2*sinx
																							   */