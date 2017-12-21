package engine.Scene;

import engine.Helper.MatrixHelper;
import engine.Model.ModelController;
import engine.Object.ObjectController;
import engine.Renderer;
import engine.Shader.ObjectShader;
import engine.Shader.ShadowShader;
import engine.Shader.SkyboxShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public abstract class Scene {
    protected Renderer renderer;
    protected ObjectController objectController;
    protected ModelController modelController;

    private final float FOV = 50;
    private final float NEARPLANE = 0.025f;
    private final float FARPLANE = 100;

    private Matrix4f projectionMatrix;

    protected ObjectShader objectShader;
    protected SkyboxShader skyboxShader;
    protected ShadowShader shadowShader;
    
    private int WIDTH, HEIGHT;

    public Scene(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        objectController = new ObjectController();
        modelController = new ModelController();
        setPerspectiveMatrix();
        initializeShaders();
        initializeLights();
        initializeModels();
        createObjects();
        initializeCamera();
        setGL();
    }

    protected void setGL() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    protected void setPerspectiveMatrix() {
        renderer = new Renderer();
        projectionMatrix = MatrixHelper
                .createProjectionMatrix(WIDTH, HEIGHT, FOV, NEARPLANE, FARPLANE);
    }

    protected void initializeShaders() {
        objectShader = new ObjectShader();
        objectShader.start();
        objectShader.setProjectionMatrix(projectionMatrix);
        objectShader.stop();

        skyboxShader = new SkyboxShader();
        skyboxShader.start();
        skyboxShader.setProjectionMatrix(projectionMatrix);
        skyboxShader.stop();

        shadowShader = new ShadowShader();

    }

    protected abstract void initializeLights();

    protected abstract void initializeCamera();

    protected abstract void initializeModels();

    protected abstract void createObjects();

    protected abstract void update(float delta);

    protected abstract void renderObjects();

    protected abstract void renderSkybox();

    protected abstract void renderShadows();

    public void loop(float delta) {

        objectShader.start();

        update(delta);

        objectShader.stop();

        renderer.prepare();

        shadowShader.start();

        renderShadows();

        shadowShader.stop();

        GL11.glViewport(0, 0, WIDTH, HEIGHT);

        skyboxShader.start();

        renderSkybox();

        skyboxShader.stop();

        objectShader.start();

        renderObjects();

        objectShader.stop();
    }

}
