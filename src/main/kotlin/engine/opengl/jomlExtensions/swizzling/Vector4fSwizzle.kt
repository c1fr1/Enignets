@file:Suppress("unused")

package engine.opengl.jomlExtensions

import org.joml.*

val Vector4fc.xx : Vector2f
	get() = Vector2f(x(), x())
var Vector4f.xy : Vector2f
	get() = Vector2f(x(), y())
	set(value) {
		x = value.x
		y = value.y
	}
var Vector4f.xz : Vector2f
	get() = Vector2f(x(), z())
	set(value) {
		x = value.x
		z = value.y
	}
var Vector4f.xw : Vector2f
	get() = Vector2f(x(), w())
	set(value) {
		x = value.x
		w = value.y
	}
var Vector4f.yx : Vector2f
	get() = Vector2f(y(), x())
	set(value) {
		y = value.x
		x = value.y
	}
val Vector4fc.yy : Vector2f
	get() = Vector2f(y(), y())
var Vector4f.yz : Vector2f
	get() = Vector2f(y(), z())
	set(value) {
		y = value.x
		z = value.y
	}
var Vector4f.yw : Vector2f
	get() = Vector2f(y(), w())
	set(value) {
		y = value.x
		w = value.y
	}
var Vector4f.zx : Vector2f
	get() = Vector2f(z(), x())
	set(value) {
		z = value.x
		x = value.y
	}
var Vector4f.zy : Vector2f
	get() = Vector2f(z(), y())
	set(value) {
		z = value.x
		y = value.y
	}
val Vector4fc.zz : Vector2f
	get() = Vector2f(z(), z())
var Vector4f.zw : Vector2f
	get() = Vector2f(z(), w())
	set(value) {
		z = value.x
		w = value.y
	}
var Vector4f.wx : Vector2f
	get() = Vector2f(w(), x())
	set(value) {
		w = value.x
		x = value.y
	}
var Vector4f.wy : Vector2f
	get() = Vector2f(w(), y())
	set(value) {
		w = value.x
		y = value.y
	}
var Vector4f.wz : Vector2f
	get() = Vector2f(w(), z())
	set(value) {
		w = value.x
		z = value.y
	}
val Vector4fc.ww : Vector2f
	get() = Vector2f(w(), w())
val Vector4fc.xxx : Vector3f
	get() = Vector3f(x(), x(), x())
val Vector4fc.xxy : Vector3f
	get() = Vector3f(x(), x(), y())
val Vector4fc.xxz : Vector3f
	get() = Vector3f(x(), x(), z())
val Vector4fc.xxw : Vector3f
	get() = Vector3f(x(), x(), w())
val Vector4fc.xyx : Vector3f
	get() = Vector3f(x(), y(), x())
val Vector4fc.xyy : Vector3f
	get() = Vector3f(x(), y(), y())
var Vector4f.xyz : Vector3f
	get() = Vector3f(x(), y(), z())
	set(value) {
		x = value.x
		y = value.y
		z = value.z
	}
var Vector4f.xyw : Vector3f
	get() = Vector3f(x(), y(), w())
	set(value) {
		x = value.x
		y = value.y
		w = value.z
	}
val Vector4fc.xzx : Vector3f
	get() = Vector3f(x(), z(), x())
var Vector4f.xzy : Vector3f
	get() = Vector3f(x(), z(), y())
	set(value) {
		x = value.x
		z = value.y
		y = value.z
	}
val Vector4fc.xzz : Vector3f
	get() = Vector3f(x(), z(), z())
var Vector4f.xzw : Vector3f
	get() = Vector3f(x(), z(), w())
	set(value) {
		x = value.x
		z = value.y
		w = value.z
	}
val Vector4fc.xwx : Vector3f
	get() = Vector3f(x(), w(), x())
var Vector4f.xwy : Vector3f
	get() = Vector3f(x(), w(), y())
	set(value) {
		x = value.x
		w = value.y
		y = value.z
	}
var Vector4f.xwz : Vector3f
	get() = Vector3f(x(), w(), z())
	set(value) {
		x = value.x
		w = value.y
		z = value.z
	}
val Vector4fc.xww : Vector3f
	get() = Vector3f(x(), w(), w())
val Vector4fc.yxx : Vector3f
	get() = Vector3f(y(), x(), x())
val Vector4fc.yxy : Vector3f
	get() = Vector3f(y(), x(), y())
var Vector4f.yxz : Vector3f
	get() = Vector3f(y(), x(), z())
	set(value) {
		y = value.x
		x = value.y
		z = value.z
	}
var Vector4f.yxw : Vector3f
	get() = Vector3f(y(), x(), w())
	set(value) {
		y = value.x
		x = value.y
		w = value.z
	}
val Vector4fc.yyx : Vector3f
	get() = Vector3f(y(), y(), x())
val Vector4fc.yyy : Vector3f
	get() = Vector3f(y(), y(), y())
