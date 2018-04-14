package Game;

import engine.*;

import static org.lwjgl.glfw.GLFW.*;

public class ProjectMain {

    private int frame = 0;
    public static ProjectMain Main;
    public static void main(String[] args) {
        Main = new ProjectMain();
    }
    public ProjectMain() {
        new Window();
        frame = 0;
        setup();
        while ( !glfwWindowShouldClose(Window.mainWindow.window) ) {
            Window.mainWindow.update();
            ++frame;
            gameLoop();
            cleanup();
        }
        Window.mainWindow.terminate();
    }
    public void setup() {
        //set variables here
    }
    public void gameLoop() {
        //game here
    }
    public void cleanup() {
        Program.disable();
        Window.mainWindow.resetOffsets();
        glfwSwapBuffers(Window.mainWindow.window);
        glfwPollEvents();
        Window.checkGLError();
    }
}