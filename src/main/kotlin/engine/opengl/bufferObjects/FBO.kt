package engine.opengl.bufferObjects

import engine.opengl.GLResource
import engine.opengl.Texture
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.glFramebufferTexture
import java.util.ArrayList

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class FBO : GLResource {

	open var depthRenderBufferID = 0
	set(id) {
		bind()
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBufferID)
		field = id
	}

	/**
	 * returns the current texture bound to the fbo
	 * @return current texture
	 */
	open var boundTexture: Texture
	set(tex) {
		glBindFramebuffer(GL_FRAMEBUFFER, id)
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex.id, 0)
		bindNewDepthRenderBuffer(tex.width, tex.height)
		field = tex
	}

	/**
	 * creates a new fbo that is linked to the given texture
	 * @param tex texture for the fbo
	 */
	constructor(tex: Texture) : super(glGenFramebuffers()) {
		glBindFramebuffer(GL_FRAMEBUFFER, id)
		glDrawBuffer(GL_COLOR_ATTACHMENT0)
		boundTexture = tex
	}

	constructor(width: Int, height: Int) : this(Texture(width, height))

	/**
	 * binds a depth render buffer to the fbo
	 * @param w width of the render buffer
	 * @param h height of the render buffer
	 */
	open fun bindNewDepthRenderBuffer(w: Int, h: Int) {
		val newID = glGenRenderbuffers()
		renderBufferIDs.add(newID)
		glBindRenderbuffer(GL_RENDERBUFFER, newID)
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, w, h)
		depthRenderBufferID = newID
	}

	/**
	 * binds the render buffer
	 */
	open fun bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id)
	}

	/**
	 * prepares to render to the texture
	 */
	open fun prepareForTexture() {
		glBindTexture(GL_TEXTURE_2D, 0)
		bind()
		glViewport(0, 0, boundTexture.width, boundTexture.height)
		clearCurrentFrameBuffer()
	}

	override fun destroy() {
		glDeleteFramebuffers(id)
	}

	companion object {
		var renderBufferIDs = ArrayList<Int>()

		/**
		 * binds the default framebuffer
		 */
		fun bindDefault() {
			glBindFramebuffer(GL_FRAMEBUFFER, 0)
		}

		/**
		 * prepares the default render
		 */
		fun prepareDefaultRender() {
			bindDefault()
			clearCurrentFrameBuffer()
		}

		/**
		 * fills the current framebuffer
		 */
		fun clearCurrentFrameBuffer() {
			glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
		}
	}
}