val Vector4fc.yyz : Vector3f
	get() = Vector3f(y(), y(), z())
val Vector4fc.yyw : Vector3f
	get() = Vector3f(y(), y(), w())
var Vector4f.yzx : Vector3f
	get() = Vector3f(y(), z(), x())
	set(value) {
		y = value.x
		z = value.y
		x = value.z
	}
val Vector4fc.yzy : Vector3f
	get() = Vector3f(y(), z(), y())
val Vector4fc.yzz : Vector3f
	get() = Vector3f(y(), z(), z())
var Vector4f.yzw : Vector3f
	get() = Vector3f(y(), z(), w())
	set(value) {
		y = value.x
		z = value.y
		w = value.z
	}
var Vector4f.ywx : Vector3f
	get() = Vector3f(y(), w(), x())
	set(value) {
		y = value.x
		w = value.y
		x = value.z
	}
val Vector4fc.ywy : Vector3f
	get() = Vector3f(y(), w(), y())
var Vector4f.ywz : Vector3f
	get() = Vector3f(y(), w(), z())
	set(value) {
		y = value.x
		w = value.y
		z = value.z
	}
val Vector4fc.yww : Vector3f
	get() = Vector3f(y(), w(), w())
val Vector4fc.zxx : Vector3f
	get() = Vector3f(z(), x(), x())
var Vector4f.zxy : Vector3f
	get() = Vector3f(z(), x(), y())
	set(value) {
		z = value.x
		x = value.y
		y = value.z
	}
val Vector4fc.zxz : Vector3f
	get() = Vector3f(z(), x(), z())
var Vector4f.zxw : Vector3f
	get() = Vector3f(z(), x(), w())
	set(value) {
		z = value.x
		x = value.y
		w = value.z
	}
var Vector4f.zyx : Vector3f
	get() = Vector3f(z(), y(), x())
	set(value) {
		z = value.x
		y = value.y
		x = value.z
	}
val Vector4fc.zyy : Vector3f
	get() = Vector3f(z(), y(), y())
val Vector4fc.zyz : Vector3f
	get() = Vector3f(z(), y(), z())
var Vector4f.zyw : Vector3f
	get() = Vector3f(z(), y(), w())
	set(value) {
		z = value.x
		y = value.y
		w = value.z
	}
val Vector4fc.zzx : Vector3f
	get() = Vector3f(z(), z(), x())
val Vector4fc.zzy : Vector3f
	get() = Vector3f(z(), z(), y())
val Vector4fc.zzz : Vector3f
	get() = Vector3f(z(), z(), z())
val Vector4fc.zzw : Vector3f
	get() = Vector3f(z(), z(), w())
var Vector4f.zwx : Vector3f
	get() = Vector3f(z(), w(), x())
	set(value) {
		z = value.x
		w = value.y
		x = value.z
	}
var Vector4f.zwy : Vector3f
	get() = Vector3f(z(), w(), y())
	set(value) {
		z = value.x
		w = value.y
		y = value.z
	}
val Vector4fc.zwz : Vector3f
	get() = Vector3f(z(), w(), z())
val Vector4fc.zww : Vector3f
	get() = Vector3f(z(), w(), w())
val Vector4fc.wxx : Vector3f
	get() = Vector3f(w(), x(), x())
var Vector4f.wxy : Vector3f
	get() = Vector3f(w(), x(), y())
	set(value) {
		w = value.x
		x = value.y
		y = value.z
	}
var Vector4f.wxz : Vector3f
	get() = Vector3f(w(), x(), z())
	set(value) {
		w = value.x
		x = value.y
		z = value.z
	}
val Vector4fc.wxw : Vector3f
	get() = Vector3f(w(), x(), w())
var Vector4f.wyx : Vector3f
	get() = Vector3f(w(), y(), x())
	set(value) {
		w = value.x
		y = value.y
		x = value.z
	}
val Vector4fc.wyy : Vector3f
	get() = Vector3f(w(), y(), y())
var Vector4f.wyz : Vector3f
	get() = Vector3f(w(), y(), z())
	set(value) {
		w = value.x
		y = value.y
		z = value.z
	}
val Vector4fc.wyw : Vector3f
	get() = Vector3f(w(), y(), w())
var Vector4f.wzx : Vector3f
	get() = Vector3f(w(), z(), x())
	set(value) {
		w = value.x
		z = value.y
		x = value.z
	}
var Vector4f.wzy : Vector3f
	get() = Vector3f(w(), z(), y())
	set(value) {
		w = value.x
		z = value.y
		y = value.z
	}
val Vector4fc.wzz : Vector3f
	get() = Vector3f(w(), z(), z())
val Vector4fc.wzw : Vector3f
	get() = Vector3f(w(), z(), w())
val Vector4fc.wwx : Vector3f
	get() = Vector3f(w(), w(), x())
val Vector4fc.wwy : Vector3f
	get() = Vector3f(w(), w(), y())
