package game.Scenes;

import engine.Scene.Lightning.Shadow;
import game.Controllers.BombController;
import game.GameObjects.CubeObject;
import game.GameObjects.PlayerObject;
import game.Models.CrateModel;
import engine.Object.GameObject;
import engine.Scene.Camera.FollowerCamera;
import engine.Scene.Lightning.Light;
import engine.Scene.Scene;
import game.Models.TerrainModel;
import game.Models.CubeModel;
import game.Skyboxes.ClearSkybox;
import org.joml.Vector3f;

public class GameScene extends Scene {
    FollowerCamera camera;

    PlayerObject player;

    GameObject scene;

    Light light;

    Shadow shadow;

    ClearSkybox clearSkybox;

    BombController bombController;

    public GameScene(int width, int height) {
        super(width, height);
    }

    @Override
    protected void initializeLights() {
        light = new Light(new Vector3f(-0.2f, 1.0f, -0.3f), new Vector3f(1, 1, 1), 0);
        shadow = new Shadow(new Vector3f(light.getPosition().x, light.getPosition().y, light.getPosition().z));
    }

    @Override
    protected void initializeCamera() {
        camera = new FollowerCamera(player, 1);
    }

    @Override
    protected void initializeModels() {
        modelController.addModel(CrateModel.class, new CrateModel());
        modelController.addModel(CubeModel.class, new CubeModel());
        modelController.addModel(TerrainModel.class, new TerrainModel());
    }

    @Override
    protected void createObjects() {
        clearSkybox = new ClearSkybox();

        scene = new GameObject(null, null, new Vector3f(0), new Vector3f(0),new Vector3f(1));

        player = new PlayerObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1.2f, 5f, 2.6f), new Vector3f(0, 0, 0), new Vector3f(0.075f, 0.2f, 0.075f));

        objectController.addObject(new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f + 0.2f, -0.7f, 2.6f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f)));

        objectController.addObject(new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f + 0.2f, 0.3f, 3.8f), new Vector3f(0, 0, 0), new Vector3f(0.2f, 0.2f, 0.2f)));

        objectController.addObject(new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f + 0.2f, -0.3f, 5f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f)));

        objectController.addObject(new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f + 0.2f, 0.6f, 5f), new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)));

        objectController.addObject(new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(5f, 2f, 5f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f)));

        bombController = new BombController(objectController, modelController, scene, player);

    }

    @Override
    protected void update(float delta) {
        shadow.updateLightView(new Vector3f(light.getPosition().x, light.getPosition().y, light.getPosition().z), player.getPosition());

        camera.handleFPSMovement(delta);

        updateTree(scene, delta);

        bombController.handle(camera, delta);

        camera.followObject();

        camera.update(objectShader);

        objectShader.setLight(light.getPosition(), light.getColor());

    }

    public void updateTree(GameObject parent, float delta) {
        for(GameObject object : parent.getChildren()) {
            object.update(delta);
            updateTree(object, delta);
        }
    }

    @Override
    protected void renderObjects() {
        objectShader.setDepthBias(shadow.getDepthBias());
        renderer.renderObject(objectController.getObjects(), objectShader, shadow);
    }

    @Override
    protected void renderSkybox() {
        camera.update(skyboxShader);
        renderer.renderSkybox(clearSkybox);
    }

    @Override
    protected void renderShadows() {
        renderer.renderShadows(objectController.getObjects(), shadow, shadowShader);
    }
}
