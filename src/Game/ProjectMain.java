package Game;

import engine.*;
import engine.Platform.*;
import engine.Functions.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class ProjectMain {

	private int frame = 0;
	
	public static ProjectMain Main;
	
	//project variables
	
	EnigFunction func;
	PlatformSegment platform;
	VAO platformModel;
	GameObject platObj;
	Camera mainCamera;
	Program plattyproggy;
	
	public void setup() {
		//set variables here
		func = new MountainFunction(0, 0, 5);
		platform = new FunctionPlatform(func);
		platformModel = platform.getSurfaceModel(-10, 10, -10, 10, 1000, 1000);
		platObj = new GameObject(platformModel);
		mainCamera = new Camera(70f, 0.1f, 1000f);
		plattyproggy = new Program("res/shaders/platformShaders/vert.gls", "res/shaders/platformShaders/frag.gls");
		EnigWindow.mainWindow.toggleCursorInput();
	}
	
	public void gameLoop() {
		//game here
		
		mainCamera.yaw(-(float) EnigWindow.mainWindow.cursorXOffset * UserControls.sensitivity);
		mainCamera.pitch(-(float) EnigWindow.mainWindow.cursorYOffset * UserControls.sensitivity);
		if (mainCamera.getPitch() > Math.PI/2) {
			mainCamera.setPitch((float) Math.PI/2);
		}else if (mainCamera.getPitch() < -Math.PI/2) {
			mainCamera.setPitch((float) -Math.PI/2);
		}
		if (EnigWindow.mainWindow.keys[UserControls.up] > 0) {
			mainCamera.translate(mainCamera.getRotatedVector(0f, -1f, 0f, 0.1f));
		}else if (EnigWindow.mainWindow.keys[UserControls.down] > 0) {
			mainCamera.translate(mainCamera.getRotatedVector(0f, 1f, 0f, 0.1f));
		}
		if (EnigWindow.mainWindow.keys[UserControls.forward] > 0) {
			mainCamera.translate(mainCamera.getRotatedVector(0f, 0f, 1f, 0.1f));
		}else if (EnigWindow.mainWindow.keys[UserControls.backward] > 0) {
			mainCamera.translate(mainCamera.getRotatedVector(0f, 0f, -1f, 0.1f));
		}
		
		if (EnigWindow.mainWindow.keys[UserControls.left] > 0) {
			mainCamera.translate(mainCamera.getRotatedVector(1f, 0f, 0f, 0.1f));
		}else if (EnigWindow.mainWindow.keys[UserControls.right] > 0) {
			mainCamera.translate(mainCamera.getRotatedVector(-1f, 0f, 0f, 0.1f));
		}
		plattyproggy.enable();
		platObj.render(mainCamera);
		
	}
	
	public static void main(String[] args) {
		Main = new ProjectMain();
	}
/*															end															*/
	public ProjectMain() {
		new EnigWindow();
		frame = 0;
		setup();
		while ( !glfwWindowShouldClose(EnigWindow.mainWindow.window) ) {
			EnigWindow.mainWindow.update();
			++frame;
			gameLoop();
			cleanup();
		}
		EnigWindow.mainWindow.terminate();
	}
	
	public void cleanup() {
		Program.disable();
		EnigWindow.mainWindow.resetOffsets();
		glfwSwapBuffers(EnigWindow.mainWindow.window);
		glfwPollEvents();
		EnigWindow.checkGLError();
	}
}