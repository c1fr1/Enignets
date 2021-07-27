package engine.opengl.shaders

import engine.opengl.jomlExtensions.toFloatArray
import engine.opengl.jomlExtensions.toIntArray
import engine.opengl.jomlExtensions.getF
import org.lwjgl.opengl.GL30.*

import engine.opengl.shaders.ShaderDataClass.*
import org.joml.*

private fun Boolean.toInt() : Int = if (this) 1 else 0

@Suppress("EXPERIMENTAL_API_USAGE")
class Uniform(uniform: ShaderUniform, program: Int) : ShaderUniform(uniform.name, uniform.type) {
	private val pos : Int = glGetUniformLocation(program, uniform.name)

	/**
	 * sets the float uniform to the info
	 * @param info input
	 */
	fun set(info : Float) {
		type.checkType(Vec1)
		glUniform1f(pos, info)

	}

	/**
	 * sets the value of a float array in a shader.
	 * @param info array to be set in the shader
	 */
	fun set(info: FloatArray) {
		type.checkType(Vec1, info.size)
		glUniform1fv(pos, info)
	}

	fun set(info : Double) = set(info.toFloat())

	fun set(info : DoubleArray) = set(FloatArray(info.size) {info[it].toFloat()})

	/**
	 * sets the int uniform to the info
	 * @param info input
	 */
	fun set(info: Int) {
		type.checkType(IVec1)
		glUniform1i(pos, info)
	}

	/**
	 * sets the value of a int array in a shader.
	 * @param info array to be set in the shader
	 */
	fun set(info: IntArray) {
		type.checkType(IVec1, info.size)
		glUniform1iv(pos, info)
	}

	fun set(info : UInt) {
		type.checkType(UVec1)
		glUniform1ui(pos, info.toInt())
	}

	fun set(info : UIntArray) {
		type.checkType(UVec1)
		glUniform1uiv(pos, info.toIntArray())
	}

	fun set(info : Boolean) = set(info.toInt())

	fun set(info : BooleanArray) = set(IntArray(info.size) {info[it].toInt()})

	fun set(infoA : Float, infoB : Float) {
		type.checkType(Vec2)
		glUniform2f(pos, infoA, infoB)
	}

	/**
	 * sets the vec2 uniform to the info
	 * @param info input
	 */
	fun set(info : Vector2fc) = set(info.x(), info.y())

	fun set(info : Array<Vector2fc>) {
		type.checkType(Vec2, info.size)
		glUniform2fv(pos, info.toFloatArray())
	}

	fun set(infoA : Double, infoB : Double) = set(infoA.toFloat(), infoB.toFloat())

	fun set(info : Vector2dc) = set(info.x(), info.y())

	fun set(info : Array<Vector2dc>) {
		type.checkType(DVec2, info.size)
		glUniform2fv(pos, info.getF())
	}

	fun set(infoA : Int, infoB : Int) {
		type.checkType(IVec2)
		glUniform2i(pos, infoA, infoB)
	}

	fun set(info : Vector2ic) = set(info.x(), info.y())

	fun set(info : Array<Vector2ic>) {
		type.checkType(IVec2, info.size)
		glUniform2iv(pos, info.toIntArray())
	}

	fun set(infoA : UInt, infoB : UInt) {
		type.checkType(UVec2)
		glUniform2ui(pos, infoA.toInt(), infoB.toInt())
	}
	//arrays of unsigned vectors not supported because JOML doesn't have an unsigned vector class

	fun set(infoA : Boolean, infoB : Boolean) = set(infoA.toInt(), infoB.toInt())
	//arrays of boolean vectors not supported because...
	//WHY ARE YOU USING VECTORS OF BOOLEANS ANYWAY? JUST USE A LIST OF BOOLEANS! OR INTS!

	fun set(infoA: Float, infoB: Float, infoC: Float) {
		type.checkType(Vec3)
		glUniform3f(pos, infoA, infoB, infoC)
	}

	/**
	 * sets the vec3 uniform to the info
	 * @param info input
	 */
	fun set(info: Vector3fc) = set(info.x(), info.y(), info.z())

	fun set(info: Array<Vector3fc>) {
		type.checkType(Vec3, info.size)
		glUniform3fv(pos, info.toFloatArray())
	}

	fun set(infoA : Double, infoB : Double, infoC : Double) = set(infoA.toFloat(), infoB.toFloat(), infoC.toFloat())

	fun set(info : Vector3dc) = set(info.x(), info.y(), info.z())

	fun set(info : Array<Vector3dc>) {
		type.checkType(DVec2, info.size)
		glUniform3fv(pos, info.getF())
	}

	fun set(infoA : Int, infoB : Int, infoC : Int) {
		type.checkType(IVec3)
		glUniform3i(pos, infoA, infoB, infoC)
	}

	fun set(info : Vector3ic) = set(info.x(), info.y(), info.z())

	fun set(info : Array<Vector3ic>) {
		type.checkType(IVec3, info.size)
		glUniform3iv(pos, info.toIntArray())
	}

	fun set(infoA : UInt, infoB : UInt, infoC : UInt) {
		type.checkType(IVec3)
		glUniform3ui(pos, infoA.toInt(), infoB.toInt(), infoC.toInt())
	}

	fun set(infoA : Boolean, infoB : Boolean, infoC : Boolean) = set(infoA.toInt(), infoB.toInt(), infoC.toInt())