val Vector4fc.wwz : Vector3f
	get() = Vector3f(w(), w(), z())
val Vector4fc.www : Vector3f
	get() = Vector3f(w(), w(), w())
val Vector4fc.xxxx : Vector4f
	get() = Vector4f(x(), x(), x(), x())
val Vector4fc.xxxy : Vector4f
	get() = Vector4f(x(), x(), x(), y())
val Vector4fc.xxxz : Vector4f
	get() = Vector4f(x(), x(), x(), z())
val Vector4fc.xxxw : Vector4f
	get() = Vector4f(x(), x(), x(), w())
val Vector4fc.xxyx : Vector4f
	get() = Vector4f(x(), x(), y(), x())
val Vector4fc.xxyy : Vector4f
	get() = Vector4f(x(), x(), y(), y())
val Vector4fc.xxyz : Vector4f
	get() = Vector4f(x(), x(), y(), z())
val Vector4fc.xxyw : Vector4f
	get() = Vector4f(x(), x(), y(), w())
val Vector4fc.xxzx : Vector4f
	get() = Vector4f(x(), x(), z(), x())
val Vector4fc.xxzy : Vector4f
	get() = Vector4f(x(), x(), z(), y())
val Vector4fc.xxzz : Vector4f
	get() = Vector4f(x(), x(), z(), z())
val Vector4fc.xxzw : Vector4f
	get() = Vector4f(x(), x(), z(), w())
val Vector4fc.xxwx : Vector4f
	get() = Vector4f(x(), x(), w(), x())
val Vector4fc.xxwy : Vector4f
	get() = Vector4f(x(), x(), w(), y())
val Vector4fc.xxwz : Vector4f
	get() = Vector4f(x(), x(), w(), z())
val Vector4fc.xxww : Vector4f
	get() = Vector4f(x(), x(), w(), w())
val Vector4fc.xyxx : Vector4f
	get() = Vector4f(x(), y(), x(), x())
val Vector4fc.xyxy : Vector4f
	get() = Vector4f(x(), y(), x(), y())
val Vector4fc.xyxz : Vector4f
	get() = Vector4f(x(), y(), x(), z())
val Vector4fc.xyxw : Vector4f
	get() = Vector4f(x(), y(), x(), w())
val Vector4fc.xyyx : Vector4f
	get() = Vector4f(x(), y(), y(), x())
val Vector4fc.xyyy : Vector4f
	get() = Vector4f(x(), y(), y(), y())
val Vector4fc.xyyz : Vector4f
	get() = Vector4f(x(), y(), y(), z())
val Vector4fc.xyyw : Vector4f
	get() = Vector4f(x(), y(), y(), w())
val Vector4fc.xyzx : Vector4f
	get() = Vector4f(x(), y(), z(), x())
val Vector4fc.xyzy : Vector4f
	get() = Vector4f(x(), y(), z(), y())
val Vector4fc.xyzz : Vector4f
	get() = Vector4f(x(), y(), z(), z())
var Vector4f.xyzw : Vector4f
	get() = Vector4f(x(), y(), z(), w())
	set(value) {
		x = value.x
		y = value.y
		z = value.z
		w = value.w
	}
val Vector4fc.xywx : Vector4f
	get() = Vector4f(x(), y(), w(), x())
val Vector4fc.xywy : Vector4f
	get() = Vector4f(x(), y(), w(), y())
var Vector4f.xywz : Vector4f
	get() = Vector4f(x(), y(), w(), z())
	set(value) {
		x = value.x
		y = value.y
		w = value.z
		z = value.w
	}
val Vector4fc.xyww : Vector4f
	get() = Vector4f(x(), y(), w(), w())
val Vector4fc.xzxx : Vector4f
	get() = Vector4f(x(), z(), x(), x())
val Vector4fc.xzxy : Vector4f
	get() = Vector4f(x(), z(), x(), y())
val Vector4fc.xzxz : Vector4f
	get() = Vector4f(x(), z(), x(), z())
val Vector4fc.xzxw : Vector4f
	get() = Vector4f(x(), z(), x(), w())
val Vector4fc.xzyx : Vector4f
	get() = Vector4f(x(), z(), y(), x())
val Vector4fc.xzyy : Vector4f
	get() = Vector4f(x(), z(), y(), y())
val Vector4fc.xzyz : Vector4f
	get() = Vector4f(x(), z(), y(), z())
var Vector4f.xzyw : Vector4f
	get() = Vector4f(x(), z(), y(), w())
	set(value) {
		x = value.x
		z = value.y
		y = value.z
		w = value.w
	}
val Vector4fc.xzzx : Vector4f
	get() = Vector4f(x(), z(), z(), x())
val Vector4fc.xzzy : Vector4f
	get() = Vector4f(x(), z(), z(), y())
val Vector4fc.xzzz : Vector4f
	get() = Vector4f(x(), z(), z(), z())
