package engine.opengl.shaders

import engine.Debug
import java.text.ParseException


@Suppress("unused")
enum class ShaderDataClass(val size : Int) {
	Vec1(1),
	DVec1(2),
	IVec1(1),
	UVec1(1),
	BVec1(1),

	Vec2(2),
	DVec2(4),
	IVec2(2),
	UVec2(2),
	BVec2(2),

	Vec3(3),
	DVec3(6),
	IVec3(3),
	UVec3(3),
	BVec3(3),

	Vec4(4),
	DVec4(8),
	IVec4(4),
	UVec4(4),
	BVec4(4),

	Mat2(4),
	DMat2(8),
	Mat3x2(6),
	DMat3x2(12),
	Mat4x2(8),
	DMat4x2(16),

	Mat2x3(6),
	DMat2x3(12),
	Mat3(9),
	DMat3(18),
	Mat4x3(12),
	DMat4x3(24),

	Mat2x4(8),
	DMat2x4(16),
	Mat3x4(12),
	DMat3x4(24),
	Mat4(16),
	DMat4(32),

	Sampler1D(1),
	ISampler1D(1),
	USampler1D(1),
	Sampler2D(1),
	ISampler2D(1),
	USampler2D(1),
	Sampler3D(1),
	ISampler3D(1),
	USampler3D(1),
	SamplerCube(1),
	ISamplerCube(1),
	USamplerCube(1),
	Sampler2DRect(1),
	ISampler2DRect(1),
	USampler2DRect(1),
	Sampler1DArray(1),
	ISampler1DArray(1),
	USampler1DArray(1),
	Sampler2DArray(1),
	ISampler2DArray(1),
	USampler2DArray(1),
	SamplerCubeArray(1),
	ISamplerCubeArray(1),
	USamplerCubeArray(1),
	SamplerBuffer(1),
	ISamplerBuffer(1),
	USamplerBuffer(1),
	Sampler2DMS(1),
	ISampler2DMS(1),
	USampler2DMS(1),
	Sampler2DMSArray(1),
	ISampler2DMSArray(1),
	USampler2DMSArray(1),

	Sampler1DShadow(1),
	Sampler2DShadow(1),
	SamplerCubeShadow(1),
	Sampler2DRectShadow(1),
	Sampler1DArrayShadow(1),
	Sampler2DArrayShadow(1),
	SamplerCubeArrayShadow(1),

	Image1D(1),
	IImage1D(1),
	UImage1D(1),
	Image2D(1),
	IImage2D(1),
	UImage2D(1),
	Image3D(1),
	IImage3D(1),
	UImage3D(1),
	ImageCube(1),
	IImageCube(1),
	UImageCube(1),
	Image2DRect(1),
	IImage2DRect(1),
	UImage2DRect(1),
	Image1DArray(1),
	IImage1DArray(1),
	UImage1DArray(1),
	Image2DArray(1),
	IImage2DArray(1),
	UImage2DArray(1),
	ImageCubeArray(1),
	IImageCubeArray(1),
	UImageCubeArray(1),
	ImageBuffer(1),
	IImageBuffer(1),
	UImageBuffer(1),
	Image2DMS(1),
	IImage2DMS(1),
	UImage2DMS(1),
	Image2DMSArray(1),
	IImage2DMSArray(1),
	UImage2DMSArray(1),

	AtomicUInt(1);

