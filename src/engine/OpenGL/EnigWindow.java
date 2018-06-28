package engine.OpenGL;

import engine.Entities.Camera;
import engine.Entities.GameObject;
import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EnigWindow {
	public long id;
	
	private int width;
	private int height;
	
	public int fps = 60;
	
	public float cursorXFloat;
	public float cursorYFloat;
	
	public double cursorXDouble;
	public double cursorYDouble;
	
	public double cursorXOffset;
	public double cursorYOffset;
	
	public boolean inputEnabled = false;
	public boolean printFrames = false;
	public boolean fullscreen = false;
	
	public int[] mouseButtons = new int[GLFW_MOUSE_BUTTON_LAST + 1];
	public int[] keys = new int[GLFW_KEY_LAST + 1];
	public int mousePressStatus;
	
	public int framesSinceLastSecond = 0;
	public int lastFrameCount = 0;
	public long lastSecond;
	
	private long device, context;
	private ALCCapabilities deviceCaps;
	
	public static EnigWindow mainWindow;
	
	/**
	 * creates an undecorated window
	 * @param title name of the window
	 */
	public EnigWindow(String title) {
		init(title, true);
	}
	
	/**
	 * creates a window
	 * @param title name of the window
	 * @param decorated if it should be decorated or not
	 */
	public EnigWindow(String title, boolean decorated) {
		init(title, decorated);
	}
	
	/**
	 * creates a window
	 * @param w width
	 * @param h height
	 * @param title name of the window
	 */
	public EnigWindow(int w, int h, String title) {
		init(w, h, title, true);
	}
	
	/**
	 * creates a window
	 * @param w width
	 * @param h height
	 * @param title name of the window
	 * @param decorated if it should be decorated or not
	 */
	public EnigWindow(int w, int h, String title, boolean decorated) {
		init(w, h, title, decorated);
	}
	
	/**
	 * close the window, delete all resources, close openGL, openAL, and clean things up
	 */
	public void terminate() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(id);
		glfwDestroyWindow(id);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
		// Terminate OpenAL
		alcDestroyContext(context);
		alcCloseDevice(device);
		
		for (Integer i: VAO.vaoIDs) {
			glDeleteVertexArrays(i);
		}
		for (Integer i: VBO.vboIDs) {
			glDeleteBuffers(i);
		}
		for (Integer i: VAO.vaoIDs) {
			glDeleteTextures(i);
		}
		for (Integer i: FBO.fboIDs) {
			glDeleteFramebuffers(i);
		}
		for (Integer i: FBO.renderBufferIDs) {
			glDeleteRenderbuffers(i);
		}
		for (Integer i: Sound.soundIDs) {
			alDeleteBuffers(i);
		}
		for (Integer i: SoundSource.sourceIDs) {
			alDeleteSources(i);
		}
	}
	
	/**
	 * makes a fullscreen window
	 * @param title name of the window
 	 * @param decorated if the window is decorated or not
	 */
	public void init(String title, boolean decorated) {
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
		glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
		
		// Create the window
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = vidMode.width();
		height = vidMode.height();
		
		id = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);//creates window
		
		if (id == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
			if (key >= 0) {
				keys[key] = action;
			}
		});
		
		glfwSetCursorPosCallback(id, new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				if (inputEnabled) {
					cursorXOffset += cursorXDouble - xpos;
					cursorYOffset += cursorYDouble - ypos;
					cursorXDouble = xpos;
					cursorYDouble = ypos;
					cursorXFloat = (float) (2*xpos - width) / (float) width;
					cursorYFloat = (float) (2*ypos - height) / (float) height;
				}else {
					cursorXDouble = xpos;
					cursorYDouble = ypos;
					cursorXFloat = (float) (2*xpos - width) / (float) width;
					cursorYFloat = (float) -(2*ypos - height) / (float) height;
				}
			}
		});
		
		glfwSetMouseButtonCallback(id, (long window, int button, int action, int mods) -> {
			mouseButtons[button] = action;
		});
		
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			
			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(id, pWidth, pHeight);
			
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window
			glfwSetWindowPos(
					id,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(id);
		// Enable v-sync
		glfwSwapInterval(1);
		
		glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		
		// Make the window visible
		glfwShowWindow(id);
		
		glfwSetWindowSizeCallback(id, (long window, int w, int h) -> {
			width = w;
			height = h;
			glViewport(0, 0, width, height);
		});
		
		initOpenGL();
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_CULL_FACE);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		initOpenAL();
		
		mainWindow = this;
		
		runOpeningSequence();
	}
	
	/**
	 * creates a window
	 * @param iw window width
	 * @param ih window height
	 * @param title window name
	 * @param decorated if the window is decorated or not?
	 */
	public void init(int iw, int ih, String title, boolean decorated) {
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
		glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
		
		// Create the window
		//GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = iw;
		height = ih;
		
		id = glfwCreateWindow(width, height, title, NULL, NULL);//fullscreen
		
		if (id == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
			if (key >= 0) {
				keys[key] = action;
			}
		});
		
		glfwSetCursorPosCallback(id, new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				if (inputEnabled) {
					cursorXOffset += cursorXDouble - xpos;
					cursorYOffset += cursorYDouble - ypos;
					cursorXDouble = xpos;
					cursorYDouble = ypos;
					cursorXFloat = (float) (2*xpos - width) / (float) width;
					cursorYFloat = (float) (2*ypos - height) / (float) height;
				}else {
					cursorXDouble = xpos;
					cursorYDouble = ypos;
					cursorXFloat = (float) (2*xpos - width) / (float) width;
					cursorYFloat = (float) -(2*ypos - height) / (float) height;
				}
			}
		});
		
		glfwSetMouseButtonCallback(id, (long window, int button, int action, int mods) -> {
			mouseButtons[button] = action;
		});
		
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*
			
			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(id, pWidth, pHeight);
			
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window
			glfwSetWindowPos(
					id,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(id);
		// Enable v-sync
		glfwSwapInterval(1);
		
		glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		
		// Make the window visible
		glfwShowWindow(id);
		
		glfwSetWindowSizeCallback(id, (long window, int w, int h) -> {
			width = w;
			height = h;
			glViewport(0, 0, width, height);
		});
		
		initOpenGL();
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_CULL_FACE);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		initOpenAL();
		
		mainWindow = this;
		
		runOpeningSequence();
	}
	
	/**
	 * runs opening sequence
	 */
	public void runOpeningSequence() {
		int frame = 0;
		ShaderProgram openingShader = new ShaderProgram("res/shaders/openingShaders/vert", "res/shaders/openingShaders/frag");
		GameObject openingObject = new GameObject("res/enignets.obj");
		Camera openingCamera = new Camera((float) (0.27*Math.PI), 0.01f, 1000f);
		
		openingCamera.usingStaticRotation = true;
		openingCamera.orderOfTransformations = new int[] {0, 2, 1};
		openingCamera.translate(0f, 0f, -5f);
		openingCamera.pitch((float)  Math.PI/2);
		
		while (openingCamera.getPitch() < (float) (3*Math.PI/2) && keys[GLFW_KEY_SPACE] != 1) {
			if (glfwWindowShouldClose(id)) {
				return;
			}
			openingCamera.setPos(0f, 0f, (float) (13-10f*Math.pow(150f-(float) frame*2, 6f)/1.1390625e+13));
			openingCamera.pitch(0.022f);
			update();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			frame += 1;
			openingShader.enable();
			openingShader.shaders[2].uniforms[0].set(new float[] {(float) frame});
			openingShader.shaders[2].uniforms[1].set(new float[] {(float) width, (float) height});
			openingObject.render(openingCamera);
			ShaderProgram.disable();
			glfwSwapBuffers(id);
			glfwPollEvents();
			sync(60);
		}
		frame = 286;
		openingCamera.setPos(0f, 0f, 7.44497655929f);
		openingCamera.setPitch((float) (3*Math.PI/2));
		update();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		openingShader.enable();
		openingShader.shaders[2].uniforms[0].set(new float[] {(float) frame});
		openingShader.shaders[2].uniforms[1].set(new float[] {(float) width, (float) height});
		openingObject.render(openingCamera);
		ShaderProgram.disable();
		glfwSwapBuffers(id);
		glfwPollEvents();
		float fov = (float) (0.27*Math.PI);
		while (frame < 343) {
			if (glfwWindowShouldClose(id)) {
				return;
			}
			if (keys[GLFW_KEY_SPACE] == 1) {
				frame = 370;
			}
			++frame;
			fov -= 0.27*Math.PI/57.01;
			openingCamera.setPos(0f, 0f, (float)(16.88933655389*Math.sqrt(1f/(1+Math.tan(fov)*Math.tan(fov)))/(2*Math.sin(fov))));
			openingCamera.setPerspective(fov, 0.01f, 99999999999999999999999999999999999999f);
			update();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			openingShader.enable();
			openingShader.shaders[2].uniforms[0].set(new float[]{(float) frame});
			openingShader.shaders[2].uniforms[1].set(new float[]{(float) width, (float) height});
			openingObject.render(openingCamera);
			ShaderProgram.disable();
			resetOffsets();
			glfwSwapBuffers(id);
			glfwPollEvents();
			EnigWindow.checkGLError();
			sync(60);
		}
		fov = 0.0001f;
		openingCamera.setPos(0f, 0f, (float)(16.88933655389*Math.sqrt(1f/(1+Math.tan(fov)*Math.tan(fov)))/(2*Math.sin(fov))));
		openingCamera.setPerspective(fov, 0.01f, 99999999999999999999999999999999999999f);
		update();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		openingShader.enable();
		openingShader.shaders[2].uniforms[0].set(new float[]{(float) frame});
		openingShader.shaders[2].uniforms[1].set(new float[]{(float) width, (float) height});
		openingObject.render(openingCamera);
		ShaderProgram.disable();
		resetOffsets();
		glfwSwapBuffers(id);
		glfwPollEvents();
		while (frame < 370) {
			++frame;
			if (glfwWindowShouldClose(id)) {
				return;
			}
			if (keys[GLFW_KEY_SPACE] > 0) {
				return;
			}
			resetOffsets();
			glfwPollEvents();
			sync(60);
		}
		glDisable(GL_CULL_FACE);
	}
	
	/**
	 * checks for any openGL errors
	 */
	public static void checkGLError() {
		int error;
		while ((error = glGetError()) != GL_NO_ERROR) {
			String errorInfo = "idk";
			switch (error) {
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
	
	/**
	 * initializes openGL
	 */
	public void initOpenGL() {
		GL.createCapabilities();
	}
	
	/**
	 * initializes openAL
	 */
	public void initOpenAL() {
		device = alcOpenDevice((ByteBuffer) null);
		if (device == 0)
			throw new IllegalStateException("Failed to open the default device.");
		
		deviceCaps = ALC.createCapabilities(device);
		
		context = alcCreateContext(device, (IntBuffer) null);
		if (context == 0)
			throw new IllegalStateException("Failed to create an OpenAL context.");
		
		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
	}
	
	/**
	 * syncs the fps and
	 */
	public void update() {
		sync(fps);
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == 1) {
				++keys[i];
			}
		}
	}
	
	/**
	 * reset cursor offsets
	 */
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
	
	/**
	 * changes whether or not the cursor is hidden and being used to move the camera
	 * @return if the cursor is being used to move the camera
	 */
	public boolean toggleCursorInput() {
		if (inputEnabled) {
			glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}else {
			glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
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
	
	/**
	 * returns the width of the window
	 * @return width of the window
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * returns the height of the window
	 * @return height of the window
	 */
	public int getHeight() {
		return height;
	}
}