val Vector4fc.xzzw : Vector4f
	get() = Vector4f(x(), z(), z(), w())
val Vector4fc.xzwx : Vector4f
	get() = Vector4f(x(), z(), w(), x())
var Vector4f.xzwy : Vector4f
	get() = Vector4f(x(), z(), w(), y())
	set(value) {
		x = value.x
		z = value.y
		w = value.z
		y = value.w
	}
val Vector4fc.xzwz : Vector4f
	get() = Vector4f(x(), z(), w(), z())
val Vector4fc.xzww : Vector4f
	get() = Vector4f(x(), z(), w(), w())
val Vector4fc.xwxx : Vector4f
	get() = Vector4f(x(), w(), x(), x())
val Vector4fc.xwxy : Vector4f
	get() = Vector4f(x(), w(), x(), y())
val Vector4fc.xwxz : Vector4f
	get() = Vector4f(x(), w(), x(), z())
val Vector4fc.xwxw : Vector4f
	get() = Vector4f(x(), w(), x(), w())
val Vector4fc.xwyx : Vector4f
	get() = Vector4f(x(), w(), y(), x())
val Vector4fc.xwyy : Vector4f
	get() = Vector4f(x(), w(), y(), y())
var Vector4f.xwyz : Vector4f
	get() = Vector4f(x(), w(), y(), z())
	set(value) {
		x = value.x
		w = value.y
		y = value.z
		z = value.w
	}
val Vector4fc.xwyw : Vector4f
	get() = Vector4f(x(), w(), y(), w())
val Vector4fc.xwzx : Vector4f
	get() = Vector4f(x(), w(), z(), x())
var Vector4f.xwzy : Vector4f
	get() = Vector4f(x(), w(), z(), y())
	set(value) {
		x = value.x
		w = value.y
		z = value.z
		y = value.w
	}
val Vector4fc.xwzz : Vector4f
	get() = Vector4f(x(), w(), z(), z())
val Vector4fc.xwzw : Vector4f
	get() = Vector4f(x(), w(), z(), w())
val Vector4fc.xwwx : Vector4f
	get() = Vector4f(x(), w(), w(), x())
val Vector4fc.xwwy : Vector4f
	get() = Vector4f(x(), w(), w(), y())
val Vector4fc.xwwz : Vector4f
	get() = Vector4f(x(), w(), w(), z())
val Vector4fc.xwww : Vector4f
	get() = Vector4f(x(), w(), w(), w())
val Vector4fc.yxxx : Vector4f
	get() = Vector4f(y(), x(), x(), x())
val Vector4fc.yxxy : Vector4f
	get() = Vector4f(y(), x(), x(), y())
val Vector4fc.yxxz : Vector4f
	get() = Vector4f(y(), x(), x(), z())
val Vector4fc.yxxw : Vector4f
	get() = Vector4f(y(), x(), x(), w())
val Vector4fc.yxyx : Vector4f
	get() = Vector4f(y(), x(), y(), x())
val Vector4fc.yxyy : Vector4f
	get() = Vector4f(y(), x(), y(), y())
val Vector4fc.yxyz : Vector4f
	get() = Vector4f(y(), x(), y(), z())
val Vector4fc.yxyw : Vector4f
	get() = Vector4f(y(), x(), y(), w())
val Vector4fc.yxzx : Vector4f
	get() = Vector4f(y(), x(), z(), x())
val Vector4fc.yxzy : Vector4f
	get() = Vector4f(y(), x(), z(), y())
val Vector4fc.yxzz : Vector4f
	get() = Vector4f(y(), x(), z(), z())
var Vector4f.yxzw : Vector4f
	get() = Vector4f(y(), x(), z(), w())
	set(value) {
		y = value.x
		x = value.y
		z = value.z
		w = value.w
	}
val Vector4fc.yxwx : Vector4f
	get() = Vector4f(y(), x(), w(), x())
val Vector4fc.yxwy : Vector4f
	get() = Vector4f(y(), x(), w(), y())
var Vector4f.yxwz : Vector4f
	get() = Vector4f(y(), x(), w(), z())
	set(value) {
		y = value.x
		x = value.y
		w = value.z
		z = value.w
	}
val Vector4fc.yxww : Vector4f
	get() = Vector4f(y(), x(), w(), w())
val Vector4fc.yyxx : Vector4f
	get() = Vector4f(y(), y(), x(), x())
val Vector4fc.yyxy : Vector4f
	get() = Vector4f(y(), y(), x(), y())
val Vector4fc.yyxz : Vector4f
	get() = Vector4f(y(), y(), x(), z())
val Vector4fc.yyxw : Vector4f
	get() = Vector4f(y(), y(), x(), w())
val Vector4fc.yyyx : Vector4f
	get() = Vector4f(y(), y(), y(), x())
val Vector4fc.yyyy : Vector4f
	get() = Vector4f(y(), y(), y(), y())
val Vector4fc.yyyz : Vector4f
	get() = Vector4f(y(), y(), y(), z())
