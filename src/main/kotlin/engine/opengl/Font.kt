package engine.opengl

import engine.getResourceStream
import org.joml.Matrix4f
import org.lwjgl.BufferUtils.createByteBuffer
import org.lwjgl.opengl.GL11.GL_RED
import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTruetype
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.memSlice
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


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

	fun getMats(text : String, base : Matrix4f, ret : (worldMats : Array<Matrix4f>, texCoordMats: Array<Matrix4f>) -> Unit) {

		val worldMats = Array(text.length) {Matrix4f()}
		val tcMats = Array(text.length) {Matrix4f()}

		val b = Matrix4f(base)
		for (i in text.indices) {
			val co = charData[text[i].code - 32]
			val cwidth = co.x1() - co.x0()
			val cheight = co.y1() - co.y0()
			worldMats[i] = Matrix4f(b)
				.translate(co.xoff() / fontSize, -(co.yoff() + cheight) / fontSize, 0f)
				.scale(cwidth / fontSize, cheight / fontSize, 1f)
			tcMats[i] = Matrix4f().translate(co.x0() / width.toFloat(), co.y0() / height.toFloat(), 0f)
					.scale(cwidth / width.toFloat(), cheight / height.toFloat(), 1f)
			b.translate(co.xadvance() / fontSize, 0f, 0f)
		}
		ret(worldMats, tcMats)
	}
}