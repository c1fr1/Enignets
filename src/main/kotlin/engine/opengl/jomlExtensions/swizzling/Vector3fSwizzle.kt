@file:Suppress("unused")

package engine.opengl.jomlExtensions

import org.joml.*

var Vector3f.xx : Vector2f
	get() = Vector2f(x, x)
	set(value) {
		x = value.x
		x = value.y
	}
var Vector3f.xy : Vector2f
	get() = Vector2f(x, y)
	set(value) {
		x = value.x
		y = value.y
	}
var Vector3f.xz : Vector2f
	get() = Vector2f(x, z)
	set(value) {
		x = value.x
		z = value.y
	}
var Vector3f.yx : Vector2f
	get() = Vector2f(y, x)
	set(value) {
		y = value.x
		x = value.y
	}
var Vector3f.yy : Vector2f
	get() = Vector2f(y, y)
	set(value) {
		y = value.x
		y = value.y
	}
var Vector3f.yz : Vector2f
	get() = Vector2f(y, z)
	set(value) {
		y = value.x
		z = value.y
	}
var Vector3f.zx : Vector2f
	get() = Vector2f(z, x)
	set(value) {
		z = value.x
		x = value.y
	}
var Vector3f.zy : Vector2f
	get() = Vector2f(z, y)
	set(value) {
		z = value.x
		y = value.y
	}
var Vector3f.zz : Vector2f
	get() = Vector2f(z, z)
	set(value) {
		z = value.x
		z = value.y
	}
var Vector3f.xxx : Vector3f
	get() = Vector3f(x, x, x)
	set(value) {
		x = value.x
		x = value.y
		x = value.z
	}
var Vector3f.xxy : Vector3f
	get() = Vector3f(x, x, y)
	set(value) {
		x = value.x
		x = value.y
		y = value.z
	}
var Vector3f.xxz : Vector3f
	get() = Vector3f(x, x, z)
	set(value) {
		x = value.x
		x = value.y
		z = value.z
	}
var Vector3f.xyx : Vector3f
	get() = Vector3f(x, y, x)
	set(value) {
		x = value.x
		y = value.y
		x = value.z
	}
var Vector3f.xyy : Vector3f
	get() = Vector3f(x, y, y)
	set(value) {
		x = value.x
		y = value.y
		y = value.z
	}
var Vector3f.xyz : Vector3f
	get() = Vector3f(x, y, z)
	set(value) {
		x = value.x
		y = value.y
		z = value.z
	}
var Vector3f.xzx : Vector3f
	get() = Vector3f(x, z, x)
	set(value) {
		x = value.x
		z = value.y
		x = value.z
	}
var Vector3f.xzy : Vector3f
	get() = Vector3f(x, z, y)
	set(value) {
		x = value.x
		z = value.y
		y = value.z
	}
var Vector3f.xzz : Vector3f
	get() = Vector3f(x, z, z)
	set(value) {
		x = value.x
		z = value.y
		z = value.z
	}
var Vector3f.yxx : Vector3f
	get() = Vector3f(y, x, x)
	set(value) {
		y = value.x
		x = value.y
		x = value.z
	}
var Vector3f.yxy : Vector3f
	get() = Vector3f(y, x, y)
	set(value) {
		y = value.x
		x = value.y
		y = value.z
	}
var Vector3f.yxz : Vector3f
	get() = Vector3f(y, x, z)
	set(value) {
		y = value.x
		x = value.y
		z = value.z
	}
var Vector3f.yyx : Vector3f
	get() = Vector3f(y, y, x)
	set(value) {
		y = value.x
		y = value.y
		x = value.z
	}
var Vector3f.yyy : Vector3f
	get() = Vector3f(y, y, y)
	set(value) {
		y = value.x
		y = value.y
		y = value.z
	}
var Vector3f.yyz : Vector3f
	get() = Vector3f(y, y, z)
	set(value) {
		y = value.x
		y = value.y
		z = value.z
	}
var Vector3f.yzx : Vector3f
	get() = Vector3f(y, z, x)
	set(value) {
		y = value.x
		z = value.y
		x = value.z
	}
var Vector3f.yzy : Vector3f
	get() = Vector3f(y, z, y)
	set(value) {
		y = value.x
		z = value.y
		y = value.z
	}