val Vector4fc.yyyw : Vector4f
	get() = Vector4f(y(), y(), y(), w())
val Vector4fc.yyzx : Vector4f
	get() = Vector4f(y(), y(), z(), x())
val Vector4fc.yyzy : Vector4f
	get() = Vector4f(y(), y(), z(), y())
val Vector4fc.yyzz : Vector4f
	get() = Vector4f(y(), y(), z(), z())
val Vector4fc.yyzw : Vector4f
	get() = Vector4f(y(), y(), z(), w())
val Vector4fc.yywx : Vector4f
	get() = Vector4f(y(), y(), w(), x())
val Vector4fc.yywy : Vector4f
	get() = Vector4f(y(), y(), w(), y())
val Vector4fc.yywz : Vector4f
	get() = Vector4f(y(), y(), w(), z())
val Vector4fc.yyww : Vector4f
	get() = Vector4f(y(), y(), w(), w())
val Vector4fc.yzxx : Vector4f
	get() = Vector4f(y(), z(), x(), x())
val Vector4fc.yzxy : Vector4f
	get() = Vector4f(y(), z(), x(), y())
val Vector4fc.yzxz : Vector4f
	get() = Vector4f(y(), z(), x(), z())
var Vector4f.yzxw : Vector4f
	get() = Vector4f(y(), z(), x(), w())
	set(value) {
		y = value.x
		z = value.y
		x = value.z
		w = value.w
	}
val Vector4fc.yzyx : Vector4f
	get() = Vector4f(y(), z(), y(), x())
val Vector4fc.yzyy : Vector4f
	get() = Vector4f(y(), z(), y(), y())
val Vector4fc.yzyz : Vector4f
	get() = Vector4f(y(), z(), y(), z())
val Vector4fc.yzyw : Vector4f
	get() = Vector4f(y(), z(), y(), w())
val Vector4fc.yzzx : Vector4f
	get() = Vector4f(y(), z(), z(), x())
val Vector4fc.yzzy : Vector4f
	get() = Vector4f(y(), z(), z(), y())
val Vector4fc.yzzz : Vector4f
	get() = Vector4f(y(), z(), z(), z())
val Vector4fc.yzzw : Vector4f
	get() = Vector4f(y(), z(), z(), w())
var Vector4f.yzwx : Vector4f
	get() = Vector4f(y(), z(), w(), x())
	set(value) {
		y = value.x
		z = value.y
		w = value.z
		x = value.w
	}
val Vector4fc.yzwy : Vector4f
	get() = Vector4f(y(), z(), w(), y())
val Vector4fc.yzwz : Vector4f
	get() = Vector4f(y(), z(), w(), z())
val Vector4fc.yzww : Vector4f
	get() = Vector4f(y(), z(), w(), w())
val Vector4fc.ywxx : Vector4f
	get() = Vector4f(y(), w(), x(), x())
val Vector4fc.ywxy : Vector4f
	get() = Vector4f(y(), w(), x(), y())
var Vector4f.ywxz : Vector4f
	get() = Vector4f(y(), w(), x(), z())
	set(value) {
		y = value.x
		w = value.y
		x = value.z
		z = value.w
	}
val Vector4fc.ywxw : Vector4f
	get() = Vector4f(y(), w(), x(), w())
val Vector4fc.ywyx : Vector4f
	get() = Vector4f(y(), w(), y(), x())
val Vector4fc.ywyy : Vector4f
	get() = Vector4f(y(), w(), y(), y())
val Vector4fc.ywyz : Vector4f
	get() = Vector4f(y(), w(), y(), z())
val Vector4fc.ywyw : Vector4f
	get() = Vector4f(y(), w(), y(), w())
var Vector4f.ywzx : Vector4f
	get() = Vector4f(y(), w(), z(), x())
	set(value) {
		y = value.x
		w = value.y
		z = value.z
		x = value.w
	}
val Vector4fc.ywzy : Vector4f
	get() = Vector4f(y(), w(), z(), y())
val Vector4fc.ywzz : Vector4f
	get() = Vector4f(y(), w(), z(), z())
val Vector4fc.ywzw : Vector4f
	get() = Vector4f(y(), w(), z(), w())
val Vector4fc.ywwx : Vector4f
	get() = Vector4f(y(), w(), w(), x())
val Vector4fc.ywwy : Vector4f
	get() = Vector4f(y(), w(), w(), y())
val Vector4fc.ywwz : Vector4f
	get() = Vector4f(y(), w(), w(), z())
val Vector4fc.ywww : Vector4f
	get() = Vector4f(y(), w(), w(), w())
val Vector4fc.zxxx : Vector4f
	get() = Vector4f(z(), x(), x(), x())
val Vector4fc.zxxy : Vector4f
	get() = Vector4f(z(), x(), x(), y())
val Vector4fc.zxxz : Vector4f
	get() = Vector4f(z(), x(), x(), z())
