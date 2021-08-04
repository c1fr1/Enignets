@file:Suppress("SpellCheckingInspection", "unused")

package engine

import engine.opengl.bufferObjects.VBO
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.assimp.AIBone
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AIScene
import org.lwjgl.assimp.Assimp.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Math.cbrt
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import kotlin.math.*

const val TAU = 2 * PI
const val PIf = PI.toFloat()
const val TAUf = TAU.toFloat()

/**
 * prints out the matrix
 * @param m matrix to print out
 */
fun printMatrix(m: Matrix4f) {
	println("[${m.m00()} ${m.m01()} ${m.m02()} ${m.m03()}]\n" +
			"[${m.m10()} ${m.m11()} ${m.m12()} ${m.m13()}]\n" +
			"[${m.m20()} ${m.m21()} ${m.m22()} ${m.m23()}]\n" +
			"[${m.m30()} ${m.m31()} ${m.m32()} ${m.m33()}]")
}

/**
 * find the axis of rotations based on a matrix
 * @param mat matrix to reverse engineer
 * @return xpos = rotation around xpos axis, same for ypos and z
 */
fun getEulerAngles(mat: Matrix4f): Vector3f {
	val ysin = mat.m20().coerceIn(-0.99999f, 0.99999f)
	val y = asin(ysin)
	val ycos = cos(y)
	val xcos = (mat.m22() / ycos).coerceIn(-0.99999f, 0.99999f)
	val xsin = -mat.m21() / ycos
	val zcos = (mat.m00() / ycos).coerceIn(-0.99999f, 0.99999f)
	val zsin = -mat.m10() / ycos
	var x = acos(xcos)
	var z = acos(zcos)
	if (xsin < 0) {
		x = TAU.toFloat() - x
	}
	if (zsin < 0) {
		z = TAU.toFloat() - z
	}
	return Vector3f(getAngle(x), getAngle(y), getAngle(z))
}

/**
 * finds the angle withing 0 and tau
 * @param a value
 * @return angle value
 */
fun getAngle(a: Float): Float {
	var ret = a
	while (ret < 0) {
		ret += TAU.toFloat()
	}
	while (ret > TAU) {
		ret -= TAU.toFloat()
	}
	return ret
}

/**
 * prints out an array of floats
 * @param arr array of floats
 */
fun printArray(arr: FloatArray) {
	print("[")
	for (f in arr) {
		print("$f, ")
	}
	println("]")
}

/**
 * prints out an integer array
 * @param arr
 */
fun printArray(arr: IntArray) {
	print("[")
	for (f in arr) {
		print("$f, ")
	}
	println("]")
}

/**
 * rounds a float to a certain amount of decimal places
 * @param x original float
 * @param decPlaces number of decimal places
 * @return new float
 */
fun round(x: Float, decPlaces: Int): Float {
	val factor = 10.0.pow(decPlaces.toDouble()).toFloat()
	return (x * factor).roundToInt() / factor
}

/**
 * rounds a double to a certain amount of decimal places
 * @param x original double
 * @param decPlaces number of decimal places
 * @return new double
 */
fun round(x: Double, decPlaces: Int): Double {
	val factor = 10.0.pow(decPlaces.toDouble())
	return (x * factor).roundToInt() / factor
}

/**
 * formats a float so that it fits into a certain amount of characters asa string
 * @param x original float
 * @param room number of characters
 * @return string representing the float input
 */
fun format(x: Float, room: Int): String {
	var ret = if (x < 0) "${round(x, room - 2)}" else "${round(x, room - 1)}"
	if (!ret.contains(".") && ret.length + 1 < room) {
		ret += "."
	}
	while (ret.length < room) {
		ret += "0"
	}
	return ret
}

