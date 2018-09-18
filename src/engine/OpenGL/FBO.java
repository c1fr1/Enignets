package engine.OpenGL;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FBO {
	
	public static ArrayList<Integer> fboIDs = new ArrayList<>();
	public static ArrayList<Integer> renderBufferIDs = new ArrayList<>();
	
	private int id;
	private int depthRenderBufferID;
	private Texture boundTexture;
	
	/**
	 * creates a new fbo that is linked to the given texture
	 * @param tex texture for the fbo
	 */
	public FBO(Texture tex) {
		id = glGenFramebuffers();
		fboIDs.add(id);
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		
		bindTexture(tex);
	}
	
	/**
	 * binds the texture to the
	 * @param tex
	 */
	public void bindTexture(Texture tex) {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex.getId(), 0);
		boundTexture = tex;
		bindDepthRenderBuffer(tex.getWidth(), tex.getHeight());
	}
	
	/**
	 * binds a depth render buffer to the fbo
	 * @param w width of the render buffer
	 * @param h height of the render buffer
	 */
	private void bindDepthRenderBuffer(int w, int h) {
		depthRenderBufferID = glGenRenderbuffers();
		renderBufferIDs.add(depthRenderBufferID);
		glBindRenderbuffer(GL_RENDERBUFFER, depthRenderBufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, w, h);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderBufferID);
	}
	
	/**
	 * binds the render buffer
	 */
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, id);
	}
	
	/**
	 * prepares to render to the texture
	 */
	public void prepareForTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
		bind();
		glViewport(0, 0, boundTexture.getWidth(), boundTexture.getHeight());
		clearCurrentFrameBuffer();
	}
	
	/**
	 * binds the default framebuffer
	 */
	public static void bindDefault() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	/**
	 * prepares the default render
	 */
	public static void prepareDefaultRender() {
		bindDefault();
		EnigWindow.mainWindow.setViewport();
		clearCurrentFrameBuffer();
	}
	
	/**
	 * fills the current framebuffer
	 */
	public static void clearCurrentFrameBuffer() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * returns the current texture bound to the fbo
	 * @return current texture
	 */
	public Texture getBoundTexture() {
		return boundTexture;
	}
	
	/**
	 * gets the fbo handle
	 * @return fbo handle
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * gets the handle for the depth render buffer
	 * @return depth render buffer
	 */
	public int getDepthRenderBufferID() {
		return depthRenderBufferID;
	}
}
