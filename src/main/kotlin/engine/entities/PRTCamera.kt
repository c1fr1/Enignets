package engine.entities

import engine.PIf
import engine.opengl.EnigWindow
import engine.opengl.jomlExtensions.rotateXYZ
import org.joml.*

typealias Camera3D = PRTCamera

@Suppress("MemberVisibilityCanBePrivate", "unused")
class PRTCamera(var projectionMatrix : Matrix4f, angles : Vector3f, pos : Vector3f) :
	Camera, Orientation(angles, pos) {

	constructor(other : PRTCamera) : this(other.projectionMatrix, other.angles, other)

	/**
	 * Creates a new Camera object
	 * @param fov field of view, 0.25*pi is standard
	 * @param minDist minimum fullRender distance
	 * @param maxDist maximum fullRender distance
	 * @param window window that can be used to form the aspect ratio
	 */
	constructor(window : EnigWindow, fov : Float = PIf / 3f, minDist : Float = 0.01f, maxDist : Float = 99f) :
			this(window.aspectRatio, fov, minDist, maxDist)

	/**
	 * Creates a new Camera object
	 * @param fov field of view, 0.25*pi is standard
	 * @param minDist minimum fullRender distance
	 * @param maxDist maximum fullRender distance
	 * @param aspectRatio aspect ratio the camera is targeted at rendering to
	 */
	constructor(aspectRatio : Float, fov : Float = PIf / 3f, minDist : Float = 0.01f, maxDist : Float = 99f) :
			this(Matrix4f().perspective(fov, aspectRatio, minDist, maxDist), Vector3f(), Vector3f())

	override fun getMatrix() : Matrix4f = Matrix4f(projectionMatrix).rotateXYZ(angles).translate(-x, -y, -z)

	override fun transformVec(v : Vector3fc, target : Vector3f) : Vector3f =
		v.mulProject(projectionMatrix, target).rotateXYZ(-angles.x, -angles.y, -angles.z).sub(x, y, z)

	override fun transformVec(v : Vector4fc, target : Vector4f) : Vector4f =
		v.mul(projectionMatrix, target).rotateXYZ(-angles.x, -angles.y, -angles.z).sub(x, y, z, 0f)

	override fun rotateX(angle: Float): Vector3f {
		angles.x += angle
		return angles
	}

	override fun rotateY(angle: Float): Vector3f {
		angles.y += angle
		return angles
	}

	override fun rotateZ(angle: Float): Vector3f {
		angles.z += angle
		return angles
	}
}