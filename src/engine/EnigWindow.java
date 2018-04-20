package engine;

import Game.UserControls;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EnigWindow {
	// The window handle
	public long window;

	public static int width = 1800/1;
	public static int height = (int)(1100/1);

	public float cursorXFloat;
	public float cursorYFloat;

	public double cursorXDouble;
	public double cursorYDouble;

	public double cursorXOffset;
	public double cursorYOffset;

	public boolean inputEnabled = false;
	public boolean printFrames = false;

	public int[] keys = new int[GLFW_KEY_LAST + 1];
	
	public int framesSinceLastSecond = 0;
	public int lastFrameCount = 0;
	public long lastSecond;
	//public long lastFrame = System.nanoTime();
	
	public static EnigWindow mainWindow;

	public void terminate() {
		//System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		//init();
		//loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public EnigWindow() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() ) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_SAMPLES, 8);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = vidMode.width();
		height = vidMode.height();
		window = glfwCreateWindow(width, height, "Enignets game", NULL/*glfwGetPrimaryMonitor()*/, NULL);
		if ( window == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			}else if (key == UserControls.pause && action == GLFW_RELEASE) {
				toggleCursorInput();
			}else {
				keys[key] = action;
			}
		});

		glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				if (inputEnabled) {
					cursorXOffset += cursorXDouble - xpos;
					cursorYOffset += cursorYDouble - ypos;
					cursorXDouble = xpos;
					cursorYDouble = ypos;
					cursorXFloat = (float) (xpos - width / 2) / (float) width;
					cursorYFloat = (float) (ypos - height / 2) / (float) height;
				}
			}
		});


		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

		// Make the window visible
		glfwShowWindow(window);

		glfwSetWindowSizeCallback(window, (long window, int w, int h) -> {
			width = w;
			height = h;
			glViewport(0, 0, width, height);
		});

		GL.createCapabilities();
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_CULL_FACE);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		mainWindow = this;
		//Opening Sequence
		//*
		int frame = 0;
		Program openingShader = new Program("res/shaders/openingShaders/vert.gls", "res/shaders/openingShaders/frag.gls");
		GameObject openingObject = new GameObject("res/enignets.obj");
		Camera openingCamera = new Camera(70, 0.01f, 1000f);
		openingCamera.orderOfTransformations = new int[] {0, 2, 1};
		openingCamera.translate(0f, 0f, -5f);
		openingCamera.pitch((float)  Math.PI/2);

		while (openingCamera.getPitch() < (float) (3*Math.PI/2) && keys[GLFW_KEY_SPACE] != 1) {
			openingCamera.setPos(0f, 0f, (float) -(13-10f*Math.pow(150f-(float) frame, 6f)/1.1390625e+13));
			openingCamera.pitch(0.011f);
			update();
			frame += 1;
			openingShader.enable();
			openingShader.shaders[2].uniforms[0].set(new float[] {(float) frame});
			openingShader.shaders[2].uniforms[1].set(new float[] {(float) EnigWindow.width, (float) EnigWindow.height});
			openingObject.render(openingCamera);
			Program.disable();
			glfwSwapBuffers(window);
			glfwPollEvents();
			checkGLError();
		}
		frame = 286;
		openingCamera.setPos(0f, 0f, (float) -(13-10f*Math.pow(150f-(float) frame, 6f)/1.1390625e+13));
		openingCamera.setPitch((float) (3*Math.PI/2));
		update();
		openingShader.enable();
		openingShader.shaders[2].uniforms[0].set(new float[] {(float) frame});
		openingShader.shaders[2].uniforms[1].set(new float[] {(float) EnigWindow.width, (float) EnigWindow.height});
		openingObject.render(openingCamera);
		Program.disable();
		glfwSwapBuffers(window);
		glfwPollEvents();
		checkGLError();
		while (frame < 400 && keys[GLFW_KEY_SPACE] != 1) {
			++frame;
			update();
			openingShader.enable();
			openingShader.shaders[2].uniforms[0].set(new float[]{(float) frame});
			openingShader.shaders[2].uniforms[1].set(new float[]{(float) EnigWindow.width, (float) EnigWindow.height});
			openingObject.render(openingCamera);
			Program.disable();
			EnigWindow.mainWindow.resetOffsets();
			glfwSwapBuffers(EnigWindow.mainWindow.window);
			glfwPollEvents();
			EnigWindow.checkGLError();
		}
		//*/
	}
	
	public static void checkGLError() {
		int error;
		while ((error = glGetError()) != GL_NO_ERROR) {
			String errorInfo = "idk";
			switch (error) {
				case GL_NO_ERROR:
					errorInfo = "No error";
				case GL_INVALID_ENUM:
					errorInfo = "Invalid enum";
				case GL_INVALID_VALUE:
					errorInfo = "Invalid value";
				case GL_INVALID_OPERATION:
					errorInfo = "Invalid operation";
				case GL_STACK_OVERFLOW:
					errorInfo = "Stack overflow";
				case GL_STACK_UNDERFLOW:
					errorInfo = "Stack underflow";
				case GL_OUT_OF_MEMORY:
					errorInfo = "Out of memory";
				case GL_TABLE_TOO_LARGE:
					errorInfo = "Table too large";
				case GL_INVALID_FRAMEBUFFER_OPERATION:
					errorInfo = "Invalid framebuffer operation";
				default:
					errorInfo = "I rly dik";
			}
			System.out.println("id: " + error + " : " + errorInfo);
			throw new RuntimeException("id: " + error + " : " + errorInfo);
		}
	}
	
	public void update() {
		sync(60);
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == 1) {
				++keys[i];
			}
		}
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
	}
	
	public void resetOffsets() {
		cursorXOffset = 0;
		cursorYOffset = 0;
		++framesSinceLastSecond;
		long time = System.nanoTime();
		if (time - lastSecond >= 1000000000) {
			lastSecond = time;
			lastFrameCount = framesSinceLastSecond;
			framesSinceLastSecond = 0;
			if (printFrames) {
				System.out.println("FPS: " + lastFrameCount);
			}
		}
	}
	
	public boolean toggleCursorInput() {
		if (inputEnabled) {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}else {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}
		inputEnabled = !inputEnabled;
		return inputEnabled;
	}
	
	private long variableYieldTime, lastTime;
	
	/**
	 * An accurate sync method that adapts automatically
	 * to the system it runs on to provide reliable results.
	 *
	 * @param fps The desired frame rate, in frames per second
	 * @author kappa (On the LWJGL Forums)
	 */
	private void sync(int fps) {
		if (fps <= 0) return;
		
		long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
		// yieldTime + remainder micro & nano seconds if smaller than sleepTime
		long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
		long overSleep = 0; // time the sync goes over by
		
		try {
			while (true) {
				long t = System.nanoTime() - lastTime;
				
				if (t < sleepTime - yieldTime) {
					Thread.sleep(1);
				}else if (t < sleepTime) {
					// burn the last few CPU cycles to ensure accuracy
					Thread.yield();
				}else {
					overSleep = t - sleepTime;
					break; // exit while loop
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
			
			// auto tune the time sync should yield
			if (overSleep > variableYieldTime) {
				// increase by 200 microseconds (1/5 a ms)
				variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
			}
			else if (overSleep < variableYieldTime - 200*1000) {
				// decrease by 2 microseconds
				variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
			}
		}
	}
}