val Vector4fc.zxxw : Vector4f
	get() = Vector4f(z(), x(), x(), w())
val Vector4fc.zxyx : Vector4f
	get() = Vector4f(z(), x(), y(), x())
val Vector4fc.zxyy : Vector4f
	get() = Vector4f(z(), x(), y(), y())
val Vector4fc.zxyz : Vector4f
	get() = Vector4f(z(), x(), y(), z())
var Vector4f.zxyw : Vector4f
	get() = Vector4f(z(), x(), y(), w())
	set(value) {
		z = value.x
		x = value.y
		y = value.z
		w = value.w
	}
val Vector4fc.zxzx : Vector4f
	get() = Vector4f(z(), x(), z(), x())
val Vector4fc.zxzy : Vector4f
	get() = Vector4f(z(), x(), z(), y())
val Vector4fc.zxzz : Vector4f
	get() = Vector4f(z(), x(), z(), z())
val Vector4fc.zxzw : Vector4f
	get() = Vector4f(z(), x(), z(), w())
val Vector4fc.zxwx : Vector4f
	get() = Vector4f(z(), x(), w(), x())
var Vector4f.zxwy : Vector4f
	get() = Vector4f(z(), x(), w(), y())
	set(value) {
		z = value.x
		x = value.y
		w = value.z
		y = value.w
	}
val Vector4fc.zxwz : Vector4f
	get() = Vector4f(z(), x(), w(), z())
val Vector4fc.zxww : Vector4f
	get() = Vector4f(z(), x(), w(), w())
val Vector4fc.zyxx : Vector4f
	get() = Vector4f(z(), y(), x(), x())
val Vector4fc.zyxy : Vector4f
	get() = Vector4f(z(), y(), x(), y())
val Vector4fc.zyxz : Vector4f
	get() = Vector4f(z(), y(), x(), z())
var Vector4f.zyxw : Vector4f
	get() = Vector4f(z(), y(), x(), w())
	set(value) {
		z = value.x
		y = value.y
		x = value.z
		w = value.w
	}
val Vector4fc.zyyx : Vector4f
	get() = Vector4f(z(), y(), y(), x())
val Vector4fc.zyyy : Vector4f
	get() = Vector4f(z(), y(), y(), y())
val Vector4fc.zyyz : Vector4f
	get() = Vector4f(z(), y(), y(), z())
val Vector4fc.zyyw : Vector4f
	get() = Vector4f(z(), y(), y(), w())
val Vector4fc.zyzx : Vector4f
	get() = Vector4f(z(), y(), z(), x())
val Vector4fc.zyzy : Vector4f
	get() = Vector4f(z(), y(), z(), y())
val Vector4fc.zyzz : Vector4f
	get() = Vector4f(z(), y(), z(), z())
val Vector4fc.zyzw : Vector4f
	get() = Vector4f(z(), y(), z(), w())
var Vector4f.zywx : Vector4f
	get() = Vector4f(z(), y(), w(), x())
	set(value) {
		z = value.x
		y = value.y
		w = value.z
		x = value.w
	}
val Vector4fc.zywy : Vector4f
	get() = Vector4f(z(), y(), w(), y())
val Vector4fc.zywz : Vector4f
	get() = Vector4f(z(), y(), w(), z())
val Vector4fc.zyww : Vector4f
	get() = Vector4f(z(), y(), w(), w())
val Vector4fc.zzxx : Vector4f
	get() = Vector4f(z(), z(), x(), x())
val Vector4fc.zzxy : Vector4f
	get() = Vector4f(z(), z(), x(), y())
val Vector4fc.zzxz : Vector4f
	get() = Vector4f(z(), z(), x(), z())
val Vector4fc.zzxw : Vector4f
	get() = Vector4f(z(), z(), x(), w())
val Vector4fc.zzyx : Vector4f
	get() = Vector4f(z(), z(), y(), x())
val Vector4fc.zzyy : Vector4f
	get() = Vector4f(z(), z(), y(), y())
val Vector4fc.zzyz : Vector4f
	get() = Vector4f(z(), z(), y(), z())
val Vector4fc.zzyw : Vector4f
	get() = Vector4f(z(), z(), y(), w())
val Vector4fc.zzzx : Vector4f
	get() = Vector4f(z(), z(), z(), x())
val Vector4fc.zzzy : Vector4f
	get() = Vector4f(z(), z(), z(), y())
val Vector4fc.zzzz : Vector4f
	get() = Vector4f(z(), z(), z(), z())
val Vector4fc.zzzw : Vector4f
	get() = Vector4f(z(), z(), z(), w())
val Vector4fc.zzwx : Vector4f
	get() = Vector4f(z(), z(), w(), x())
val Vector4fc.zzwy : Vector4f
	get() = Vector4f(z(), z(), w(), y())
val Vector4fc.zzwz : Vector4f
	get() = Vector4f(z(), z(), w(), z())