var Vector3f.yzz : Vector3f
	get() = Vector3f(y, z, z)
	set(value) {
		y = value.x
		z = value.y
		z = value.z
	}
var Vector3f.zxx : Vector3f
	get() = Vector3f(z, x, x)
	set(value) {
		z = value.x
		x = value.y
		x = value.z
	}
var Vector3f.zxy : Vector3f
	get() = Vector3f(z, x, y)
	set(value) {
		z = value.x
		x = value.y
		y = value.z
	}
var Vector3f.zxz : Vector3f
	get() = Vector3f(z, x, z)
	set(value) {
		z = value.x
		x = value.y
		z = value.z
	}
var Vector3f.zyx : Vector3f
	get() = Vector3f(z, y, x)
	set(value) {
		z = value.x
		y = value.y
		x = value.z
	}
var Vector3f.zyy : Vector3f
	get() = Vector3f(z, y, y)
	set(value) {
		z = value.x
		y = value.y
		y = value.z
	}
var Vector3f.zyz : Vector3f
	get() = Vector3f(z, y, z)
	set(value) {
		z = value.x
		y = value.y
		z = value.z
	}
var Vector3f.zzx : Vector3f
	get() = Vector3f(z, z, x)
	set(value) {
		z = value.x
		z = value.y
		x = value.z
	}
var Vector3f.zzy : Vector3f
	get() = Vector3f(z, z, y)
	set(value) {
		z = value.x
		z = value.y
		y = value.z
	}
var Vector3f.zzz : Vector3f
	get() = Vector3f(z, z, z)
	set(value) {
		z = value.x
		z = value.y
		z = value.z
	}
var Vector3f.xxxx : Vector4f
	get() = Vector4f(x, x, x, x)
	set(value) {
		x = value.x
		x = value.y
		x = value.z
		x = value.w
	}
var Vector3f.xxxy : Vector4f
	get() = Vector4f(x, x, x, y)
	set(value) {
		x = value.x
		x = value.y
		x = value.z
		y = value.w
	}
var Vector3f.xxxz : Vector4f
	get() = Vector4f(x, x, x, z)
	set(value) {
		x = value.x
		x = value.y
		x = value.z
		z = value.w
	}
var Vector3f.xxyx : Vector4f
	get() = Vector4f(x, x, y, x)
	set(value) {
		x = value.x
		x = value.y
		y = value.z
		x = value.w
	}
var Vector3f.xxyy : Vector4f
	get() = Vector4f(x, x, y, y)
	set(value) {
		x = value.x
		x = value.y
		y = value.z
		y = value.w
	}
var Vector3f.xxyz : Vector4f
	get() = Vector4f(x, x, y, z)
	set(value) {
		x = value.x
		x = value.y
		y = value.z
		z = value.w
	}
var Vector3f.xxzx : Vector4f
	get() = Vector4f(x, x, z, x)
	set(value) {
		x = value.x
		x = value.y
		z = value.z
		x = value.w
	}
var Vector3f.xxzy : Vector4f
	get() = Vector4f(x, x, z, y)
	set(value) {
		x = value.x
		x = value.y
		z = value.z
		y = value.w
	}
var Vector3f.xxzz : Vector4f
	get() = Vector4f(x, x, z, z)
	set(value) {
		x = value.x
		x = value.y
		z = value.z
		z = value.w
	}
var Vector3f.xyxx : Vector4f
	get() = Vector4f(x, y, x, x)
	set(value) {
		x = value.x
		y = value.y
		x = value.z
		x = value.w
	}
var Vector3f.xyxy : Vector4f
	get() = Vector4f(x, y, x, y)
	set(value) {
		x = value.x
		y = value.y
		x = value.z
		y = value.w
	}
var Vector3f.xyxz : Vector4f
	get() = Vector4f(x, y, x, z)
	set(value) {
		x = value.x
		y = value.y
		x = value.z
		z = value.w
	}
var Vector3f.xyyx : Vector4f
	get() = Vector4f(x, y, y, x)
	set(value) {
		x = value.x
		y = value.y
		y = value.z
		x = value.w
	}
var Vector3f.xyyy : Vector4f
	get() = Vector4f(x, y, y, y)
	set(value) {
		x = value.x
		y = value.y
		y = value.z
		y = value.w
	}
