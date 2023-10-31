package engine.opengl

import engine.getResourceStream
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils.createByteBuffer
import org.lwjgl.opengl.GL11.GL_RED
import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTruetype
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.nio.ByteBuffer


open class Font : Texture {

	val charData : STBTTBakedChar.Buffer
	val fontSize : Float

	constructor(width : Int, height : Int, image : ByteBuffer, charData : STBTTBakedChar.Buffer, fontSize: Float) : super(width, height, image, GL_RED) {
		this.charData = charData
		this.fontSize = fontSize
	}

	companion object {

		operator fun invoke(path : String, fontSize : Float, width : Int, height : Int) =
			invoke(getResourceStream(path), fontSize, width, height)

		operator fun invoke(stream : InputStream, fontSize : Float, width : Int, height : Int) : Font {
			val allBytes = stream.readBytes()
			val fontData = MemoryUtil.memCalloc(allBytes.size)
			fontData.put(allBytes)
			fontData.flip()
			val imageBuffer = createByteBuffer(width * height)
			val charData = STBTTBakedChar.malloc(96)
			STBTruetype.stbtt_BakeFontBitmap(fontData, fontSize, imageBuffer, width, height, 32, charData)
			MemoryUtil.memFree(fontData)
			return Font(width, height, imageBuffer, charData, fontSize)
		}
	}

	fun getTransforms(text : String,
	                  r : (
		                  charTranslation : Vector3f,
		                  charScale : Vector3f,
		                  tcTranslation : Vector3f,
		                  tcScale : Vector3f
	                  ) -> Unit) {
		var xAdv = 0f
		for (i in text.indices) {
			val co = charData[text[i].code - 32]
			val cwidth = co.x1() - co.x0()
			val cheight = co.y1() - co.y0()
			val charTranslation = Vector3f(co.xoff() / fontSize + xAdv, -(co.yoff() + cheight) / fontSize, 0f)
			val charScale = Vector3f(cwidth / fontSize, cheight / fontSize, 1f)
			val tcTranslation = Vector3f(co.x0() / width.toFloat(), co.y0() / height.toFloat(), 0f)
			val tcScale = Vector3f(cwidth / width.toFloat(), cheight / height.toFloat(), 1f)

			r(charTranslation, charScale, tcTranslation, tcScale)
			xAdv += co.xadvance() / fontSize
		}
	}

	fun getMats(text : String, base : Matrix4f, r : (charMat : Matrix4f, tcMat : Matrix4f) -> Unit) {
		getTransforms(text) {charTranslation, charScale, tcTranslation, tcScale ->
			r(Matrix4f(base).translate(charTranslation).scale(charScale),
				Matrix4f().translate(charTranslation).scale(tcScale))
		}
	}

	fun getMatsArray(text : String, base : Matrix4f, ret : (worldMats : Array<Matrix4f>, texCoordMats: Array<Matrix4f>) -> Unit) {

		val worldMats = Array(text.length) {Matrix4f()}
		val tcMats = Array(text.length) {Matrix4f()}
		var i = 0

		getMats(text, base) {charMat, tcMat ->
			worldMats[i] = charMat
			tcMats[i++] = tcMat
		}

		ret(worldMats, tcMats)
	}
}