val Vector4fc.zzww : Vector4f
	get() = Vector4f(z(), z(), w(), w())
val Vector4fc.zwxx : Vector4f
	get() = Vector4f(z(), w(), x(), x())
var Vector4f.zwxy : Vector4f
	get() = Vector4f(z(), w(), x(), y())
	set(value) {
		z = value.x
		w = value.y
		x = value.z
		y = value.w
	}
val Vector4fc.zwxz : Vector4f
	get() = Vector4f(z(), w(), x(), z())
val Vector4fc.zwxw : Vector4f
	get() = Vector4f(z(), w(), x(), w())
var Vector4f.zwyx : Vector4f
	get() = Vector4f(z(), w(), y(), x())
	set(value) {
		z = value.x
		w = value.y
		y = value.z
		x = value.w
	}
val Vector4fc.zwyy : Vector4f
	get() = Vector4f(z(), w(), y(), y())
val Vector4fc.zwyz : Vector4f
	get() = Vector4f(z(), w(), y(), z())
val Vector4fc.zwyw : Vector4f
	get() = Vector4f(z(), w(), y(), w())
val Vector4fc.zwzx : Vector4f
	get() = Vector4f(z(), w(), z(), x())
val Vector4fc.zwzy : Vector4f
	get() = Vector4f(z(), w(), z(), y())
val Vector4fc.zwzz : Vector4f
	get() = Vector4f(z(), w(), z(), z())
val Vector4fc.zwzw : Vector4f
	get() = Vector4f(z(), w(), z(), w())
val Vector4fc.zwwx : Vector4f
	get() = Vector4f(z(), w(), w(), x())
val Vector4fc.zwwy : Vector4f
	get() = Vector4f(z(), w(), w(), y())
val Vector4fc.zwwz : Vector4f
	get() = Vector4f(z(), w(), w(), z())
val Vector4fc.zwww : Vector4f
	get() = Vector4f(z(), w(), w(), w())
val Vector4fc.wxxx : Vector4f
	get() = Vector4f(w(), x(), x(), x())
val Vector4fc.wxxy : Vector4f
	get() = Vector4f(w(), x(), x(), y())
val Vector4fc.wxxz : Vector4f
	get() = Vector4f(w(), x(), x(), z())
val Vector4fc.wxxw : Vector4f
	get() = Vector4f(w(), x(), x(), w())
val Vector4fc.wxyx : Vector4f
	get() = Vector4f(w(), x(), y(), x())
val Vector4fc.wxyy : Vector4f
	get() = Vector4f(w(), x(), y(), y())
var Vector4f.wxyz : Vector4f
	get() = Vector4f(w(), x(), y(), z())
	set(value) {
		w = value.x
		x = value.y
		y = value.z
		z = value.w
	}
val Vector4fc.wxyw : Vector4f
	get() = Vector4f(w(), x(), y(), w())
val Vector4fc.wxzx : Vector4f
	get() = Vector4f(w(), x(), z(), x())
var Vector4f.wxzy : Vector4f
	get() = Vector4f(w(), x(), z(), y())
	set(value) {
		w = value.x
		x = value.y
		z = value.z
		y = value.w
	}
val Vector4fc.wxzz : Vector4f
	get() = Vector4f(w(), x(), z(), z())
val Vector4fc.wxzw : Vector4f
	get() = Vector4f(w(), x(), z(), w())
val Vector4fc.wxwx : Vector4f
	get() = Vector4f(w(), x(), w(), x())
val Vector4fc.wxwy : Vector4f
	get() = Vector4f(w(), x(), w(), y())
val Vector4fc.wxwz : Vector4f
	get() = Vector4f(w(), x(), w(), z())
val Vector4fc.wxww : Vector4f
	get() = Vector4f(w(), x(), w(), w())
val Vector4fc.wyxx : Vector4f
	get() = Vector4f(w(), y(), x(), x())
val Vector4fc.wyxy : Vector4f
	get() = Vector4f(w(), y(), x(), y())
var Vector4f.wyxz : Vector4f
	get() = Vector4f(w(), y(), x(), z())
	set(value) {
		w = value.x
		y = value.y
		x = value.z
		z = value.w
	}
val Vector4fc.wyxw : Vector4f
	get() = Vector4f(w(), y(), x(), w())
val Vector4fc.wyyx : Vector4f
	get() = Vector4f(w(), y(), y(), x())
val Vector4fc.wyyy : Vector4f
	get() = Vector4f(w(), y(), y(), y())
val Vector4fc.wyyz : Vector4f
	get() = Vector4f(w(), y(), y(), z())
val Vector4fc.wyyw : Vector4f
	get() = Vector4f(w(), y(), y(), w())
var Vector4f.wyzx : Vector4f
	get() = Vector4f(w(), y(), z(), x())
	set(value) {
		w = value.x
		y = value.y
		z = value.z
		x = value.w
	}
