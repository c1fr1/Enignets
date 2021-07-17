@file:Suppress("unused")

package engine.opengl.jomlExtensions

import org.joml.*
import org.joml.Math.sin

operator fun Vector2fc.plus(inc : Vector2fc) : Vector2f = add(inc, Vector2f())
operator fun Vector2fc.minus(inc : Vector2fc) : Vector2f = sub(inc, Vector2f())
operator fun Vector2fc.times(scalar : Float) : Vector2f = mul(scalar, Vector2f())
operator fun Vector2fc.times(v : Vector2fc) : Vector2f = mul(v, Vector2f())
operator fun Vector2fc.times(mat : Matrix2fc) : Vector2f = mul(mat, Vector2f())
operator fun Vector2fc.times(mat : Matrix2dc) : Vector2f = mul(mat, Vector2f())
operator fun Vector2fc.div(scalar : Float) : Vector2f = div(scalar, Vector2f())
operator fun Vector2fc.div(v : Vector2fc) : Vector2f = div(v, Vector2f())
operator fun Vector2f.plusAssign(inc : Vector2fc) {add(inc)}
operator fun Vector2f.minusAssign(inc : Vector2fc) {sub(inc)}
operator fun Vector2f.timesAssign(scalar : Float) {mul(scalar)}
operator fun Vector2f.timesAssign(v : Vector2fc) {mul(v)}
operator fun Vector2f.timesAssign(mat : Matrix2fc) {mul(mat)}
operator fun Vector2f.timesAssign(mat : Matrix2dc) {mul(mat)}
operator fun Vector2f.divAssign(scalar : Float) {div(scalar)}
operator fun Vector2f.divAssign(v : Vector2fc) {div(v)}

operator fun Vector2dc.plus(inc : Vector2dc) : Vector2d = add(inc, Vector2d())
operator fun Vector2dc.plus(inc : Vector2fc) : Vector2d = add(inc, Vector2d())
operator fun Vector2dc.minus(inc : Vector2dc) : Vector2d = sub(inc, Vector2d())
operator fun Vector2dc.minus(inc : Vector2fc) : Vector2d = sub(inc, Vector2d())
operator fun Vector2dc.times(scalar : Double) : Vector2d = mul(scalar, Vector2d())
operator fun Vector2dc.times(v : Vector2dc) : Vector2d = mul(v, Vector2d())
operator fun Vector2dc.times(mat : Matrix2fc) : Vector2d = mul(mat, Vector2d())
operator fun Vector2dc.times(mat : Matrix2dc) : Vector2d = mul(mat, Vector2d())
operator fun Vector2dc.div(scalar : Double) : Vector2d = div(scalar, Vector2d())
operator fun Vector2dc.div(v : Vector2dc) : Vector2d = div(v, Vector2d())
operator fun Vector2dc.div(v : Vector2fc) : Vector2d = div(v, Vector2d())
operator fun Vector2d.plusAssign(inc : Vector2dc) {add(inc)}
operator fun Vector2d.plusAssign(inc : Vector2fc) {add(inc)}
operator fun Vector2d.minusAssign(inc : Vector2dc) {sub(inc)}
operator fun Vector2d.minusAssign(inc : Vector2fc) {sub(inc)}
operator fun Vector2d.timesAssign(scalar : Double) {mul(scalar)}
operator fun Vector2d.timesAssign(v : Vector2dc) {mul(v)}
operator fun Vector2d.timesAssign(mat : Matrix2fc) {mul(mat)}
operator fun Vector2d.timesAssign(mat : Matrix2dc) {mul(mat)}
operator fun Vector2d.divAssign(scalar : Double) {div(scalar)}
operator fun Vector2d.divAssign(v : Vector2dc) {div(v)}
operator fun Vector2d.divAssign(v : Vector2fc) {div(v)}

