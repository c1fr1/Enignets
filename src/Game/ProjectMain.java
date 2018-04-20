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
	
	private EnigFunction func;
	private PlatformSegment platform;
	private VAO platformModel;
	private GameObject platObj;
	private Program plattyproggy;
	private Player player;
	
	public void setup() {
		//set variables here
		func = new MountainFunction(0, 0, 5);
		platform = new FunctionPlatform(func);
		platformModel = platform.getSurfaceModel(-10, 10, -10, 10, 50, 50);
		platObj = new GameObject(platformModel);
		plattyproggy = new Program("res/shaders/platformShaders/vert.gls", "res/shaders/platformShaders/frag.gls");
		EnigWindow.mainWindow.toggleCursorInput();
		player = new Player(100, 100, 0.1f);
	}
	
	public void gameLoop() {
		//game here
		player.updateRotations();
		player.updatePosition(platform);
		plattyproggy.enable();
		platObj.render(player.cam);
		
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