val Vector4fc.wyzy : Vector4f
	get() = Vector4f(w(), y(), z(), y())
val Vector4fc.wyzz : Vector4f
	get() = Vector4f(w(), y(), z(), z())
val Vector4fc.wyzw : Vector4f
	get() = Vector4f(w(), y(), z(), w())
val Vector4fc.wywx : Vector4f
	get() = Vector4f(w(), y(), w(), x())
val Vector4fc.wywy : Vector4f
	get() = Vector4f(w(), y(), w(), y())
val Vector4fc.wywz : Vector4f
	get() = Vector4f(w(), y(), w(), z())
val Vector4fc.wyww : Vector4f
	get() = Vector4f(w(), y(), w(), w())
val Vector4fc.wzxx : Vector4f
	get() = Vector4f(w(), z(), x(), x())
var Vector4f.wzxy : Vector4f
	get() = Vector4f(w(), z(), x(), y())
	set(value) {
		w = value.x
		z = value.y
		x = value.z
		y = value.w
	}
val Vector4fc.wzxz : Vector4f
	get() = Vector4f(w(), z(), x(), z())
val Vector4fc.wzxw : Vector4f
	get() = Vector4f(w(), z(), x(), w())
var Vector4f.wzyx : Vector4f
	get() = Vector4f(w(), z(), y(), x())
	set(value) {
		w = value.x
		z = value.y
		y = value.z
		x = value.w
	}
val Vector4fc.wzyy : Vector4f
	get() = Vector4f(w(), z(), y(), y())
val Vector4fc.wzyz : Vector4f
	get() = Vector4f(w(), z(), y(), z())
val Vector4fc.wzyw : Vector4f
	get() = Vector4f(w(), z(), y(), w())
val Vector4fc.wzzx : Vector4f
	get() = Vector4f(w(), z(), z(), x())
val Vector4fc.wzzy : Vector4f
	get() = Vector4f(w(), z(), z(), y())
val Vector4fc.wzzz : Vector4f
	get() = Vector4f(w(), z(), z(), z())
val Vector4fc.wzzw : Vector4f
	get() = Vector4f(w(), z(), z(), w())
val Vector4fc.wzwx : Vector4f
	get() = Vector4f(w(), z(), w(), x())
val Vector4fc.wzwy : Vector4f
	get() = Vector4f(w(), z(), w(), y())
val Vector4fc.wzwz : Vector4f
	get() = Vector4f(w(), z(), w(), z())
val Vector4fc.wzww : Vector4f
	get() = Vector4f(w(), z(), w(), w())
val Vector4fc.wwxx : Vector4f
	get() = Vector4f(w(), w(), x(), x())
val Vector4fc.wwxy : Vector4f
	get() = Vector4f(w(), w(), x(), y())
val Vector4fc.wwxz : Vector4f
	get() = Vector4f(w(), w(), x(), z())
val Vector4fc.wwxw : Vector4f
	get() = Vector4f(w(), w(), x(), w())
val Vector4fc.wwyx : Vector4f
	get() = Vector4f(w(), w(), y(), x())
val Vector4fc.wwyy : Vector4f
	get() = Vector4f(w(), w(), y(), y())
val Vector4fc.wwyz : Vector4f
	get() = Vector4f(w(), w(), y(), z())
val Vector4fc.wwyw : Vector4f
	get() = Vector4f(w(), w(), y(), w())
val Vector4fc.wwzx : Vector4f
	get() = Vector4f(w(), w(), z(), x())
val Vector4fc.wwzy : Vector4f
	get() = Vector4f(w(), w(), z(), y())
val Vector4fc.wwzz : Vector4f
	get() = Vector4f(w(), w(), z(), z())
val Vector4fc.wwzw : Vector4f
	get() = Vector4f(w(), w(), z(), w())
val Vector4fc.wwwx : Vector4f
	get() = Vector4f(w(), w(), w(), x())
val Vector4fc.wwwy : Vector4f
	get() = Vector4f(w(), w(), w(), y())
val Vector4fc.wwwz : Vector4f
	get() = Vector4f(w(), w(), w(), z())
val Vector4fc.wwww : Vector4f
	get() = Vector4f(w(), w(), w(), w())

fun Vector4f(xyz : Vector3f, w : Float) = Vector4f(xyz.x, xyz.y, xyz.z, w)
fun Vector4f(x : Float, yzw : Vector3f) = Vector4f(x, yzw.x, yzw.y, yzw.z)
fun Vector4f(xy : Vector2f, zw : Vector2f) = Vector4f(xy.x, xy.y, zw.x, zw.y)
fun Vector4f(xy : Vector2f, z : Float, w : Float) = Vector4f(xy.x, xy.y, z, w)
fun Vector4f(x : Float, yz : Vector2f, w : Float) = Vector4f(x, yz.x, yz.y, w)
fun Vector4f(x : Float, y : Float, zw : Vector2f) = Vector4f(x, y, zw.x, zw.y)
