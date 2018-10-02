package game.Systems;

import engine.Base.Components.*;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.System.InitializeSystem;
import engine.Model.ModelController;
import engine.Scene.Camera.FollowerCamera;
import engine.Scene.Lightning.Light;
import engine.Scene.Lightning.Shadow;
import engine.Texture.Texture;
import engine.UI.Quad;
import game.GameEntities.CubeEntity;
import game.GameEntities.PlayerEntity;
import game.Models.BombModel;
import game.Models.CrateModel;
import game.Models.CubeModel;
import game.Skyboxes.ClearSkybox;
import org.joml.Vector3f;

public class GameInitSystem extends InitializeSystem {
    PlayerEntity playerEntity;

    public GameInitSystem(EntityManager entityManager) {
        super(entityManager);
        initEnv();
        initModels();
        createObjects();
        createCamera();
    }

    private void initEnv() {
        Light light = new Light(new Vector3f(-0.1f, 1.0f, -0.1f), new Vector3f(1, 1, 1), 0);
        Shadow shadow = new Shadow(new Vector3f(light.getPosition().x, light.getPosition().y, light.getPosition().z),
                4096);

        entityManager.createEntity().addComponent(new LightComponent(light));
        entityManager.createEntity().addComponent(new ShadowComponent(shadow));
        entityManager.createEntity().addComponent(new SkyboxComponent(new ClearSkybox()));
    }

    private void initModels() {
        ModelController.getModelController().addModel(CrateModel.class, new CrateModel());
        ModelController.getModelController().addModel(CubeModel.class, new CubeModel());
        ModelController.getModelController().addModel(BombModel.class, new BombModel());
    }

    private void createObjects() {
        entityManager.createEntity().addComponent(new QuadComponent(
                new Quad(new Vector3f(0f, 0f, 0), new Vector3f(48f / 1600, 48f / 1600 * 16 / 9, 1f),
                        new Texture("textures/crosshairsg.png"))));

        playerEntity = new PlayerEntity(entityManager, new Vector3f(-3f, 5f, 2.6f), new Vector3f(0, 0, 0),
                new Vector3f(0.075f, 0.2f, 0.075f));

        for (int i = 0; i < 100; i++) {
            new CubeEntity(entityManager, new Vector3f((float)Math.random() * 10f, (float)Math.random() * 10f, (float)Math.random() * 10f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));
        }
    }
    private void createCamera() {
        FollowerCamera followerCamera = new FollowerCamera(playerEntity.getComponent(TransformComponent.class), 1);
        CameraComponent cameraComponent = new CameraComponent(followerCamera);
        entityManager.createEntity().addComponent(cameraComponent);
    }
}
