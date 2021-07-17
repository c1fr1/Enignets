package engine.entities

import engine.opengl.EnigWindow
import engine.opengl.jomlExtensions.times
import org.joml.*
import kotlin.math.PI

//Dynamic Axis Camera
@Suppress("unused", "MemberVisibilityCanBePrivate")
class DACamera(var projectionMatrix : Matrix4f, rotation : Quaternionf, var pos : Vector3fc) :
	Camera, DAOrientation(rotation, pos) {

	constructor(other : DACamera) : this(other.projectionMatrix, other.rotation, other)

	/**
	 * Creates a new Camera object
	 * @param fov field of view, 0.25*pi is standard
	 * @param minDist minimum fullRender distance
	 * @param maxDist maximum fullRender distance
	 * @param window window that can be used to form the aspect ratio
	 */
	constructor(
		fov : Float = PI.toFloat() * 0.25f,
		minDist : Float = 0.01f,
		maxDist : Float = 9999f,
		window : EnigWindow
	) : this(fov, minDist, maxDist, window.aspectRatio)

	/**
	 * Creates a new Camera object
	 * @param fov field of view, 0.25*pi is standard
	 * @param minDist minimum fullRender distance
	 * @param maxDist maximum fullRender distance
	 * @param aspectRatio aspect ratio the camera is targeted at rendering to
	 */
	constructor(
		fov : Float = PI.toFloat() * 0.25f,
		minDist : Float = 0.01f,
		maxDist : Float = 9999f,
		aspectRatio : Float
	) : this(Matrix4f().perspective(fov, aspectRatio, minDist, maxDist), Quaternionf(), Vector3f())

	override fun getMatrix() : Matrix4f =
		(projectionMatrix * rotation.get(Matrix4f())).translate(-this.x, -this.y, -this.z)

	override fun transformVec(v : Vector3fc, target : Vector3f) : Vector3f =
		rotation.transformInverse(v.sub(x, y, z, target)).mulProject(projectionMatrix)

	override fun transformVec(v : Vector4fc, target : Vector4f) : Vector4f =
		rotation.transformInverse(v.sub(x, y, z, 0f, target)).mul(projectionMatrix)
}