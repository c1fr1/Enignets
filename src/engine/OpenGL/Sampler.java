package engine.OpenGL;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class Sampler {
	public String name;
	public int location;
	public Sampler(String name) {
		this.name = name;
	}
	public void setLocation(int shaderID) {
		location = glGetUniformLocation(shaderID, name);
	}
}
