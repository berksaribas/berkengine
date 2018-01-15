package game.Scenes;

import engine.Object.GameObject;
import engine.Scene.Camera.FollowerCamera;
import engine.Scene.Lightning.Light;
import engine.Scene.Lightning.Shadow;
import engine.Scene.Scene;
import engine.Texture.Texture;
import engine.UI.Quad;
import game.Controllers.GameController;
import game.Models.BombModel;
import game.Models.CrateModel;
import game.Models.CubeModel;
import game.Models.TerrainModel;
import game.Skyboxes.ClearSkybox;
import org.joml.Vector3f;

public class GameScene extends Scene {
    FollowerCamera camera;

    Light light;

    Shadow shadow;

    ClearSkybox clearSkybox;

    GameController gameController;

    public GameScene(int width, int height) {
        super(width, height);
    }

    @Override
    protected void initializeLights() {
        light = new Light(new Vector3f(-0.3f, 1.0f,  -0.1f), new Vector3f(1, 1, 1), 0);
        shadow = new Shadow(new Vector3f(light.getPosition().x, light.getPosition().y, light.getPosition().z), 4096);
    }

    @Override
    protected void initializeCamera() {
        camera = new FollowerCamera(gameController.getPlayer(), 1);
    }

    @Override
    protected void initializeModels() {
        modelController.addModel(CrateModel.class, new CrateModel());
        modelController.addModel(CubeModel.class, new CubeModel());
        modelController.addModel(TerrainModel.class, new TerrainModel());
        modelController.addModel(BombModel.class, new BombModel());
    }

    @Override
    protected void createObjects() {
        clearSkybox = new ClearSkybox();

        uiController.addNewUIElement(new Quad(new Vector3f(0f,0f,0), new Vector3f(48f/1600,48f/1600 * 16/9,1f), new Texture("textures/crosshairsg.png"),quadShader));

//        uiController.addNewUIElement(new Quad(new Vector3f(-0.4f,-0.3f,0), new Vector3f(480f/1600,480f/1600 * 16/9,1f), renderer.gaussianBlur.getResult(), quadShader));
//
//        uiController.addNewUIElement(new Quad(new Vector3f(+0.4f,-0.3f,0), new Vector3f(480f/1600,480f/1600 * 16/9,1f), shadow.getColorTexture(), quadShader));

        gameController = new GameController(objectController, modelController);
    }

    @Override
    protected void update(float delta) {
        shadow.updateLightView(new Vector3f(light.getPosition().x, light.getPosition().y, light.getPosition().z), gameController.getPlayer().getPosition());

        camera.handleFPSMovement(delta);

        updateTree(gameController.getScene(), delta);

        gameController.update(camera);

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

    @Override
    protected void renderUI() {
        renderer.renderQuads(uiController.getQuads());
    }
}