var Vector3f.xyyz : Vector4f
	get() = Vector4f(x, y, y, z)
	set(value) {
		x = value.x
		y = value.y
		y = value.z
		z = value.w
	}
var Vector3f.xyzx : Vector4f
	get() = Vector4f(x, y, z, x)
	set(value) {
		x = value.x
		y = value.y
		z = value.z
		x = value.w
	}
var Vector3f.xyzy : Vector4f
	get() = Vector4f(x, y, z, y)
	set(value) {
		x = value.x
		y = value.y
		z = value.z
		y = value.w
	}
var Vector3f.xyzz : Vector4f
	get() = Vector4f(x, y, z, z)
	set(value) {
		x = value.x
		y = value.y
		z = value.z
		z = value.w
	}
var Vector3f.xzxx : Vector4f
	get() = Vector4f(x, z, x, x)
	set(value) {
		x = value.x
		z = value.y
		x = value.z
		x = value.w
	}
var Vector3f.xzxy : Vector4f
	get() = Vector4f(x, z, x, y)
	set(value) {
		x = value.x
		z = value.y
		x = value.z
		y = value.w
	}
var Vector3f.xzxz : Vector4f
	get() = Vector4f(x, z, x, z)
	set(value) {
		x = value.x
		z = value.y
		x = value.z
		z = value.w
	}
var Vector3f.xzyx : Vector4f
	get() = Vector4f(x, z, y, x)
	set(value) {
		x = value.x
		z = value.y
		y = value.z
		x = value.w
	}
var Vector3f.xzyy : Vector4f
	get() = Vector4f(x, z, y, y)
	set(value) {
		x = value.x
		z = value.y
		y = value.z
		y = value.w
	}
var Vector3f.xzyz : Vector4f
	get() = Vector4f(x, z, y, z)
	set(value) {
		x = value.x
		z = value.y
		y = value.z
		z = value.w
	}
var Vector3f.xzzx : Vector4f
	get() = Vector4f(x, z, z, x)
	set(value) {
		x = value.x
		z = value.y
		z = value.z
		x = value.w
	}
var Vector3f.xzzy : Vector4f
	get() = Vector4f(x, z, z, y)
	set(value) {
		x = value.x
		z = value.y
		z = value.z
		y = value.w
	}
var Vector3f.xzzz : Vector4f
	get() = Vector4f(x, z, z, z)
	set(value) {
		x = value.x
		z = value.y
		z = value.z
		z = value.w
	}
var Vector3f.yxxx : Vector4f
	get() = Vector4f(y, x, x, x)
	set(value) {
		y = value.x
		x = value.y
		x = value.z
		x = value.w
	}
var Vector3f.yxxy : Vector4f
	get() = Vector4f(y, x, x, y)
	set(value) {
		y = value.x
		x = value.y
		x = value.z
		y = value.w
	}
var Vector3f.yxxz : Vector4f
	get() = Vector4f(y, x, x, z)
	set(value) {
		y = value.x
		x = value.y
		x = value.z
		z = value.w
	}
var Vector3f.yxyx : Vector4f
	get() = Vector4f(y, x, y, x)
	set(value) {
		y = value.x
		x = value.y
		y = value.z
		x = value.w
	}
var Vector3f.yxyy : Vector4f
	get() = Vector4f(y, x, y, y)
	set(value) {
		y = value.x
		x = value.y
		y = value.z
		y = value.w
	}
var Vector3f.yxyz : Vector4f
	get() = Vector4f(y, x, y, z)
	set(value) {
		y = value.x
		x = value.y
		y = value.z
		z = value.w
	}
var Vector3f.yxzx : Vector4f
	get() = Vector4f(y, x, z, x)
	set(value) {
		y = value.x
		x = value.y
		z = value.z
		x = value.w
	}
var Vector3f.yxzy : Vector4f
	get() = Vector4f(y, x, z, y)
	set(value) {
		y = value.x
		x = value.y
		z = value.z
		y = value.w
	}
var Vector3f.yxzz : Vector4f
	get() = Vector4f(y, x, z, z)
	set(value) {
		y = value.x
		x = value.y
		z = value.z
		z = value.w
	}
var Vector3f.yyxx : Vector4f
	get() = Vector4f(y, y, x, x)
	set(value) {
		y = value.x
		y = value.y
		x = value.z
		x = value.w
	}