	fun set(infoA: Float, infoB: Float, infoC: Float, infoD: Float) {
		type.checkType(Vec4)
		glUniform4f(pos, infoA, infoB, infoC, infoD)
	}

	/**
	 * sets the vec4 uniform to the info
	 * @param info input
	 */
	fun set(info: Vector4fc) = set(info.x(), info.y(), info.z(), info.w())

	fun set(info: Array<Vector4fc>) {
		type.checkType(Vec4, info.size)
		glUniform4fv(pos, info.toFloatArray())
	}

	fun set(infoA: Double, infoB: Double, infoC: Double, infoD: Double) =
		set(infoA.toFloat(), infoB.toFloat(), infoC.toFloat(), infoD.toFloat())

	fun set(info: Vector4dc) = set(info.x(), info.y(), info.z(), info.w())

	fun set(info: Array<Vector4dc>) {
		type.checkType(DVec4, info.size)
		glUniform4fv(pos, info.getF())
	}

	fun set(infoA : Int, infoB : Int, infoC : Int, infoD : Int) {
		type.checkType(IVec4)
		glUniform4i(pos, infoA, infoB, infoC, infoD)
	}

	fun set(info : Vector4ic) = set(info.x(), info.y(), info.z(), info.w())

	fun set(info : Array<Vector4ic>) {
		type.checkType(IVec4, info.size)
		glUniform4iv(pos, info.toIntArray())
	}

	fun set(infoA: UInt, infoB: UInt, infoC: UInt, infoD: UInt) {
		type.checkType(UVec4)
		glUniform4ui(pos, infoA.toInt(), infoB.toInt(), infoC.toInt(), infoD.toInt())
	}

	fun set(infoA : Boolean, infoB : Boolean, infoC : Boolean, infoD : Boolean) =
		set(infoA.toInt(), infoB.toInt(), infoC.toInt(), infoD.toInt())

	fun set(info: Matrix2fc) {
		type.checkType(Mat2)
		glUniformMatrix2fv(pos, false, info.get(FloatArray(4)))
	}

	fun set(info: Array<Matrix2fc>) {
		type.checkType(Mat2, info.size)
		glUniformMatrix2fv(pos, false, info.toFloatArray())
	}

	fun set(info: Matrix3fc) {
		type.checkType(Mat3)
		glUniformMatrix3fv(pos, false, info.get(FloatArray(9)))
	}

	fun set(info: Array<Matrix3fc>) {
		type.checkType(Mat3, info.size)
		glUniformMatrix3fv(pos, false, info.toFloatArray())
	}

	fun set(info: Matrix3x2fc) {
		type.checkType(Mat3x2)
		glUniformMatrix3x2fv(pos, false, info.get(FloatArray(6)))
	}

	fun set(info: Array<Matrix3x2fc>) {
		type.checkType(Mat3x2, info.size)
		glUniformMatrix3x2fv(pos, false, info.toFloatArray())
	}

	fun set(info: Matrix4fc) {
		type.checkType(Mat4)
		glUniformMatrix4fv(pos, false, info.get(FloatArray(16)))
	}

	fun set(info: Array<Matrix4fc>) {
		type.checkType(Mat4, info.size)
		glUniformMatrix4fv(pos, false, info.toFloatArray())
	}

	fun set(info: Array<Matrix4f>) {
		type.checkType(Mat4, info.size)
		glUniformMatrix4fv(pos, false, info.toFloatArray())
	}

	fun set(info: Matrix4x3fc) {
		type.checkType(Mat4x3)
		glUniformMatrix4x3fv(pos, false, info.get(FloatArray(12)))
	}

	fun set(info: Array<Matrix4x3fc>) {
		type.checkType(Mat4x3, info.size)
		glUniformMatrix4x3fv(pos, false, info.toFloatArray())
	}

	fun set(info: Matrix2dc) {
		type.checkType(DMat2)
		glUniformMatrix2fv(pos, false, info.getF())
	}

	fun set(info: Array<Matrix2dc>) {
		type.checkType(DMat2, info.size)
		glUniformMatrix2fv(pos, false, info.getF())
	}

	fun set(info: Matrix3dc) {
		type.checkType(DMat3)
		glUniformMatrix3fv(pos, false, info.get(FloatArray(9)))
	}

	fun set(info: Array<Matrix3dc>) {
		type.checkType(DMat3, info.size)
		glUniformMatrix3fv(pos, false, info.getF())
	}

	fun set(info: Matrix3x2dc) {
		type.checkType(DMat3x2)
		glUniformMatrix3x2fv(pos, false, info.getF())
	}

	fun set(info: Array<Matrix3x2dc>) {
		type.checkType(DMat3x2, info.size)
		glUniformMatrix3x2fv(pos, false, info.getF())
	}

	fun set(info: Matrix4dc) {
		type.checkType(DMat4)
		glUniformMatrix4fv(pos, false, info.get(FloatArray(16)))
	}

	fun set(info: Array<Matrix4dc>) {
		type.checkType(DMat4, info.size)
		glUniformMatrix4fv(pos, false, info.getF())
	}

	fun set(info: Matrix4x3dc) {
		type.checkType(DMat4)
		glUniformMatrix4fv(pos, false, info.get(FloatArray(12)))
	}

	fun set(info: Array<Matrix4x3dc>) {
		type.checkType(DMat4, info.size)
		glUniformMatrix4fv(pos, false, info.getF())
	}
}