fun compareAngles(angleA: Float, angleB: Float): Float {
	val a = getAngle(angleA)
	val b = getAngle(angleB)
	var ret = abs(a - b)
	ret = if (a < Math.PI) {
		ret.coerceAtMost(abs(a - b + TAU.toFloat()))
	} else {
		ret.coerceAtMost(abs(a - b - TAU.toFloat()))
	}
	return ret
}

fun resourceExists(path : String) : Boolean = {}.javaClass.classLoader.getResourceAsStream(path) != null

fun getResource(path : String) : BufferedReader = BufferedReader(InputStreamReader(getResourceStream(path)))

fun getResourceStream(path : String) : InputStream = {}.javaClass.classLoader.getResourceAsStream(path)!!

fun makeGLFWImage(imagePath: String): GLFWImage {
	val image = ImageIO.read(getResourceStream(imagePath))
	val img = GLFWImage.create()
	img.width(image.width)
	img.height(image.height)
	img.pixels(image.toByteBuffer())
	return img
}

fun BufferedImage.toByteBuffer() : ByteBuffer {
	val pixels = getRGB(0, 0, width, height, null, 0, width)
	val buffer = BufferUtils.createByteBuffer(width * height * 4)
	for (pixel in pixels) {
		buffer.put((pixel shr 16 and 0xFF).toByte()) // Red
		buffer.put((pixel shr 8 and 0xFF).toByte()) // Green
		buffer.put((pixel and 0xFF).toByte()) // Blue
		buffer.put((pixel shr 24 and 0xFF).toByte()) // Alpha
	}
	buffer.flip()
	return buffer
}

fun measurePerformance(f : () -> Unit) : Long {
	f()
	return measurePerformanceOnce(f)
}

fun measurePerformanceSeconds(f : () -> Unit) = (measurePerformance(f).toDouble() / 1e9).toFloat()

fun measurePerformanceOnce(f : () -> Unit) : Long {
	val startTime = System.nanoTime()
	f()
	val endTime = System.nanoTime()
	return endTime - startTime
}

fun measurePerformanceOnceSeconds(f : () -> Unit) = (measurePerformanceOnce(f).toDouble() / 1e9).toFloat()

fun measurePerformanceFor(f : () -> Unit, ns : Long) : Long {
	f()
	var total = 0L
	var repetitions = 0
	do {
		total += measurePerformanceOnce(f)
		++repetitions
	} while (total < ns)
	println(repetitions - 1)
	return total / repetitions.toLong()
}

fun measurePerformanceFor(f : () -> Unit, s : Float) : Float = measurePerformanceFor(f, s.toLong() * 1e9.toLong()) / 1e9f

fun<T> measurePerformanceFor(ns : Long, g : (Long) -> T, f : (T) -> Unit) : Long {
	f(g(-1))
	var total = 0L
	var repetitions = 0L
	do {
		val input = g(repetitions)
		total += measurePerformanceOnce {f(input)}
		++repetitions
	} while (total < ns)
	println(repetitions)
	return total / repetitions
}

fun<T> measurePerformanceFor(s : Float, g : (Long) -> T, f : (T) -> Unit) : Float
	= measurePerformanceFor((s * 1e9).toLong(), g, f) / 1e9f

fun mino(a : Float?, b : Float?) : Float? {
	return min(a ?: return b, b ?: return a)
}

fun solveQuadratic(a : Float, b : Float, c : Float, callback : (Float?, Float?) -> Unit) {
	val inner = b * b - 4 * a * c
	if (inner < 0) {
		callback(null, null)
		return
	}
	callback((-b + sqrt(inner)) / (2 * a * c), (-b - sqrt(inner)) / (2 * a * c))
}