	fun simplify() : ShaderDataClass {
		return when (this) {
			DVec1 -> Vec1
			BVec1 -> IVec1
			DVec2 -> Vec2
			BVec2 -> IVec2
			DVec3 -> Vec3
			BVec3 -> IVec3
			DVec4 -> Vec4
			BVec4 -> IVec4
			DMat2 -> Mat2
			DMat3x2 -> Mat3x2
			DMat4x2 -> Mat4x2
			DMat2x3 -> Mat2x3
			DMat3 -> Mat3
			DMat4x3 -> Mat4x3
			DMat2x4 -> Mat2x4
			DMat3x4 -> Mat3x4
			DMat4 -> Mat4
			Sampler1D -> IVec1
			ISampler1D -> IVec1
			USampler1D -> IVec1
			Sampler2D -> IVec1
			ISampler2D -> IVec1
			USampler2D -> IVec1
			Sampler3D -> IVec1
			ISampler3D -> IVec1
			USampler3D -> IVec1
			SamplerCube -> IVec1
			ISamplerCube -> IVec1
			USamplerCube -> IVec1
			Sampler2DRect -> IVec1
			ISampler2DRect -> IVec1
			USampler2DRect -> IVec1
			Sampler1DArray -> IVec1
			ISampler1DArray -> IVec1
			USampler1DArray -> IVec1
			Sampler2DArray -> IVec1
			ISampler2DArray -> IVec1
			USampler2DArray -> IVec1
			SamplerCubeArray -> IVec1
			ISamplerCubeArray -> IVec1
			USamplerCubeArray -> IVec1
			SamplerBuffer -> IVec1
			ISamplerBuffer -> IVec1
			USamplerBuffer -> IVec1
			Sampler2DMS -> IVec1
			ISampler2DMS -> IVec1
			USampler2DMS -> IVec1
			Sampler2DMSArray -> IVec1
			ISampler2DMSArray -> IVec1
			USampler2DMSArray -> IVec1
			Sampler1DShadow -> IVec1
			Sampler2DShadow -> IVec1
			SamplerCubeShadow -> IVec1
			Sampler2DRectShadow -> IVec1
			Sampler1DArrayShadow -> IVec1
			Sampler2DArrayShadow -> IVec1
			SamplerCubeArrayShadow -> IVec1
			Image1D -> IVec1
			IImage1D -> IVec1
			UImage1D -> IVec1
			Image2D -> IVec1
			IImage2D -> IVec1
			UImage2D -> IVec1
			Image3D -> IVec1
			IImage3D -> IVec1
			UImage3D -> IVec1
			ImageCube -> IVec1
			IImageCube -> IVec1
			UImageCube -> IVec1
			Image2DRect -> IVec1
			IImage2DRect -> IVec1
			UImage2DRect -> IVec1
			Image1DArray -> IVec1
			IImage1DArray -> IVec1
			UImage1DArray -> IVec1
			Image2DArray -> IVec1
			IImage2DArray -> IVec1
			UImage2DArray -> IVec1
			ImageCubeArray -> IVec1
			IImageCubeArray -> IVec1
			UImageCubeArray -> IVec1
			ImageBuffer -> IVec1
			IImageBuffer -> IVec1
			UImageBuffer -> IVec1
			Image2DMS -> IVec1
			IImage2DMS -> IVec1
			UImage2DMS -> IVec1
			Image2DMSArray -> IVec1
			IImage2DMSArray -> IVec1
			UImage2DMSArray -> IVec1
			AtomicUInt -> IVec1
			else -> this
		}
	}

