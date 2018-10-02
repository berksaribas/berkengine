package engine.Scene;

import engine.Base.Systems.*;
import engine.Base.Utils.ShaderUtils;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.System.BaseSystem;
import engine.Helper.MatrixHelper;
import engine.Shader.ObjectShader;
import engine.Shader.QuadShader;
import engine.Shader.ShadowShader;
import engine.Shader.SkyboxShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public abstract class Scene {
    private EntityManager entityManager;
    private Matrix4f projectionMatrix;
    public static int WIDTH, HEIGHT;
    private ArrayList<BaseSystem> systems = new ArrayList<>();
    public static float deltaTime;

    public Scene(int width, int height) {
        entityManager = new EntityManager();
        WIDTH = width;
        HEIGHT = height;
        setPerspectiveMatrix();
        initializeShaders();
        setGL();
        setupSystems();
    }

    public abstract void registerSystems(ArrayList<BaseSystem> systems);

    private void setupSystems() {
        registerSystems(systems);

        systems.add(new PhysicsSystem(entityManager));
        systems.add(new CameraSystem(entityManager));
        systems.add(new ShadowSystem(entityManager));
        systems.add(new EffectSystem(entityManager));
        systems.add(new SkyboxSystem(entityManager));
        systems.add(new RenderSystem(entityManager));
        systems.add(new QuadSystem(entityManager));
        systems.add(new DestroySystem(entityManager));
    }

    private void setGL() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    private void setPerspectiveMatrix() {
        float fov = 60;
        float nearPlane = 0.025f;
        float farPlane = 100;
        projectionMatrix = MatrixHelper
                .createProjectionMatrix(WIDTH, HEIGHT, fov, nearPlane, farPlane);
    }

    protected void initializeShaders() {
        ObjectShader objectShader = new ObjectShader();
        objectShader.start();
        objectShader.setProjectionMatrix(projectionMatrix);
        objectShader.stop();
        ShaderUtils.getInstance().registerShader(objectShader);

        SkyboxShader skyboxShader = new SkyboxShader();
        skyboxShader.start();
        skyboxShader.setProjectionMatrix(projectionMatrix);
        skyboxShader.stop();
        ShaderUtils.getInstance().registerShader(skyboxShader);

        ShadowShader shadowShader = new ShadowShader();
        ShaderUtils.getInstance().registerShader(shadowShader);

        QuadShader quadShader = new QuadShader();
        ShaderUtils.getInstance().registerShader(quadShader);
    }

    public void loop(float delta) {
        deltaTime = delta;
        long time = System.currentTimeMillis();

        for (int i = 0; i < systems.size(); i++) {
            systems.get(i).update();
        }
        System.out.println("Frame CPU execution took: " + (System.currentTimeMillis() - time) + " miliseconds");

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
