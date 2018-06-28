package engine.Entities;

import engine.EnigUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class is not intended for static rotations
 */
public class PositionInfo extends Vector3f {
	private Matrix4f rotationMatrix;
	private Vector3f rotations;
	private float scale = 1f;
	private boolean needsUpdate = true;
	public PositionInfo() {
		rotationMatrix = new Matrix4f();
	}
	
	/**
	 * creates default info from a position
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 */
	public PositionInfo(float x, float y, float z) {
		super(x, y, z);
		rotationMatrix = new Matrix4f();
	}
	
	/**
	 * creates info from a position and rotation
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @param pitch pitch
	 * @param yaw yaw
	 * @param roll roll
	 */
	public PositionInfo(float x, float y, float z, float pitch, float yaw, float roll) {
		super(x, y, z);
		rotationMatrix = new Matrix4f().setRotationXYZ(pitch, yaw, roll);
	}
	
	/**
	 * creates info based on a position and rotation matrix
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @param rotationMatrix rotation matrix
	 */
	public PositionInfo(float x, float y, float z, Matrix4f rotationMatrix) {
		super(x, y, z);
		this.rotationMatrix = rotationMatrix;
	}
	
	/**
	 * get a vector representing the rotation of the object
	 * @return x = pitch, y = yaw, z = roll
	 */
	public Vector3f getRotations() {
		if (needsUpdate) {
			rotations = EnigUtils.getEulerAngles(rotationMatrix);
			needsUpdate = false;
		}
		return rotations;
	}
	
	/**
	 * set the current rotations
	 * @param ax pitch
	 * @param ay yaw
	 * @param az roll
	 */
	public void setRotations(float ax, float ay, float az) {
		rotationMatrix = new Matrix4f().setRotationXYZ(ax, ay, az);
		needsUpdate = true;
	}
	
	/**
	 * get the current pitch of the rotation matrix
	 * @return current pitch
	 */
	public float getPitch() {
		if (needsUpdate) {
			rotations = EnigUtils.getEulerAngles(rotationMatrix);
			needsUpdate = false;
		}
		return rotations.x;
	}
	
	/**
	 * get the current yaw of the rotation matrix
	 * @return current yaw
	 */
	public float getYaw() {
		if (needsUpdate) {
			rotations = EnigUtils.getEulerAngles(rotationMatrix);
			needsUpdate = false;
		}
		return rotations.y;
	}
	
	/**
	 * get the current roll of the rotation matrix
	 * @return current roll
	 */
	public float getRoll() {
		if (needsUpdate) {
			rotations = EnigUtils.getEulerAngles(rotationMatrix);
			needsUpdate = false;
		}
		return rotations.z;
	}
	
	/**
	 * sets the position to a vector
	 * @param npos vector representing the new position
	 */
	public void setPos(Vector3f npos) {
		x = npos.x;
		y = npos.y;
		z = npos.z;
	}
	
	/**
	 * sets the position to a vector
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
	 * adds a vector to the current position
	 * @param tpos translation
	 */
	public void translate(Vector3f tpos) {
		super.add(tpos);
	}
	
	/**
	 * adds a vector to the current position
	 * @param x x translation
	 * @param y y translation
	 * @param z z translation
	 */
	public void translate(float x, float y, float z) {
		super.add(x, y, z);
	}
	
	/**
	 * changes the pitch of the rotation matrix
	 * @param amount change of pitch
	 */
	public void pitch(float amount) {
		Matrix4f tempRotationMatrix = new Matrix4f().rotateX(amount);
		tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		needsUpdate = true;
	}
	
	/**
	 * changes the yaw of the rotation matrix
	 * @param amount change in yaw
	 */
	public void yaw(float amount) {
		Matrix4f tempRotationMatrix = new Matrix4f().rotateY(amount);
		tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		needsUpdate = true;
	}
	
	/**
	 * changes the roll of the rotation matrix
	 * @param amount change in roll
	 */
	public void roll(float amount) {
		Matrix4f tempRotationMatrix = new Matrix4f().rotateZ(amount);
		tempRotationMatrix.mul(rotationMatrix, rotationMatrix);
		needsUpdate = true;
	}
	