	companion object {
		fun parse(representation : String) : ShaderDataClass {
			return when (representation.trim().substringBefore('[')) {
				"float" -> Vec1
				"double" -> DVec1
				"int" -> IVec1
				"uint" -> UVec1
				"bool" -> BVec1

				"vec2" -> Vec2
				"dvec2" -> DVec2
				"ivec2" -> IVec2
				"uvec2" -> UVec2
				"bvec2" -> BVec2

				"vec3" -> Vec3
				"dvec3" -> DVec3
				"ivec3" -> IVec3
				"uvec3" -> UVec3
				"bvec3" -> BVec3

				"vec4" -> Vec4
				"dvec4" -> DVec4
				"ivec4" -> IVec4
				"uvec4" -> UVec4
				"bvec4" -> BVec4

				"mat2" -> Mat2
				"dmat2" -> DMat2
				"mat3x2" -> Mat3x2
				"dmat3x2" -> DMat3x2
				"mat4x2" -> Mat4x2
				"dmat4x2" -> DMat4x2

				"mat2x3" -> Mat2x3
				"dmat2x3" -> DMat2x3
				"mat3" -> Mat3
				"dmat3" -> DMat3
				"mat4x3" -> Mat4x3
				"dmat4x3" -> DMat4x3

				"mat2x4" -> Mat2x4
				"dmat2x4" -> DMat2x4
				"mat3x4" -> Mat3x4
				"dmat3x4" -> DMat3x4
				"mat4" -> Mat4
				"dmat4" -> DMat4

				"sampler1D" -> Sampler1D
				"isampler1D" -> ISampler1D
				"usampler1D" -> USampler1D
				"sampler2D" -> Sampler2D
				"isampler2D" -> ISampler2D
				"usampler2D" -> USampler2D
				"sampler3D" -> Sampler3D
				"isampler3D" -> ISampler3D
				"usampler3D" -> USampler3D
				"samplerCube" -> SamplerCube
				"isamplerCube" -> ISamplerCube
				"usamplerCube" -> USamplerCube
				"sampler2DRect" -> Sampler2DRect
				"isampler2DRect" -> ISampler2DRect
				"usampler2DRect" -> USampler2DRect
				"sampler1DArray" -> Sampler1DArray
				"isampler1DArray" -> ISampler1DArray
				"usampler1DArray" -> USampler1DArray
				"sampler2DArray" -> Sampler2DArray
				"isampler2DArray" -> ISampler2DArray
				"usampler2DArray" -> USampler2DArray
				"samplerCubeArray" -> SamplerCubeArray
				"isamplerCubeArray" -> ISamplerCubeArray
				"usamplerCubeArray" -> USamplerCubeArray
				"samplerBuffer" -> SamplerBuffer
				"isamplerBuffer" -> ISamplerBuffer
				"usamplerBuffer" -> USamplerBuffer
				"sampler2DMS" -> Sampler2DMS
				"isampler2DMS" -> ISampler2DMS
				"usampler2DMS" -> USampler2DMS
				"sampler2DMSArray" -> Sampler2DMSArray
				"isampler2DMSArray" -> ISampler2DMSArray
				"usampler2DMSArray" -> USampler2DMSArray

				"sampler1DShadow" -> Sampler1DShadow
				"sampler2DShadow" -> Sampler2DShadow
				"samplerCubeShadow" -> SamplerCubeShadow
				"sampler2DRectShadow" -> Sampler2DRectShadow
				"sampler1DArrayShadow" -> Sampler1DArrayShadow
				"sampler2DArrayShadow" -> Sampler2DArrayShadow
				"samplerCubeArrayShadow" -> SamplerCubeArrayShadow

				"image1D" -> Image1D
				"iimage1D" -> IImage1D
				"uimage1D" -> UImage1D
				"image2D" -> Image2D
				"iimage2D" -> IImage2D
				"uimage2D" -> UImage2D
				"image3D" -> Image3D
				"iimage3D" -> IImage3D
				"uimage3D" -> UImage3D
				"imageCube" -> ImageCube
				"iimageCube" -> IImageCube
				"uimageCube" -> UImageCube
				"image2DRect" -> Image2DRect
				"iimage2DRect" -> IImage2DRect
				"uimage2DRect" -> UImage2DRect
				"image1DArray" -> Image1DArray
				"iimage1DArray" -> IImage1DArray
				"uimage1DArray" -> UImage1DArray
				"image2DArray" -> Image2DArray
				"iimage2DArray" -> IImage2DArray
				"uimage2DArray" -> UImage2DArray
				"imageCubeArray" -> ImageCubeArray
				"iimageCubeArray" -> IImageCubeArray
				"uimageCubeArray" -> UImageCubeArray
				"imageBuffer" -> ImageBuffer
				"iimageBuffer" -> IImageBuffer
				"uimageBuffer" -> UImageBuffer
				"image2DMS" -> Image2DMS
				"iimage2DMS" -> IImage2DMS
				"uimage2DMS" -> UImage2DMS
				"image2DMSArray" -> Image2DMSArray
				"iimage2DMSArray" -> IImage2DMSArray
				"uimage2DMSArray" -> UImage2DMSArray

				"atomic_uint" -> AtomicUInt
				else -> throw ParseException("shader had unknown data type \"$representation\"", 0)
			}
		}
	}

