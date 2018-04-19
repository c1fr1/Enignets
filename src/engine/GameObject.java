package engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObject {
	public VAO model;
	
	public Vector3f translation = new Vector3f(0f, 0f, 0f);
	public Vector3f rotation = new Vector3f(0f, 0f, 0f);
	
	public float scale = 1;

	public GameObject(VAO vao) {
		model = vao;
	}
	
	public GameObject(String path) {
		model = OBJLoader.loadVAO(path);
	}

	/**
	 * render the model
	 * @param cam
	 * player matrix
	 */
	public void render(Camera cam) {
		for (Uniform uni:Program.currentShaderProgram.shaders[0].uniforms) {
			if (uni.getName().equals("matrix")) {
				uni.set(cam.getCameraMatrix().mul(translationMatrix()));
			}
		}
		model.render();
	}
	
	public Matrix4f translationMatrix() {
		return new Matrix4f().translate(translation).rotateZ(rotation.z).rotateY(rotation.y).rotateX(rotation.x).scale(scale);
	}
	
	public void translate(float x, float y, float z) {
		translation.x += x;
		translation.y += y;
		translation.z += z;
	}
	
	public void translate(Vector3f dir) {
		translation.x += dir.x;
		translation.y += dir.y;
		translation.z += dir.z;
	}
	
	public void translateX(float v) {
		translation.x += v;
	}
	
	public void translateY(float v) {
		translation.y += v;
	}
	
	public void translateZ(float v) {
		translation.z += v;
	}
	
	public Vector3f getPos() {
		return new Vector3f(-translation.x, -translation.y, -translation.z);
	}

	/**
	 * returns a normalized vector pointing in the direction of the rotation of the object
	 * @return
	 */
	public Vector3f getRotationVector() {
		Vector3f ret = new Vector3f(0f, 0f, 1f);
		ret.rotateZ(rotation.z);
		ret.rotateY(rotation.y);
		ret.rotateX(rotation.x);
		return ret;
	}

	/**
	 * rotate the camera object around an axis
	 * @param angle
	 * magnitude of rotation
	 * @param axis
	 * axis of rotation
	 */
	public void rotateAlongAxis(float angle, Vector3f axis) {
		Matrix4f tempMatrix = new Matrix4f();
		tempMatrix.rotate(angle, axis);
		tempMatrix.rotateX(rotation.x);
		tempMatrix.rotateY(rotation.y);
		tempMatrix.rotateZ(rotation.z);
		rotation = EnigUtils.getEulerAngles(tempMatrix);
	}
}
