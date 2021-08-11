package engine.shapes.bounds

import engine.shapes.Bound3f
import org.joml.Vector3fc

open class Box3d : Bound3f {
	override var minx : Float = 0f
	override var maxx : Float = 0f
	override var miny : Float = 0f
	override var maxy : Float = 0f
	override var minz : Float = 0f
	override var maxz : Float = 0f

	override var width : Float
		get() = maxx - minx
		set(value) {maxx = minx + value}
	override var height : Float
		get() = maxy - miny
		set(value) {maxy = miny + value}
	override var depth : Float
		get() = maxz - minz
		set(value) {maxz = minz + value}

	override var x : Float
		get() = minx
		set(value) {
			maxx = value + width
			minx = value
		}
	override var y : Float
		get() = miny
		set(value) {
			maxy = value + height
			miny = value
		}
	override var z : Float
		get() = minz
		set(value) {
			maxz = value + depth
			minz = value
		}

	constructor()

	/**
	 * creates a box based on the maximum and minimum coordinates
	 * @param xmin minimum x
	 * @param ymin minimum y
	 * @param zmin minimum z
	 * @param xmax maximum x
	 * @param ymax maximum y
	 * @param zmax maximum z
	 */
	constructor(xmin : Float, ymin : Float, zmin : Float, xmax : Float, ymax : Float, zmax : Float) {
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
	constructor(a : Vector3fc, b : Vector3fc) {
		minx = a.x().coerceAtMost(b.x())
		maxx = a.x().coerceAtLeast(b.x())
		miny = a.y().coerceAtMost(b.y())
		maxy = a.y().coerceAtLeast(b.y())
		minz = a.z().coerceAtMost(b.z())
		maxz = a.z().coerceAtLeast(b.z())
	}

	open fun translate(delta : Vector3fc) = translate(delta.x(), delta.y(), delta.z())

	open fun translate(x : Float, y : Float, z : Float) = translate(x, y, z, this)
}