package engine.OpenGL;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	
	public static ArrayList<Integer> textureIDs = new ArrayList<>();
	
	private int id, width, height;
	
	/**
	 * Creates a new texture object from an image
	 * @param path path to the texture
	 */
	public Texture(String path) {
		try {
			BufferedImage bi = ImageIO.read(new File(path));
			width = bi.getWidth();
			height = bi.getHeight();
			int[] pixels = bi.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int pixel = pixels[i * width + j];
					buffer.put((byte)((pixel >> 16) & 0xFF)); // Red
					buffer.put((byte)((pixel >>  8) & 0xFF)); // Green
					buffer.put((byte)((pixel	  ) & 0xFF)); // Blue
					buffer.put((byte)((pixel >> 24) & 0xFF)); // Alpha
				}
			}
			buffer.flip();
			
			id = glGenTextures();
			textureIDs.add(id);
			bind();
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			unbind();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * creates a new texture from a buffered image
	 * @param bi image
	 */
	public Texture(BufferedImage bi) {
		width = bi.getWidth();
		height = bi.getHeight();
		int[] pixels = bi.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = pixels[i * width + j];
				buffer.put((byte)((pixel >> 16) & 0xFF)); // Red
				buffer.put((byte)((pixel >>  8) & 0xFF)); // Green
				buffer.put((byte)((pixel	  ) & 0xFF)); // Blue
				buffer.put((byte)((pixel >> 24) & 0xFF)); // Alpha
			}
		}
		buffer.flip();
		
		id = glGenTextures();
		textureIDs.add(id);
		bind();
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		unbind();
	}
	
	/**
	 * creates a new empty texture given a width and height
	 * @param w width
	 * @param h height
	 */
	public Texture(int w, int h) {
		width = w;
		height = h;
		id = glGenTextures();
		textureIDs.add(id);
		bind();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		unbind();
	}
	
	/**
	 * binds the texture
	 */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * binds the default texture
	 */
	public static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * deletes the texture
	 */
	public void destroy() {
		glDeleteTextures(id);
	}
	
	/**
	 * get the texture handle
	 * @return texture handle
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * get the width
	 * @return width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * get the height
	 * @return height
	 */
	public int getHeight() {
		return height;
	}
}
