@file:Suppress("unused")

package engine.opengl.jomlExtensions

import org.joml.*

fun Array<Vector2fc>.toFloatArray() = FloatArray(size * 2) {get(it / 2)[it % 2]}
fun Array<Vector2f>.toFloatArray() = FloatArray(size * 2) {get(it / 2)[it % 2]}
fun Array<Vector3fc>.toFloatArray() = FloatArray(size * 3) {get(it / 3)[it % 3]}
fun Array<Vector3f>.toFloatArray() = FloatArray(size * 3) {get(it / 3)[it % 3]}
fun Array<Vector4fc>.toFloatArray() = FloatArray(size * 4) {get(it / 4)[it % 4]}
fun Array<Vector4f>.toFloatArray() = FloatArray(size * 4) {get(it / 4)[it % 4]}

fun Array<Vector2dc>.toDoubleArray() = DoubleArray(size * 2) {get(it / 2)[it % 2]}
fun Array<Vector2d>.toDoubleArray() = DoubleArray(size * 2) {get(it / 2)[it % 2]}
fun Array<Vector3dc>.toDoubleArray() = DoubleArray(size * 3) {get(it / 3)[it % 3]}
fun Array<Vector3d>.toDoubleArray() = DoubleArray(size * 3) {get(it / 3)[it % 3]}
fun Array<Vector4dc>.toDoubleArray() = DoubleArray(size * 4) {get(it / 4)[it % 4]}
fun Array<Vector4d>.toDoubleArray() = DoubleArray(size * 4) {get(it / 4)[it % 4]}

fun Array<Vector2dc>.getF() = FloatArray(size * 2) {get(it / 2)[it % 2].toFloat()}
fun Array<Vector2d>.getF() = FloatArray(size * 2) {get(it / 2)[it % 2].toFloat()}
fun Array<Vector3dc>.getF() = FloatArray(size * 3) {get(it / 3)[it % 3].toFloat()}
fun Array<Vector3d>.getF() = FloatArray(size * 3) {get(it / 3)[it % 3].toFloat()}
fun Array<Vector4dc>.getF() = FloatArray(size * 4) {get(it / 4)[it % 4].toFloat()}
fun Array<Vector4d>.getF() = FloatArray(size * 4) {get(it / 4)[it % 4].toFloat()}

fun Array<Vector2ic>.toIntArray() = IntArray(size * 2) {get(it / 2)[it % 2]}
fun Array<Vector2i>.toIntArray() = IntArray(size * 2) {get(it / 2)[it % 2]}
fun Array<Vector3ic>.toIntArray() = IntArray(size * 3) {get(it / 3)[it % 3]}
fun Array<Vector3i>.toIntArray() = IntArray(size * 3) {get(it / 3)[it % 3]}
fun Array<Vector4ic>.toIntArray() = IntArray(size * 4) {get(it / 4)[it % 4]}
fun Array<Vector4i>.toIntArray() = IntArray(size * 4) {get(it / 4)[it % 4]}

fun Array<Matrix2fc>.toFloatArray() : FloatArray {
	val ret = FloatArray(size * 4)
	for (i in indices) {
		get(i).get(ret, i * 4)
	}
	return ret
}
fun Array<Matrix3fc>.toFloatArray() : FloatArray {
	val ret = FloatArray(size * 9)
	for (i in indices) {
		get(i).get(ret, i * 9)
	}
	return ret
}
fun Array<Matrix3x2fc>.toFloatArray() : FloatArray {
	val ret = FloatArray(size * 6)
	for (i in indices) {
		get(i).get(ret, i * 6)
	}
	return ret
}
fun Array<Matrix4fc>.toFloatArray() : FloatArray {
	val ret = FloatArray(size * 16)
	for (i in indices) {
		get(i).get(ret, i * 16)
	}
	return ret
}
fun Array<Matrix4x3fc>.toFloatArray() : FloatArray {
	val ret = FloatArray(size * 12)
	for (i in indices) {
		get(i).get(ret, i * 12)
	}
	return ret
}

fun Array<Matrix2dc>.toFloatArray() : DoubleArray {
	val ret = DoubleArray(size * 4)
	for (i in indices) {
		get(i).get(ret, i * 4)
	}
	return ret
}
fun Array<Matrix3dc>.toFloatArray() : DoubleArray {
	val ret = DoubleArray(size * 9)
	for (i in indices) {
		get(i).get(ret, i * 9)
	}
	return ret
}
fun Array<Matrix3x2dc>.toFloatArray() : DoubleArray {
	val ret = DoubleArray(size * 6)
	for (i in indices) {
		get(i).get(ret, i * 6)
	}
	return ret
}
fun Array<Matrix4dc>.toFloatArray() : DoubleArray {
	val ret = DoubleArray(size * 16)
	for (i in indices) {
		get(i).get(ret, i * 16)
	}
	return ret
}
fun Array<Matrix4x3dc>.toFloatArray() : DoubleArray {
	val ret = DoubleArray(size * 12)
	for (i in indices) {
		get(i).get(ret, i * 12)
	}
	return ret
}

fun Array<Matrix2dc>.getF() : FloatArray {
	val ret = DoubleArray(size * 4)
	for (i in indices) {
		get(i).get(ret, i * 4)
	}
	return FloatArray(ret.size) {ret[it].toFloat()}
}
fun Array<Matrix3dc>.getF() : FloatArray {
	val ret = DoubleArray(size * 9)
	for (i in indices) {
		get(i).get(ret, i * 9)
	}
	return FloatArray(ret.size) {ret[it].toFloat()}
}
fun Array<Matrix3x2dc>.getF() : FloatArray {
	val ret = DoubleArray(size * 6)
	for (i in indices) {
		get(i).get(ret, i * 6)
	}
	return FloatArray(ret.size) {ret[it].toFloat()}
}
fun Array<Matrix4dc>.getF() : FloatArray {
	val ret = DoubleArray(size * 16)
	for (i in indices) {
		get(i).get(ret, i * 16)
	}
	return FloatArray(ret.size) {ret[it].toFloat()}
}
fun Array<Matrix4x3dc>.getF() : FloatArray {
	val ret = DoubleArray(size * 12)
	for (i in indices) {
		get(i).get(ret, i * 12)
	}
	return FloatArray(ret.size) {ret[it].toFloat()}
}

fun Matrix2dc.getF() : FloatArray {
	val ret = get(DoubleArray(4))
	return FloatArray(ret.size) {ret[it].toFloat()}
}
fun Matrix3x2dc.getF() : FloatArray {
	val ret = get(DoubleArray(6))
	return FloatArray(ret.size) {ret[it].toFloat()}
}