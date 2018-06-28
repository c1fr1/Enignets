package Game;

import engine.*;
import engine.Entities.GameObject;
import engine.Entities.Player;
import engine.OpenAL.Sound;
import engine.OpenAL.SoundSource;
import engine.OpenGL.*;
import engine.Platform.Box3d;
import engine.Platform.ModelPlatform;
import engine.Platform.PlatformSegment;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.opengl.GL11.*;

public class MainView extends EnigView {
	public static MainView main;
	
	//project variables
	
	private GameObject boxModel;
	private Texture boxTexture;
	
	private ShaderProgram boxShader;
	private ShaderProgram vecShader;
	private ShaderProgram matShader;
	
	private PlatformSegment platform;
	private GameObject platObj;
	private ShaderProgram plattyproggy;
	private Player player;
	
	private VAO platformModel;
	private VAO screen2;
	private VAO screena;
	
	private FBO mainFBO;
	
	private ShaderProgram joeProgram;
	
	private SoundSource boxSource;
	private Sound boxSound;
	
	@Override
	public void setup() {
		//set variables here
		EnigWindow.mainWindow.toggleCursorInput();
		platformModel = new VAO("res/plat.obj");
		platform = new ModelPlatform(platformModel);
		platObj = new GameObject(platformModel);
		plattyproggy = new ShaderProgram("platformShaders");
		player = new Player(100, 100, 0.05f);
		screen2 = new VAO(-1f, 0.5f, 0.5f, 0.5f);
		screena = new VAO(-1f, -1f, 2f, 2f);
		mainFBO = new FBO(new Texture(1920, 1080));
		joeProgram = new ShaderProgram("textureShaders");
		boxSource = new SoundSource();
		boxSound = new Sound("res/bounce.wav");
		boxModel = new GameObject(new Box3d(-1f,  -1f, -1f, 1, 1, 1).getVAO());
		boxTexture = new Texture("res/testex.png");
		boxShader = new ShaderProgram("thirdDimensionalShaders");
		
		boxSource.setLoop();
		boxSource.playSound(boxSound);
	}
	
	@Override
	public boolean loop() {
		//game here
		glEnable(GL_DEPTH_TEST);
		if (window.keys[GLFW_KEY_K] > 0) {
			boxModel.translate(0f, 0.1f, 0f);
		}else if (window.keys[GLFW_KEY_L] > 0) {
			boxModel.translate(0f, -0.1f, 0f);
		}
		mainFBO.prepareForTexture();
		plattyproggy.enable();
		player.updateRotations();
		player.updatePosition(platform, window);
		if (UserControls.pause(window)) {
			window.toggleCursorInput();
		}
		platObj.render(player);
		boxShader.enable();
		boxTexture.bind();
		boxModel.render(player);
		FBO.prepareDefaultRender();
		glDisable(GL_DEPTH_TEST);
		joeProgram.enable();
		mainFBO.getBoundTexture().bind();
		screena.fullRender();
		screen2.fullRender();
		screen2.fullRender();
		boxSource.updateSourcePosition(player);
		boxSource.setPos(boxModel);
		if (UserControls.quit(window)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		main = new MainView();
	}
	
	@Override
	public String getName() {
		return "Enignets Game";
	}
}
