package engine;

import engine.OpenGL.EnigWindow;
import engine.OpenGL.ShaderProgram;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public abstract class EnigView {
	public EnigWindow window;
	
	public long framesSinceStart = 0;
	
	/**
	 * creates a new main view
	 */
	public EnigView() {
		window = new EnigWindow(getName());
		setup();
		while ( !glfwWindowShouldClose(EnigWindow.mainWindow.id) ) {
			window.update();
			++framesSinceStart;
			if (loop()) {
				cleanUp();
				break;
			}
			cleanUp();
		}
		window.terminate();
	}
	
	/**
	 * creates a new main view
	 * @param width width of the new window
	 * @param height height of the new window
	 */
	public EnigView(int width, int height) {
		window = new EnigWindow(width, height, getName());
		setup();
		while ( !glfwWindowShouldClose(EnigWindow.mainWindow.id) ) {
			window.update();
			++framesSinceStart;
			if (loop()) {
				cleanUp();
				break;
			}
			cleanUp();
		}
		window.terminate();
	}
	
	/**
	 * creates a new view based on an existing window that can be not the main view
	 * @param swindow window for the view to take place in
	 * @param isMain wether or not the window should be terminated once the task ends
	 */
	public EnigView(EnigWindow swindow, boolean isMain) {
		window = swindow;
		setup();
		while ( !glfwWindowShouldClose(EnigWindow.mainWindow.id) ) {
			window.update();
			++framesSinceStart;
			if (loop()) {
				cleanUp();
				break;
			};
			cleanUp();
		}
		if (isMain) {
			window.terminate();
		}
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
	 * sets up the view
	 */
	public abstract void setup();
	
	/**
	 * gets the name of the window
	 * @return name of the window
	 */
	public abstract String getName();
}
