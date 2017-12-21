package engine;

import engine.Input.KeyboardHandler;
import engine.Input.MouseHandler;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

    private int width, height;
    private long windowID;
    private String windowTitle;
    private boolean vsSync = true, resized;
    private float delta;
    private long lastFrameTime;

    public DisplayManager(String windowTitle, int width, int height) {
        this.windowTitle = windowTitle;
        this.width = width;
        this.height = height;
    }

    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        windowID = glfwCreateWindow(width, height, windowTitle, NULL, NULL);

        if ( windowID == NULL ) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });


        glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetKeyCallback(windowID, new KeyboardHandler());

        glfwSetCursorPosCallback(windowID, new MouseHandler());

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                windowID,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        glfwMakeContextCurrent(windowID);

        if (vsSync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }

        glfwShowWindow(windowID);

        GL.createCapabilities();

        GL11.glEnable(GL13.GL_MULTISAMPLE);

        lastFrameTime = System.nanoTime();
    }

    public boolean closeRequested() {
        return glfwWindowShouldClose(windowID);
    }

    public void update(){
        glfwSwapBuffers(windowID);
        glfwPollEvents();
        long currentFrameTime = System.nanoTime();
        delta = (currentFrameTime - lastFrameTime) / 1000000000f;
        lastFrameTime = currentFrameTime;
    }

    public void closeDisplay(){
        glfwTerminate();
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public float getDelta() {
        return delta;
    }
}