operator fun Vector2ic.plus(inc : Vector2ic) : Vector2i = add(inc, Vector2i())
operator fun Vector2ic.minus(inc : Vector2ic) : Vector2i = sub(inc, Vector2i())
operator fun Vector2ic.times(scalar : Int) : Vector2i = mul(scalar, Vector2i())
operator fun Vector2ic.times(v : Vector2ic) : Vector2i = mul(v, Vector2i())
operator fun Vector2ic.div(scalar : Int) : Vector2i = div(scalar, Vector2i())
operator fun Vector2ic.div(scalar : Float) : Vector2i = div(scalar, Vector2i())
operator fun Vector2i.plusAssign(inc : Vector2ic) {add(inc)}
operator fun Vector2i.minusAssign(inc : Vector2ic) {sub(inc)}
operator fun Vector2i.timesAssign(scalar : Int) {mul(scalar)}
operator fun Vector2i.timesAssign(v : Vector2ic) {mul(v)}
operator fun Vector2i.divAssign(scalar : Int) {div(scalar)}
operator fun Vector2i.divAssign(scalar : Float) {div(scalar)}

operator fun Vector3fc.plus(inc : Vector3fc) : Vector3f = add(inc, Vector3f())
operator fun Vector3fc.minus(inc : Vector3fc) : Vector3f = sub(inc, Vector3f())
operator fun Vector3fc.times(scalar : Float) : Vector3f = mul(scalar, Vector3f())
operator fun Float.times(vec : Vector3fc) : Vector3f = vec * this
operator fun Vector3fc.times(v : Vector3fc) : Vector3f = mul(v, Vector3f())
operator fun Vector3fc.times(mat : Matrix3fc) : Vector3f = mul(mat, Vector3f())
operator fun Vector3fc.times(mat : Matrix4fc) : Vector3f = mulProject(mat, Vector3f())
operator fun Vector3fc.times(mat : Matrix3x2fc) : Vector3f = mul(mat, Vector3f())
operator fun Vector3fc.times(mat : Matrix3dc) : Vector3f = mul(mat, Vector3f())
operator fun Vector3fc.div(scalar : Float) : Vector3f = div(scalar, Vector3f())
operator fun Vector3fc.div(v : Vector3fc) : Vector3f = div(v, Vector3f())
infix fun Vector3fc.dot(o : Vector3fc) = dot(o)
infix fun Vector3fc.cross(o : Vector3fc) : Vector3f = cross(o, Vector3f())
operator fun Vector3f.plusAssign(inc : Vector3fc) {add(inc)}
operator fun Vector3f.minusAssign(inc : Vector3fc) {sub(inc)}
operator fun Vector3f.timesAssign(scalar : Float) {mul(scalar)}
operator fun Vector3f.timesAssign(v : Vector3fc) {mul(v)}
operator fun Vector3f.timesAssign(mat : Matrix3fc) {mul(mat)}
operator fun Vector3f.timesAssign(mat : Matrix4fc) {mulProject(mat)}
operator fun Vector3f.timesAssign(mat : Matrix3x2fc) {mul(mat)}
operator fun Vector3f.timesAssign(mat : Matrix3dc) {mul(mat)}
operator fun Vector3f.divAssign(scalar : Float) {div(scalar)}
operator fun Vector3f.divAssign(v : Vector3fc) {div(v)}

