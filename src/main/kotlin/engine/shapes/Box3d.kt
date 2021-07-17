package engine.shapes

import org.joml.Vector3f
import engine.opengl.bufferObjects.VAO

class Box3d : Bound3f {
	override var minx: Float
	override var maxx: Float
	override var miny: Float
	override var maxy: Float
	override var minz: Float
	override var maxz: Float

	override var width : Float
		get() = maxx - minx
		set(value) {maxx = minx + value}
	override var height : Float
		get() = maxy - miny
		set(value) {maxy = miny + value}
	override var depth : Float
		get() = maxz - minz
		set(value) {maxz = minz + value}

	var x : Float
		get() = minx
		set(value) {
			maxx = value + width
			minx = value
		}
	var y : Float
		get() = miny
		set(value) {
			maxy = value + height
			miny = value
		}
	var z : Float
		get() = minz
		set(value) {
			maxz = value + depth
			minz = value
		}

	/**
	 * creates a box based on the maximum and minimum coordinates
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param zmin minimum z
	 * @param xmax maximum x
	 * @param ymax maximum y
	 * @param zmax maximum z
	 */
	constructor(xmin: Float, ymin: Float, zmin: Float, xmax: Float, ymax: Float, zmax: Float) {
		minx = xmin
		maxx = xmax
		miny = ymin
		maxy = ymax
		minz = zmin
		maxz = zmax
	}

	/**
	 * creates a box based on two points at opposite corners
	 * @param a first corner
	 * @param b opposing corner
	 */
	constructor(a: Vector3f, b: Vector3f) {
		minx = a.x.coerceAtMost(b.x)
		maxx = a.x.coerceAtLeast(b.x)
		miny = a.y.coerceAtMost(b.y)
		maxy = a.y.coerceAtLeast(b.y)
		minz = a.z.coerceAtMost(b.z)
		maxz = a.z.coerceAtLeast(b.z)
	}

	/**
	 * returns the center of the box
	 * @return center of the box
	 */
	val center: Vector3f
		get() = Vector3f((minx + maxx) / 2, (miny + maxy) / 2, (minz + maxz) / 2)

	/**
	 * gets a VAO that represents the box
	 * @return VAO representing the box
	 */
	fun makeVAO() = VAO(this)

	/**
	 * gets the string representation of the box
	 * @return string representation of the box
	 */
	override fun toString(): String {
		return "x ranges from $minx to $maxx\ny ranges from $miny to $maxy\nz ranges from $minz to $maxz"
	}
}