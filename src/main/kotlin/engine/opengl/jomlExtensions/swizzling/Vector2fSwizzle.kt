@file:Suppress("unused")

package engine.opengl.jomlExtensions

import org.joml.*

val Vector2fc.xx : Vector2f
	get() = Vector2f(x(), x())
var Vector2f.xy : Vector2f
	get() = Vector2f(x(), y())
	set(value) {
		x = value.x
		y = value.y
	}
var Vector2f.yx : Vector2f
	get() = Vector2f(y(), x())
	set(value) {
		y = value.x
		x = value.y
	}
val Vector2fc.yy : Vector2f
	get() = Vector2f(y(), y())
val Vector2fc.xxx : Vector3f
	get() = Vector3f(x(), x(), x())
val Vector2fc.xxy : Vector3f
	get() = Vector3f(x(), x(), y())
val Vector2fc.xyx : Vector3f
	get() = Vector3f(x(), y(), x())
val Vector2fc.xyy : Vector3f
	get() = Vector3f(x(), y(), y())
val Vector2fc.yxx : Vector3f
	get() = Vector3f(y(), x(), x())
val Vector2fc.yxy : Vector3f
	get() = Vector3f(y(), x(), y())
val Vector2fc.yyx : Vector3f
	get() = Vector3f(y(), y(), x())
val Vector2fc.yyy : Vector3f
	get() = Vector3f(y(), y(), y())
val Vector2fc.xxxx : Vector4f
	get() = Vector4f(x(), x(), x(), x())
val Vector2fc.xxxy : Vector4f
	get() = Vector4f(x(), x(), x(), y())
val Vector2fc.xxyx : Vector4f
	get() = Vector4f(x(), x(), y(), x())
val Vector2fc.xxyy : Vector4f
	get() = Vector4f(x(), x(), y(), y())
val Vector2fc.xyxx : Vector4f
	get() = Vector4f(x(), y(), x(), x())
val Vector2fc.xyxy : Vector4f
	get() = Vector4f(x(), y(), x(), y())
val Vector2fc.xyyx : Vector4f
	get() = Vector4f(x(), y(), y(), x())
val Vector2fc.xyyy : Vector4f
	get() = Vector4f(x(), y(), y(), y())
val Vector2fc.yxxx : Vector4f
	get() = Vector4f(y(), x(), x(), x())
val Vector2fc.yxxy : Vector4f
	get() = Vector4f(y(), x(), x(), y())
val Vector2fc.yxyx : Vector4f
	get() = Vector4f(y(), x(), y(), x())
val Vector2fc.yxyy : Vector4f
	get() = Vector4f(y(), x(), y(), y())
val Vector2fc.yyxx : Vector4f
	get() = Vector4f(y(), y(), x(), x())
val Vector2fc.yyxy : Vector4f
	get() = Vector4f(y(), y(), x(), y())
val Vector2fc.yyyx : Vector4f
	get() = Vector4f(y(), y(), y(), x())
val Vector2fc.yyyy : Vector4f
	get() = Vector4f(y(), y(), y(), y())
