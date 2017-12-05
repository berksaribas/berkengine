package game.Scenes;

import engine.Input.KeyboardHandler;
import engine.Input.MouseHandler;
import engine.Model.TexturedModel;
import engine.Physics.AABB;
import engine.Scene.Lightning.Shadow;
import game.GameObjects.PlayerObject;
import game.Models.CrateModel;
import engine.Object.GameObject;
import engine.Scene.Camera.FollowerCamera;
import engine.Scene.Lightning.Light;
import engine.Scene.Scene;
import game.Models.TerrainModel;
import game.Models.TreeModel;
import game.Skyboxes.ClearSkybox;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
    FollowerCamera camera;

    CrateModel crateModel;
    TreeModel treeModel;
    TerrainModel terrainModel;

    PlayerObject player;
    GameObject scene, ground, cylinder;
    Light light;
    Shadow shadow;
    ClearSkybox clearSkybox;

    HashMap<TexturedModel, ArrayList<GameObject>> objects;

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
        crateModel = new CrateModel();
        treeModel = new TreeModel();
        terrainModel = new TerrainModel();
    }

    @Override
    protected void createObjects() {


        clearSkybox = new ClearSkybox();

        objects = new HashMap<>();
        objects.put(crateModel, new ArrayList<>());
        objects.put(treeModel, new ArrayList<>());
        objects.put(terrainModel, new ArrayList<>());

        scene = new GameObject(null, null, new Vector3f(0), new Vector3f(0),new Vector3f(1));
        player = new PlayerObject(scene, crateModel, new Vector3f(0, 2f, 0), new Vector3f(0, 0, 0), new Vector3f(0.025f, 0.06f, 0.025f));
        ground = new GameObject(scene, terrainModel, new Vector3f(0, -0.1f, 0), new Vector3f(0, 0, 0), new Vector3f(10f, 0.1f, 10f));

        objects.get(terrainModel).add(ground);
        objects.get(crateModel).add(new GameObject(scene, crateModel, new Vector3f(1f, 0 , 1f ), new Vector3f(0, 0, 0), new Vector3f(0.04f, 0.04f, 0.04f)));
        cylinder = new GameObject(scene, treeModel, new Vector3f(1f + 0.2f, 0.1f, 2.2f + 0.4f), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f));
        objects.get(treeModel).add(cylinder);
        objects.get(treeModel).add(new GameObject(scene, treeModel, new Vector3f(1f + 0.2f, 0.1f, 3.5f), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f)));
        objects.get(treeModel).add(new GameObject(scene, treeModel, new Vector3f(1f + 0.2f, 0.3f, 3.5f), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f)));
        objects.get(treeModel).add(new GameObject(scene, treeModel, new Vector3f(1f + 0.2f, 0.5f, 3.5f), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f)));
        objects.get(treeModel).add(new GameObject(scene, treeModel, new Vector3f(1f + 0.2f, 0.7f, 3.5f), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f)));
        objects.get(treeModel).add(new GameObject(scene, treeModel, new Vector3f(1f + 0.2f, 0.9f, 3.5f), new Vector3f(0, 0, 0), new Vector3f(0.1f, 0.1f, 0.1f)));



    }

    @Override
    protected void update(float delta) {
        shadow.updateLightView(new Vector3f(light.getPosition().x, light.getPosition().y, light.getPosition().z), player.getPosition());

        camera.handleFPSMovement(delta);

        player.update(delta);

        camera.followObject();

        camera.update(objectShader);

        objectShader.setLight(light.getPosition(), light.getColor());
    }

    @Override
    protected void renderObjects() {
        objectShader.setDepthBias(shadow.getDepthBias());
        renderer.renderObject(objects, objectShader, shadow);
    }

    @Override
    protected void renderSkybox() {
        camera.update(skyboxShader);
        renderer.renderSkybox(clearSkybox);
    }

    @Override
    protected void renderShadows() {
        renderer.renderShadows(objects, shadow, shadowShader);
    }
}
