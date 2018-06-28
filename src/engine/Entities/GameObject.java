package engine.Entities;

import engine.OpenGL.ShaderProgram;
import engine.OpenGL.Uniform;
import engine.OpenGL.VAO;

public class GameObject extends PositionInfo {
	public VAO model;
	
	/**
	 * creates a game object from a vao
	 * @param vao model
	 */
	public GameObject(VAO vao) {
		model = vao;
	}
	
	/**
	 * creates a game object from an obj file
	 * @param path obj file path
	 */
	public GameObject(String path) {
		model = new VAO(path);
	}
	
	/**
	 * fullRender the model
	 * @param cam player matrix
	 */
	public void render(Camera cam) {
		for (Uniform uni: ShaderProgram.currentShaderProgram.shaders[0].uniforms) {
			if (uni.getName().equals("matrix")) {
				uni.set(translateMatrix(cam.getCameraMatrix()));
			}
		}
		model.fullRender();
	}
}
