package engine;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;

public abstract class EnigView {
	public EnigWindow window;

	public long loopStartTime;

	public float deltaTime;

	public long frameStartTime;

	/**
	 * creates a new main view
	 */
	public EnigView(EnigWindow swindow) {
		window = swindow;
	}

	public void runLoop() {
		loopStartTime = System.nanoTime();
		frameStartTime = loopStartTime;
		while (!glfwWindowShouldClose(window.id)) {
			long newTime = System.nanoTime();
			deltaTime = (float) (newTime - frameStartTime) / 1e9f;
			frameStartTime = newTime;
			if (loop()) {
				cleanUp();
				break;
			}
			window.update();
			cleanUp();
		}
		setDown();
	}

	/**
	 * cleans up at the end of a frame
	 */
	public void cleanUp() {
		ShaderProgram.disable();
		window.resetOffsets();
		glfwSwapBuffers(window.id);
		glfwPollEvents();
		EnigWindow.checkGLError();
		//window.update();
	}

	/**
	 * loop that gets called every frame
	 * @return if the view should close after this frame ends
	 */
	public abstract boolean loop();

	/**
	 * does any neccisary cleanup
	 */
	public void setDown() {};
}