fun<T> solveCubic(a : Float, b : Float, c : Float, d : Float, callback : (Float, Float?, Float?) -> T) : T {
	/*val descriminant = 18 * a * b * c * d - 4 * b * b * b * d + b * b * c * c - 4 * a * c * c * c - 27 * a * a * d * d
	val gamma = (-1 + cbrt(-3.0)) / 2f
	val d0 = b * b - 3 * a * c
	val d1 = 2 * b * b * b - 9 * a * b * c + 27 * a * a * d
	val inner = d1 * d1 - 4 * d0 * d0 * d0*/
	return solveDepressedCubic((3 * a * c - b * b) / (3 * a * a), (2 * b * b * b - 9 * a * b * c + 27 * a * a * d) / (27 * a * a * a)) {ansa, ansb, ansc ->
		val offset = b / (3 * a)
		callback(ansa - offset, ansb?.let{it - offset}, ansc?.let{it - offset})
	}
}

//x^3 + px + q
fun<T> solveDepressedCubic(p : Float, q : Float, callback : (Float, Float?, Float?) -> T) : T {
	val tp = -p / 3.0
	val tq = q / 2.0
	val inner = tp * tp * tp - tq * tq
	return if (inner < 0) {//only 1 root
		val rt = sqrt(-inner)
		callback(-(cbrt(tq + rt) + cbrt(tq - rt)).toFloat(), null, null)
	} else {//3 roots
		val theta = atan(sqrt(inner) / tq) / 3f
		val len = -2 * sqrt(tp) * sign(tq)
		val ansy = sin(theta)
		val ansx = sqrt(1 - ansy * ansy)
		//val ansy = sin(theta)
		//val ansx = cosFromSin(ansy, theta)

		val rx = -0.5
		val ry = sqrt(3.0) / 2.0

		val ansa = len * (ansx * rx + ansy * ry)
		val ansb = len * (ansx * rx - ansy * ry)
		val ansc = len * ansx
		callback(ansa.toFloat(), ansb.toFloat(), ansc.toFloat())
	}
}

fun Float.lerp(other : Float, t : Float) = this + t * (other - this)
fun Double.lerp(other : Float, t : Float) = this + t * (other - this)

fun loadScene(path : String) : AIScene {

	val allBytes = getResourceStream(path).readAllBytes()
	val buffer = MemoryUtil.memCalloc(allBytes.size)
	buffer.put(allBytes)
	buffer.flip()

	val settings = aiCreatePropertyStore()!!
	aiSetImportPropertyInteger(
		settings, AI_CONFIG_PP_SBP_REMOVE,
		aiPrimitiveType_LINE or aiPrimitiveType_POINT
	)

	val scene = aiImportFileFromMemoryWithProperties(
		buffer,
		aiProcess_FindDegenerates or // removes "fake" triangle primitives
				aiProcess_SortByPType or// removes line and point primitives
				aiProcess_JoinIdenticalVertices or// trivial
				aiProcess_Triangulate or// trivial
				aiProcess_LimitBoneWeights or// selects only top 4 weights
				aiProcess_GenUVCoords or// generates tex coords if they are specified in a different format
				aiProcess_FindInvalidData or// removes some potential invalid data and fixes it if possible
				aiProcess_ImproveCacheLocality// improve cache hit rate
				,
		"",
		settings
	)!!
	MemoryUtil.memFree(buffer)
	return scene
}

fun loadBoneData(m : AIMesh) : Pair<IntArray, FloatArray> {
	val boneIndexBuffer = IntArray(m.mNumVertices() * 4) {-1}
	val boneWeightBuffer = FloatArray(m.mNumVertices() * 4) {0f}
	for (i in 0 until m.mNumBones()) {
		val b = AIBone.create(m.mBones()!![i])
		for (w in b.mWeights()) {
			for (j in 0 until 4) {
				if (boneIndexBuffer[w.mVertexId() * 4 + j] == -1) {
					boneIndexBuffer[w.mVertexId() * 4 + j] = i
					boneWeightBuffer[w.mVertexId() * 4 + j] = w.mWeight()
					break
				}
			}
		}
	}

	for (i in boneIndexBuffer.indices) if (boneIndexBuffer[i] == -1) boneIndexBuffer[i] = 0

	return Pair(boneIndexBuffer, boneWeightBuffer)
}