operator fun Vector3dc.plus(inc : Vector3dc) : Vector3d = add(inc, Vector3d())
operator fun Vector3dc.plus(inc : Vector3fc) : Vector3d = add(inc, Vector3d())
operator fun Vector3dc.minus(inc : Vector3dc) : Vector3d = sub(inc, Vector3d())
operator fun Vector3dc.minus(inc : Vector3fc) : Vector3d = sub(inc, Vector3d())
operator fun Vector3dc.times(scalar : Double) : Vector3d = mul(scalar, Vector3d())
operator fun Vector3dc.times(v : Vector3dc) : Vector3d = mul(v, Vector3d())
operator fun Vector3dc.times(v : Vector3fc) : Vector3d = mul(v, Vector3d())
operator fun Vector3dc.times(mat : Matrix3fc) : Vector3d = mul(mat, Vector3d())
operator fun Vector3dc.times(mat : Matrix4fc) : Vector3d = mulProject(mat, Vector3d())
operator fun Vector3dc.times(mat : Matrix3dc) : Vector3d = mul(mat, Vector3d())
operator fun Vector3dc.times(mat : Matrix4dc) : Vector3d = mulProject(mat, Vector3d())
operator fun Vector3dc.times(mat : Matrix3x2fc) : Vector3d = mul(mat, Vector3d())
operator fun Vector3dc.times(mat : Matrix3x2dc) : Vector3d = mul(mat, Vector3d())
operator fun Vector3dc.div(scalar : Double) : Vector3d = div(scalar, Vector3d())
operator fun Vector3dc.div(v : Vector3dc) : Vector3d = div(v, Vector3d())
operator fun Vector3dc.div(v : Vector3fc) : Vector3d = div(v, Vector3d())
operator fun Vector3d.plusAssign(inc : Vector3dc) {add(inc)}
operator fun Vector3d.plusAssign(inc : Vector3fc) {add(inc)}
operator fun Vector3d.minusAssign(inc : Vector3dc) {sub(inc)}
operator fun Vector3d.minusAssign(inc : Vector3fc) {sub(inc)}
operator fun Vector3d.timesAssign(scalar : Double) {mul(scalar)}
operator fun Vector3d.timesAssign(v : Vector3dc) {mul(v)}
operator fun Vector3d.timesAssign(v : Vector3fc) {mul(v)}
operator fun Vector3d.timesAssign(mat : Matrix3fc) {mul(mat)}
operator fun Vector3d.timesAssign(mat : Matrix4fc) {mulProject(mat)}
operator fun Vector3d.timesAssign(mat : Matrix3dc) {mul(mat)}
operator fun Vector3d.timesAssign(mat : Matrix4dc) {mulProject(mat)}
operator fun Vector3d.timesAssign(mat : Matrix3x2fc) {mul(mat)}
operator fun Vector3d.timesAssign(mat : Matrix3x2dc) {mul(mat)}
operator fun Vector3d.divAssign(scalar : Double) {div(scalar)}
operator fun Vector3d.divAssign(v : Vector3dc) {div(v)}
operator fun Vector3d.divAssign(v : Vector3fc) {div(v)}

operator fun Vector3ic.plus(inc : Vector3ic) : Vector3i = add(inc, Vector3i())
operator fun Vector3ic.minus(inc : Vector3ic) : Vector3i = sub(inc, Vector3i())
operator fun Vector3ic.times(scalar : Int) : Vector3i = mul(scalar, Vector3i())
operator fun Vector3ic.times(v : Vector3ic) : Vector3i = mul(v, Vector3i())
operator fun Vector3ic.div(scalar : Int) : Vector3i = div(scalar, Vector3i())
operator fun Vector3ic.div(scalar : Float) : Vector3i = div(scalar, Vector3i())
operator fun Vector3i.plusAssign(inc : Vector3ic) {add(inc)}
operator fun Vector3i.minusAssign(inc : Vector3ic) {sub(inc)}
operator fun Vector3i.timesAssign(scalar : Int) {mul(scalar)}
operator fun Vector3i.timesAssign(v : Vector3ic) {mul(v)}
operator fun Vector3i.divAssign(scalar : Int) {div(scalar)}
operator fun Vector3i.divAssign(scalar : Float) {div(scalar)}

operator fun Vector4fc.plus(inc : Vector4fc) : Vector4f = add(inc, Vector4f())
operator fun Vector4fc.minus(inc : Vector4fc) : Vector4f = sub(inc, Vector4f())
operator fun Vector4fc.times(scalar : Float) : Vector4f = mul(scalar, Vector4f())
operator fun Vector4fc.times(v : Vector4fc) : Vector4f = mul(v, Vector4f())
operator fun Vector4fc.times(mat : Matrix4fc) : Vector4f = mul(mat, Vector4f())
operator fun Vector4fc.div(scalar : Float) : Vector4f = div(scalar, Vector4f())
operator fun Vector4fc.div(v : Vector4fc) : Vector4f = div(v, Vector4f())
operator fun Vector4f.plusAssign(inc : Vector4fc) {add(inc)}
operator fun Vector4f.minusAssign(inc : Vector4fc) {sub(inc)}
operator fun Vector4f.timesAssign(scalar : Float) {mul(scalar)}
operator fun Vector4f.timesAssign(v : Vector4fc) {mul(v)}
operator fun Vector4f.timesAssign(mat : Matrix4fc) {mul(mat)}
operator fun Vector4f.timesAssign(mat : Matrix4x3fc) {mul(mat)}
operator fun Vector4f.divAssign(scalar : Float) {div(scalar)}
operator fun Vector4f.divAssign(v : Vector4fc) {div(v)}

