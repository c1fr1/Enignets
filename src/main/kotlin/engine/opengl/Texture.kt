package engine.opengl

import engine.getResourceStream
import engine.toByteBuffer
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.ArrayList

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class Texture : GLResource {

	@Suppress("JoinDeclarationAndAssignment")//idk why it gives this warning, it shouldn't be possible easily
	val width : Int
	val height : Int

	constructor(w : Int, h : Int, data : ByteBuffer?, type : Int = GL_RGBA) : super(glGenTextures()) {
		width = w
		height = h
		bind()
		glTexImage2D(GL_TEXTURE_2D, 0, type, width, height, 0, type, GL_UNSIGNED_BYTE, data)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
		unbind()
	}

	/**
	 * Creates a new texture object from an image
	 * @param path path to the texture
	 */
	constructor(path : String) : this(getResourceStream(path))

	/**
	 * Creates a new texture object from a resource stream
	 * @param stream input stream of a resource that has a file
	 */
	constructor(stream : InputStream) : this(ImageIO.read(stream))

	/**
	 * creates a new texture from a buffered image
	 * @param bi image
	 */
	constructor(bi : BufferedImage) : this(bi.width, bi.height, bi.toByteBuffer())

	/**
	 * creates a new empty texture given a width and height
	 * @param w width
	 * @param h height
	 */
	constructor(w: Int, h: Int) : this(w, h, null)

	/**
	 * binds the texture
	 */
	fun bind() = bindPosition(0)

	/**
	 * binds the texture to a specific position
	 * @param pos the position that is bound
	 */
	fun bindPosition(pos: Int) {
		glActiveTexture(GL_TEXTURE0 + pos)
		glBindTexture(GL_TEXTURE_2D, id)
	}

	/**
	 * deletes the texture
	 */
	override fun destroy() = glDeleteTextures(id)

	companion object {
		/**
		 * binds the default texture
		 */
		fun unbind() = unbindPosition(0)

		/**
		 * binds the default texture
		 */
		fun unbindPosition(pos: Int) {
			glActiveTexture(GL_TEXTURE0 + pos)
			glBindTexture(GL_TEXTURE_2D, 0)
		}
	}
}