var Vector3f.yyxy : Vector4f
	get() = Vector4f(y, y, x, y)
	set(value) {
		y = value.x
		y = value.y
		x = value.z
		y = value.w
	}
var Vector3f.yyxz : Vector4f
	get() = Vector4f(y, y, x, z)
	set(value) {
		y = value.x
		y = value.y
		x = value.z
		z = value.w
	}
var Vector3f.yyyx : Vector4f
	get() = Vector4f(y, y, y, x)
	set(value) {
		y = value.x
		y = value.y
		y = value.z
		x = value.w
	}
var Vector3f.yyyy : Vector4f
	get() = Vector4f(y, y, y, y)
	set(value) {
		y = value.x
		y = value.y
		y = value.z
		y = value.w
	}
var Vector3f.yyyz : Vector4f
	get() = Vector4f(y, y, y, z)
	set(value) {
		y = value.x
		y = value.y
		y = value.z
		z = value.w
	}
var Vector3f.yyzx : Vector4f
	get() = Vector4f(y, y, z, x)
	set(value) {
		y = value.x
		y = value.y
		z = value.z
		x = value.w
	}
var Vector3f.yyzy : Vector4f
	get() = Vector4f(y, y, z, y)
	set(value) {
		y = value.x
		y = value.y
		z = value.z
		y = value.w
	}
var Vector3f.yyzz : Vector4f
	get() = Vector4f(y, y, z, z)
	set(value) {
		y = value.x
		y = value.y
		z = value.z
		z = value.w
	}
var Vector3f.yzxx : Vector4f
	get() = Vector4f(y, z, x, x)
	set(value) {
		y = value.x
		z = value.y
		x = value.z
		x = value.w
	}
var Vector3f.yzxy : Vector4f
	get() = Vector4f(y, z, x, y)
	set(value) {
		y = value.x
		z = value.y
		x = value.z
		y = value.w
	}
var Vector3f.yzxz : Vector4f
	get() = Vector4f(y, z, x, z)
	set(value) {
		y = value.x
		z = value.y
		x = value.z
		z = value.w
	}
var Vector3f.yzyx : Vector4f
	get() = Vector4f(y, z, y, x)
	set(value) {
		y = value.x
		z = value.y
		y = value.z
		x = value.w
	}
var Vector3f.yzyy : Vector4f
	get() = Vector4f(y, z, y, y)
	set(value) {
		y = value.x
		z = value.y
		y = value.z
		y = value.w
	}
var Vector3f.yzyz : Vector4f
	get() = Vector4f(y, z, y, z)
	set(value) {
		y = value.x
		z = value.y
		y = value.z
		z = value.w
	}
var Vector3f.yzzx : Vector4f
	get() = Vector4f(y, z, z, x)
	set(value) {
		y = value.x
		z = value.y
		z = value.z
		x = value.w
	}
var Vector3f.yzzy : Vector4f
	get() = Vector4f(y, z, z, y)
	set(value) {
		y = value.x
		z = value.y
		z = value.z
		y = value.w
	}
var Vector3f.yzzz : Vector4f
	get() = Vector4f(y, z, z, z)
	set(value) {
		y = value.x
		z = value.y
		z = value.z
		z = value.w
	}
var Vector3f.zxxx : Vector4f
	get() = Vector4f(z, x, x, x)
	set(value) {
		z = value.x
		x = value.y
		x = value.z
		x = value.w
	}
var Vector3f.zxxy : Vector4f
	get() = Vector4f(z, x, x, y)
	set(value) {
		z = value.x
		x = value.y
		x = value.z
		y = value.w
	}
var Vector3f.zxxz : Vector4f
	get() = Vector4f(z, x, x, z)
	set(value) {
		z = value.x
		x = value.y
		x = value.z
		z = value.w
	}
var Vector3f.zxyx : Vector4f
	get() = Vector4f(z, x, y, x)
	set(value) {
		z = value.x
		x = value.y
		y = value.z
		x = value.w
	}
var Vector3f.zxyy : Vector4f
	get() = Vector4f(z, x, y, y)
	set(value) {
		z = value.x
		x = value.y
		y = value.z
		y = value.w
	}