operator fun Vector4dc.plus(inc : Vector4dc) : Vector4d = add(inc, Vector4d())
operator fun Vector4dc.plus(inc : Vector4fc) : Vector4d = add(inc, Vector4d())
operator fun Vector4dc.minus(inc : Vector4dc) : Vector4d = sub(inc, Vector4d())
operator fun Vector4dc.minus(inc : Vector4fc) : Vector4d = sub(inc, Vector4d())
operator fun Vector4dc.times(scalar : Double) : Vector4d = mul(scalar, Vector4d())
operator fun Vector4dc.times(v : Vector4dc) : Vector4d = mul(v, Vector4d())
operator fun Vector4dc.times(v : Vector4fc) : Vector4d = mul(v, Vector4d())
operator fun Vector4dc.times(mat : Matrix4fc) : Vector4d = mul(mat, Vector4d())
operator fun Vector4dc.div(scalar : Double) : Vector4d = div(scalar, Vector4d())
operator fun Vector4dc.div(v : Vector4dc) : Vector4d = div(v, Vector4d())
operator fun Vector4d.plusAssign(inc : Vector4dc)  {add(inc)}
operator fun Vector4d.plusAssign(inc : Vector4fc) {add(inc)}
operator fun Vector4d.minusAssign(inc : Vector4dc) {sub(inc)}
operator fun Vector4d.minusAssign(inc : Vector4fc) {sub(inc)}
operator fun Vector4d.timesAssign(scalar : Double) {mul(scalar)}
operator fun Vector4d.timesAssign(v : Vector4dc) {mul(v)}
operator fun Vector4d.timesAssign(v : Vector4fc) {mul(v)}
operator fun Vector4d.timesAssign(mat : Matrix4fc) {mul(mat)}
operator fun Vector4d.timesAssign(mat : Matrix4x3fc) {mul(mat)}
operator fun Vector4d.divAssign(scalar : Double) {div(scalar)}
operator fun Vector4d.divAssign(v : Vector4dc) {div(v)}

operator fun Vector4ic.plus(inc : Vector4ic) : Vector4i = add(inc, Vector4i())
operator fun Vector4ic.minus(inc : Vector4ic) : Vector4i = sub(inc, Vector4i())
operator fun Vector4ic.times(scalar : Int) : Vector4i = mul(scalar, Vector4i())
operator fun Vector4ic.times(v : Vector4ic) : Vector4i = mul(v, Vector4i())
operator fun Vector4ic.div(scalar : Int) : Vector4i = div(scalar, Vector4i())
operator fun Vector4ic.div(scalar : Float) : Vector4i = div(scalar, Vector4i())
operator fun Vector4i.plusAssign(inc : Vector4ic) {add(inc)}
operator fun Vector4i.minusAssign(inc : Vector4ic) {sub(inc)}
operator fun Vector4i.timesAssign(scalar : Int) {mul(scalar)}
operator fun Vector4i.timesAssign(v : Vector4ic) {mul(v)}
operator fun Vector4i.divAssign(scalar : Int) {div(scalar)}
operator fun Vector4i.divAssign(scalar : Float) {div(scalar)}

operator fun Matrix2fc.plus(inc : Matrix2fc) : Matrix2f = add(inc, Matrix2f())
operator fun Matrix2fc.minus(inc : Matrix2fc) : Matrix2f = sub(inc, Matrix2f())
operator fun Matrix2fc.times(right : Matrix2fc) : Matrix2f = mul(right, Matrix2f())
operator fun Matrix2f.plusAssign(inc : Matrix2fc) {add(inc)}
operator fun Matrix2f.minusAssign(inc : Matrix2fc) {sub(inc)}
operator fun Matrix2f.timesAssign(right : Matrix2fc) {mul(right)}