	override fun toString() : String {
		return when (this) {
			Vec1 -> "vec1"
			DVec1 -> "dvec1"
			IVec1 -> "ivec1"
			UVec1 -> "uvec1"
			BVec1 -> "bvec1"
			Vec2 -> "vec2"
			DVec2 -> "dvec2"
			IVec2 -> "ivec2"
			UVec2 -> "uvec2"
			BVec2 -> "bvec2"
			Vec3 -> "vec3"
			DVec3 -> "dvec3"
			IVec3 -> "ivec3"
			UVec3 -> "uvec3"
			BVec3 -> "bvec3"
			Vec4 -> "vec4"
			DVec4 -> "dvec4"
			IVec4 -> "ivec4"
			UVec4 -> "uvec4"
			BVec4 -> "bvec4"
			Mat2 -> "mat2"
			DMat2 -> "dmat2"
			Mat3x2 -> "mat3x2"
			DMat3x2 -> "dmat3x2"
			Mat4x2 -> "mat4x2"
			DMat4x2 -> "dmat4x2"
			Mat2x3 -> "mat2x3"
			DMat2x3 -> "dmat2x3"
			Mat3 -> "mat3"
			DMat3 -> "dmat3"
			Mat4x3 -> "mat4x3"
			DMat4x3 -> "dmat4x3"
			Mat2x4 -> "mat2x4"
			DMat2x4 -> "dmat2x4"
			Mat3x4 -> "mat3x4"
			DMat3x4 -> "dmat3x4"
			Mat4 -> "mat4"
			DMat4 -> "dmat4"
			Sampler1D -> "sampler1D"
			ISampler1D -> "isampler1D"
			USampler1D -> "usampler1D"
			Sampler2D -> "sampler2D"
			ISampler2D -> "isampler2D"
			USampler2D -> "usampler2D"
			Sampler3D -> "sampler3D"
			ISampler3D -> "isampler3D"
			USampler3D -> "usampler3D"
			SamplerCube -> "samplerCube"
			ISamplerCube -> "isamplerCube"
			USamplerCube -> "usamplerCube"
			Sampler2DRect -> "sampler2DRect"
			ISampler2DRect -> "isampler2DRect"
			USampler2DRect -> "usampler2DRect"
			Sampler1DArray -> "sampler1DArray"
			ISampler1DArray -> "isampler1DArray"
			USampler1DArray -> "usampler1DArray"
			Sampler2DArray -> "sampler2DArray"
			ISampler2DArray -> "isampler2DArray"
			USampler2DArray -> "usampler2DArray"
			SamplerCubeArray -> "samplerCubeArray"
			ISamplerCubeArray -> "isamplerCubeArray"
			USamplerCubeArray -> "usamplerCubeArray"
			SamplerBuffer -> "samplerBuffer"
			ISamplerBuffer -> "isamplerBuffer"
			USamplerBuffer -> "usamplerBuffer"
			Sampler2DMS -> "sampler2DMS"
			ISampler2DMS -> "isampler2DMS"
			USampler2DMS -> "usampler2DMS"
			Sampler2DMSArray -> "Sampler2DMSArray"
			ISampler2DMSArray -> "isampler2DMSArray"
			USampler2DMSArray -> "usampler2DMSArray"
			Sampler1DShadow -> "sampler1DShadow"
			Sampler2DShadow -> "sampler2DShadow"
			SamplerCubeShadow -> "samplerCubeShadow"
			Sampler2DRectShadow -> "sampler2DRectShadow"
			Sampler1DArrayShadow -> "sampler1DArrayShadow"
			Sampler2DArrayShadow -> "sampler2DArrayShadow"
			SamplerCubeArrayShadow -> "samplerCubeArrayShadow"
			Image1D -> "image1D"
			IImage1D -> "iimage1D"
			UImage1D -> "uimage1D"
			Image2D -> "image2D"
			IImage2D -> "iimage2D"
			UImage2D -> "uimage2D"
			Image3D -> "image3D"
			IImage3D -> "iimage3D"
			UImage3D -> "uimage3D"
			ImageCube -> "imageCube"
			IImageCube -> "iimageCube"
			UImageCube -> "uimageCube"
			Image2DRect -> "image2DRect"
			IImage2DRect -> "iimage2DRect"
			UImage2DRect -> "uimage2DRect"
			Image1DArray -> "image1DArray"
			IImage1DArray -> "iimage1DArray"
			UImage1DArray -> "uimage1DArray"
			Image2DArray -> "image2DArray"
			IImage2DArray -> "iimage2DArray"
			UImage2DArray -> "uimage2DArray"
			ImageCubeArray -> "imageCubeArray"
			IImageCubeArray -> "iimageCubeArray"
			UImageCubeArray -> "uimageCubeArray"
			ImageBuffer -> "imageBuffer"
			IImageBuffer -> "iimageBuffer"
			UImageBuffer -> "uimageBuffer"
			Image2DMS -> "image2DMS"
			IImage2DMS -> "iimage2DMS"
			UImage2DMS -> "uimage2DMS"
			Image2DMSArray -> "image2DMSArray"
			IImage2DMSArray -> "iimage2DMSArray"
			UImage2DMSArray -> "uimage2DMSArray"
			AtomicUInt -> "atomic_uint"
		}
	}
}

data class ShaderDataType(val dataClass : ShaderDataClass, val arraySize : Int) {
	constructor(representation : String) : this(
		ShaderDataClass.parse(representation),
		if (representation.contains('[')) {
			representation.substringAfter('[').substringBefore(']').toInt()
		} else {-1})

	constructor(dataClass: ShaderDataClass) : this(dataClass, -1)

	@Debug
	fun checkType(actual : ShaderDataClass, actualArraySize: Int) {
		if (actual.simplify() != dataClass.simplify() || actualArraySize > arraySize) {
			throw InvalidGLTypeException(this, ShaderDataType(actual, actualArraySize))
		}
	}

	@Debug
	fun checkType(expected : ShaderDataClass) {
		if (expected.simplify() != dataClass.simplify()) {
			throw InvalidGLTypeException(this, ShaderDataType(expected))
		}
	}

	override fun toString() : String {
		return if (arraySize >= 0) {"$dataClass[$arraySize]"} else {"$dataClass"}
	}
}

open class InvalidGLTypeException(expected : ShaderDataType, actual : ShaderDataType)
	: Exception("invalid GL type found, expected $expected, found $actual")