var Vector3f.zxyz : Vector4f
	get() = Vector4f(z, x, y, z)
	set(value) {
		z = value.x
		x = value.y
		y = value.z
		z = value.w
	}
var Vector3f.zxzx : Vector4f
	get() = Vector4f(z, x, z, x)
	set(value) {
		z = value.x
		x = value.y
		z = value.z
		x = value.w
	}
var Vector3f.zxzy : Vector4f
	get() = Vector4f(z, x, z, y)
	set(value) {
		z = value.x
		x = value.y
		z = value.z
		y = value.w
	}
var Vector3f.zxzz : Vector4f
	get() = Vector4f(z, x, z, z)
	set(value) {
		z = value.x
		x = value.y
		z = value.z
		z = value.w
	}
var Vector3f.zyxx : Vector4f
	get() = Vector4f(z, y, x, x)
	set(value) {
		z = value.x
		y = value.y
		x = value.z
		x = value.w
	}
var Vector3f.zyxy : Vector4f
	get() = Vector4f(z, y, x, y)
	set(value) {
		z = value.x
		y = value.y
		x = value.z
		y = value.w
	}
var Vector3f.zyxz : Vector4f
	get() = Vector4f(z, y, x, z)
	set(value) {
		z = value.x
		y = value.y
		x = value.z
		z = value.w
	}
var Vector3f.zyyx : Vector4f
	get() = Vector4f(z, y, y, x)
	set(value) {
		z = value.x
		y = value.y
		y = value.z
		x = value.w
	}
var Vector3f.zyyy : Vector4f
	get() = Vector4f(z, y, y, y)
	set(value) {
		z = value.x
		y = value.y
		y = value.z
		y = value.w
	}
var Vector3f.zyyz : Vector4f
	get() = Vector4f(z, y, y, z)
	set(value) {
		z = value.x
		y = value.y
		y = value.z
		z = value.w
	}
var Vector3f.zyzx : Vector4f
	get() = Vector4f(z, y, z, x)
	set(value) {
		z = value.x
		y = value.y
		z = value.z
		x = value.w
	}
var Vector3f.zyzy : Vector4f
	get() = Vector4f(z, y, z, y)
	set(value) {
		z = value.x
		y = value.y
		z = value.z
		y = value.w
	}
var Vector3f.zyzz : Vector4f
	get() = Vector4f(z, y, z, z)
	set(value) {
		z = value.x
		y = value.y
		z = value.z
		z = value.w
	}
var Vector3f.zzxx : Vector4f
	get() = Vector4f(z, z, x, x)
	set(value) {
		z = value.x
		z = value.y
		x = value.z
		x = value.w
	}
var Vector3f.zzxy : Vector4f
	get() = Vector4f(z, z, x, y)
	set(value) {
		z = value.x
		z = value.y
		x = value.z
		y = value.w
	}
var Vector3f.zzxz : Vector4f
	get() = Vector4f(z, z, x, z)
	set(value) {
		z = value.x
		z = value.y
		x = value.z
		z = value.w
	}
var Vector3f.zzyx : Vector4f
	get() = Vector4f(z, z, y, x)
	set(value) {
		z = value.x
		z = value.y
		y = value.z
		x = value.w
	}
var Vector3f.zzyy : Vector4f
	get() = Vector4f(z, z, y, y)
	set(value) {
		z = value.x
		z = value.y
		y = value.z
		y = value.w
	}
var Vector3f.zzyz : Vector4f
	get() = Vector4f(z, z, y, z)
	set(value) {
		z = value.x
		z = value.y
		y = value.z
		z = value.w
	}
var Vector3f.zzzx : Vector4f
	get() = Vector4f(z, z, z, x)
	set(value) {
		z = value.x
		z = value.y
		z = value.z
		x = value.w
	}
var Vector3f.zzzy : Vector4f
	get() = Vector4f(z, z, z, y)
	set(value) {
		z = value.x
		z = value.y
		z = value.z
		y = value.w
	}
var Vector3f.zzzz : Vector4f
	get() = Vector4f(z, z, z, z)
	set(value) {
		z = value.x
		z = value.y
		z = value.z
		z = value.w
	}

fun Vector3f(xy : Vector2f, z : Float) = Vector3f(xy.x, xy.y, z)
fun Vector3f(x : Float, yz : Vector2f) = Vector3f(x, yz.x, yz.y)