operator fun Matrix2dc.plus(inc : Matrix2dc) : Matrix2d = add(inc, Matrix2d())
operator fun Matrix2dc.minus(inc : Matrix2dc) : Matrix2d = sub(inc, Matrix2d())
operator fun Matrix2dc.times(right : Matrix2dc) : Matrix2d = mul(right, Matrix2d())
operator fun Matrix2d.plusAssign(inc : Matrix2dc) {add(inc)}
operator fun Matrix2d.minusAssign(inc : Matrix2dc) {sub(inc)}
operator fun Matrix2d.timesAssign(right : Matrix2dc) {mul(right)}

operator fun Matrix3fc.plus(inc : Matrix3fc) : Matrix3f = add(inc, Matrix3f())
operator fun Matrix3fc.minus(inc : Matrix3fc) : Matrix3f = sub(inc, Matrix3f())
operator fun Matrix3fc.times(right : Matrix3fc) : Matrix3f = mul(right, Matrix3f())
operator fun Matrix3f.plusAssign(inc : Matrix3fc) {add(inc)}
operator fun Matrix3f.minusAssign(inc : Matrix3fc) {sub(inc)}
operator fun Matrix3f.timesAssign(right : Matrix3fc) {mul(right)}

operator fun Matrix3dc.plus(inc : Matrix3dc) : Matrix3d = add(inc, Matrix3d())
operator fun Matrix3dc.minus(inc : Matrix3dc) : Matrix3d = sub(inc, Matrix3d())
operator fun Matrix3dc.times(right : Matrix3dc) : Matrix3d = mul(right, Matrix3d())
operator fun Matrix3d.plusAssign(inc : Matrix3dc) {add(inc)}
operator fun Matrix3d.minusAssign(inc : Matrix3dc) {sub(inc)}
operator fun Matrix3d.timesAssign(right : Matrix3dc) {mul(right)}

operator fun Matrix3x2fc.times(right : Matrix3x2fc) : Matrix3x2f = mul(right, Matrix3x2f())
operator fun Matrix3x2f.timesAssign(right : Matrix3x2fc) {mul(right)}

operator fun Matrix3x2dc.times(right : Matrix3x2dc) : Matrix3x2d = mul(right, Matrix3x2d())
operator fun Matrix3x2d.timesAssign(right : Matrix3x2dc) {mul(right)}

operator fun Matrix4fc.plus(inc : Matrix4fc) : Matrix4f = add(inc, Matrix4f())
operator fun Matrix4fc.minus(inc : Matrix4fc) : Matrix4f = sub(inc, Matrix4f())
operator fun Matrix4fc.times(right : Matrix4fc) : Matrix4f = mul(right, Matrix4f())
operator fun Matrix4f.plusAssign(inc : Matrix4fc) {add(inc)}
operator fun Matrix4f.minusAssign(inc : Matrix4fc) {sub(inc)}
operator fun Matrix4f.timesAssign(right : Matrix4fc) {mul(right)}

operator fun Matrix4dc.plus(inc : Matrix4dc) : Matrix4d = add(inc, Matrix4d())
operator fun Matrix4dc.minus(inc : Matrix4dc) : Matrix4d = sub(inc, Matrix4d())
operator fun Matrix4dc.times(right : Matrix4dc) : Matrix4d = mul(right, Matrix4d())
operator fun Matrix4d.plusAssign(inc : Matrix4dc) {add(inc)}
operator fun Matrix4d.minusAssign(inc : Matrix4dc) {sub(inc)}
operator fun Matrix4d.timesAssign(right : Matrix4dc) {mul(right)}

operator fun Matrix4x3fc.times(right : Matrix4x3fc) : Matrix4x3f = mul(right, Matrix4x3f())
operator fun Matrix4x3f.timesAssign(right : Matrix4x3fc) {mul(right)}

operator fun Matrix4x3dc.times(right : Matrix4x3dc) : Matrix4x3d = mul(right, Matrix4x3d())
operator fun Matrix4x3d.timesAssign(right : Matrix4x3dc) {mul(right)}

