package engine.OpenGL;

import engine.Entities.Camera;
import engine.Entities.GameObject;
import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class EnigWindow {
	public long id;

	private int width;
	private int height;
	private float aspectRatio;

	public int fps = 144;

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
	private ALCCapabilities deviceCapablities;

	public static EnigWindow mainWindow;

	/**
	 * creates an undecorated window
	 * @param title name of the window
	 */
	public EnigWindow(String title) {
		maxInit(title, false, true);
	}

	/**
	 * creates an undecorated window
	 * @param title name of the window
	 * @param iconPath path to the icon for the window
	 */
	public EnigWindow(String title, String iconPath) {
		maxInit(title, false, true);
		setIcon(iconPath);
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
	 * @param title name of the window
	 * @param iconPath path to the icon for the window
	 * @param decorated if it should be decorated or not
	 */
	public EnigWindow(String title, String iconPath, boolean decorated) {
		init(title, decorated);
		setIcon(iconPath);
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
	 * @param iconPath path to the icon for the window
	 */
	public EnigWindow(int w, int h, String title, String iconPath) {
		init(w, h, title, true);
		setIcon(iconPath);
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
	 * creates a window
	 * @param w width
	 * @param h height
	 * @param title name of the window
	 * @param title name of the window
	 * @param decorated if it should be decorated or not
	 */
	public EnigWindow(int w, int h, String title, String iconPath, boolean decorated) {
		init(w, h, title, decorated);
		setIcon(iconPath);
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
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		// Create the window
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = vidMode.width();
		height = vidMode.height();
		setAspectRatio();

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
			if (button >= 0) {
				if (action == 0) {
					mouseButtons[button] = 3;
				} else {
					mouseButtons[button] = action;
				}
			}
		});

		glfwSetScrollCallback(id, (long window, double xOffset, double yOffset) -> {

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
			setAspectRatio();
			setViewport();
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
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		// Create the window
		//GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = iw;
		height = ih;
		setAspectRatio();

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
			if (button >= 0) {
				if (action == 0) {
					mouseButtons[button] = 3;
				} else {
					mouseButtons[button] = action;
				}
			}
		});

		glfwSetScrollCallback(id, (long window, double xOffset, double yOffset) -> {

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
			setViewport();
		});

		initOpenGL();
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_CULL_FACE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		initOpenAL();

		mainWindow = this;
	}

	/**
	 * creates a maximized window
	 * @param title window name
	 * @param decorated if the window is decorated or not?
	 */
	public void maxInit(String title, boolean decorated, boolean resizeable) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() ) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, resizeable ? 1 : 0); // the window will be resizable
		glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		// Create the window
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = vidMode.width();
		height = vidMode.height();
		setAspectRatio();

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
			if (button >= 0) {
				if (action == 0) {
					mouseButtons[button] = 3;
				} else {
					mouseButtons[button] = action;
				}
			}
		});

		glfwSetScrollCallback(id, (long window, double xOffset, double yOffset) -> {

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
			setViewport();
		});

		initOpenGL();
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		glEnable(GL_CULL_FACE);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		initOpenAL();

		mainWindow = this;
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

	private void setAspectRatio() {
		aspectRatio = (float) width / (float) height;
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

		deviceCapablities = ALC.createCapabilities(device);

		context = alcCreateContext(device, (IntBuffer) null);
		if (context == 0)
			throw new IllegalStateException("Failed to create an OpenAL context.");

		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCapablities);
	}

	/**
	 * syncs the fps and
	 */
	public void update() {
		sync(fps);
		for (int i = 0; i < keys.length; ++i) {
			if (keys[i] == 1) {
				++keys[i];
			}
		}
		for (int i = 0; i < mouseButtons.length; ++i) {
			if (mouseButtons[i] == 1) {
				++mouseButtons[i];
			}
			if (mouseButtons[i] == 3) {
				mouseButtons[i] = 0;
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
					//Thread.sleep(1);
				}else if (t < sleepTime) {
					// burn the last few CPU cycles to ensure accuracy
					Thread.yield();
				}else {
					overSleep = t - sleepTime;
					break; // exit while loop
				}
			}
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

	public void setViewport() {
		int[] fbwidth = new int[1];
		int[] fbheight = new int[1];
		glfwGetFramebufferSize(id, fbwidth, fbheight);
		glViewport(0, 0, fbwidth[0], fbheight[0]);
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

	public float getAspectRatio() {
		return aspectRatio;
	}

	public Matrix4f getSquarePerspectiveMatrix(float maxHeight) {
		return new Matrix4f().ortho(-maxHeight/2f * getAspectRatio(),  maxHeight/2f * getAspectRatio(), -maxHeight/2f, maxHeight/2f, 0, 1);
	}

	public Matrix4f getAlignedPerspectiveMatrix(float maxHeight) {
		return new Matrix4f().ortho(0,  maxHeight * getAspectRatio(), -maxHeight/2f, maxHeight/2f, 0, 1);
	}

	public float getCursorXAligned(float maxHeight) {
		return (1 + cursorXFloat) * maxHeight / 2f * aspectRatio;
	}

	public float getCursorXScaled(float maxHeight) {
		return cursorXFloat * maxHeight / 2f * aspectRatio;
	}
	public float getCursorYScaled(float maxHeight) {
		return cursorYFloat * maxHeight / 2f;
	}

	public void setIcon(String imagePath) {

		GLFWImage image = makeGLFWImage(imagePath);
		GLFWImage.Buffer buffer = GLFWImage.malloc(1);
		buffer.put(0, image);

		glfwSetWindowIcon(id, buffer);
	}

	public static GLFWImage makeGLFWImage(String imagePath) {
		BufferedImage b = null;
		try {
			ClassLoader l = EnigWindow.class.getClassLoader();
			b = ImageIO.read(EnigWindow.class.getClassLoader().getResource(imagePath));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		int bwi = b.getWidth();
		int bhi = b.getHeight();
		int len = bwi * bhi;

		int[] rgbArray = new int[len];

		System.out.println();

		b.getRGB(0, 0, bwi, bhi, rgbArray, 0, bwi);

		ByteBuffer buffer = BufferUtils.createByteBuffer(len * 4);

		for(int i = 0; i < len; ++i) {
			int rgb = rgbArray[i];
			buffer.put((byte)(rgb >> 16 & 0xff));
			buffer.put((byte)(rgb >>  8 & 0xff));
			buffer.put((byte)(rgb       & 0xff));
			buffer.put((byte)(rgb >> 24 & 0xff));
		}

		buffer.flip();

		// create a GLFWImage
		GLFWImage img= GLFWImage.create();
		img.width(bwi);     // setup the images' width
		img.height(bhi);   // setup the images' height
		img.pixels(buffer);   // pass image data

		return img;
	}
}