	/**
	 * rotates a vector by the current rotation matrix
	 * @param vector input vector
	 * @return new rotated vector
	 */
	public Vector3f rotateVector(Vector3f vector) {
		return rotateVector(vector.x, vector.y, vector.z);
	}
	
	/**
	 * rotates a vector by the current rotation matrix
	 * @param vx input x
	 * @param vy input y
	 * @param vz input z
	 * @return new rotated vector
	 */
	public Vector3f rotateVector(float vx, float vy, float vz) {
		Vector3f ret = new Vector3f(vx, vy, vz);
		if (needsUpdate) {
			rotations = EnigUtils.getEulerAngles(rotationMatrix);
			needsUpdate = false;
		}
		ret.rotateX(rotations.x);
		ret.rotateY(rotations.y);
		ret.rotateZ(rotations.z);
		return ret;
	}
	
	/**
	 * gets the current rotation matrix
	 * @return rotation matrix
	 */
	public Matrix4f getRotationMatrix() {
		return rotationMatrix;
	}
	
	/**
	 * a vector that points in the direction the rotation matrix is facing
	 * @return rotated vector
	 */
	public Vector3f getDirectionVector() {
		return rotateVector(0, 0, -1);
	}
	
	/**
	 * rotate the camera object around an axis
	 * @param angle
	 * magnitude of rotation
	 * @param axis
	 * axis of rotation
	 */
	public void rotateAlongAxis(float angle, Vector3f axis) {
		rotationMatrix.rotate(angle, axis);
		needsUpdate = true;
	}
	
	/**
	 * sets the scale of the object
	 * @param scale new scale of the object
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * change the scale by a factor
	 * @param factor factor to scale by
	 */
	public void scaleBy(float factor) {
		scale *= factor;
	}
	
	/**
	 * gets the current scale
	 * @return current scale
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * translates a camera matrix by the information in stored in this object
	 * @param mat camera matrix
	 * @return translated matrix
	 */
	public Matrix4f translateMatrix(Matrix4f mat) {
		if (needsUpdate) {
			rotations = EnigUtils.getEulerAngles(rotationMatrix);
			needsUpdate = false;
		}
		mat.translate(this);
		mat.rotateX(-rotations.x);
		mat.rotateY(-rotations.y);
		mat.rotateZ(-rotations.z);
		return mat;
	}
	
	/**
	 * WIP
	 * @param newVector
	 */
	public void setDirectionVector(Vector3f newVector) {
		needsUpdate = true;
		/*Vector3f normal = newVector.normalize(new Vector3f());
		Vector3f directionVector = new Vector3f(0f, 0f, 1f);
		Vector3f axis = directionVector.cross(normal).normalize();
		rotationMatrix = new Matrix4f().rotate((float) Math.acos(normal.dot(directionVector)), axis);
		*/
		Vector3f normal = newVector.normalize(new Vector3f());
		float yaw = (float) Math.atan2(normal.y, Math.abs(normal.z));
		float pitch = -(float) Math.atan2(normal.x, -normal.z);
		float sinx = (float) Math.sin(yaw);
		float cosx = (float) Math.cos(yaw);
		float siny = (float) Math.sin(pitch);
		float cosy = (float) Math.cos(pitch);
		//rotationMatrix = new Matrix4f().rotateY(pitch).rotateX(yaw);
		rotationMatrix = rotationMatrix.setLookAlong(newVector.x, newVector.y, newVector.z, 0f, 1f, 0f);
		//rotationMatrix.lookat
		//rotationMatrix = rotationMatrix.setLookAlong(0f, 0f, 1f, 0f, 1f, 0f);
	}
	
	/**
	 * WIP
	 * @param pos
	 */
	public void setLookAt(Vector3f pos) {
		needsUpdate = true;
		//rotationMatrix.setLookAt(new Vector3f().sub(this), new Vector3f().sub(pos), new Vector3f(0f, 1f, 0f));
		setDirectionVector(new Vector3f((pos.x-x), (pos.y-y), (pos.z-z)));
		
	}
	
}
