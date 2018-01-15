package game.Controllers;

import engine.Model.ModelController;
import engine.Object.GameObject;
import engine.Object.ObjectController;
import engine.Object.PhysicsComponent;
import engine.Scene.Camera.FollowerCamera;
import game.GameObjects.CubeObject;
import game.GameObjects.PlayerObject;
import game.Models.CubeModel;
import org.joml.Vector3f;

public class GameController {
    PlayerObject player;

    CubeObject cubeObject1, cubeObject2, cubeObject3, cubeObject4;

    GameObject scene;

    BombController bombController;

    ObjectController objectController;

    ModelController modelController;

    public GameController(ObjectController objectController, ModelController modelController) {
        this.objectController = objectController;
        this.modelController = modelController;
        createGameObjects();
    }

    public PlayerObject getPlayer() {
        return player;
    }

    public GameObject getScene() {
        return scene;
    }

    public void update(FollowerCamera camera) {
        bombController.handle(camera);
    }

    private void createGameObjects() {
        scene = new GameObject(null, null, new Vector3f(0), new Vector3f(0),new Vector3f(1));

        player = new PlayerObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1.2f, 5f, 2.6f), new Vector3f(0, 0, 0), new Vector3f(0.075f, 0.2f, 0.075f));

        objectController.addObject(new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f, 0.25f, 3f), new Vector3f(0, 0, 0), new Vector3f(0.25f, 0.25f, 0.25f)));

        cubeObject1 = new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f, -1f, 3f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));
        cubeObject2 = new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(1f, 1f, 7f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));
        cubeObject3 = new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(-3f, 3f, 7f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));
        cubeObject4 = new CubeObject(scene, modelController.getByModel(CubeModel.class), new Vector3f(-3f, 5f, 3f), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));

        objectController.addObject(cubeObject1);
        objectController.addObject(cubeObject2);
        objectController.addObject(cubeObject3);
        objectController.addObject(cubeObject4);

        createGame();
    }

    private void createGame() {
        player.setOnReset(() -> {
            cubeObject1.getPosition().x = 1f;
            cubeObject1.getPosition().y = -1f;
            cubeObject1.getPosition().z = 3f;

            cubeObject2.getPosition().x = 1f;
            cubeObject2.getPosition().y = 1f;
            cubeObject2.getPosition().z = 7f;

            cubeObject3.getPosition().x = -3f;
            cubeObject3.getPosition().y = 3f;
            cubeObject3.getPosition().z = 7f;

            cubeObject4.getPosition().x = -3f;
            cubeObject4.getPosition().y = 5f;
            cubeObject4.getPosition().z = 3f;

            cubeObject1.setRising(false);
            cubeObject2.setRising(false);
            cubeObject3.setRising(false);
            cubeObject4.setRising(false);
        });

        player.getByComponent(PhysicsComponent.class).setOnCollideWith(cubeObject1, () -> {
            if(player.getCurrentPlatform() == 3) {
                setPlatformCollision(cubeObject4,0);
            }
        });

        player.getByComponent(PhysicsComponent.class).setOnCollideWith(cubeObject2, () -> {
            if(player.getCurrentPlatform() == 0) {
                setPlatformCollision(cubeObject1,1);
            }
        });

        player.getByComponent(PhysicsComponent.class).setOnCollideWith(cubeObject3, () -> {
            if(player.getCurrentPlatform() == 1) {
                setPlatformCollision(cubeObject2,2);
            }
        });

        player.getByComponent(PhysicsComponent.class).setOnCollideWith(cubeObject4, () -> {
            if(player.getCurrentPlatform() == 2) {
                setPlatformCollision(cubeObject3,3);
            }
        });

        bombController = new BombController(objectController, modelController, scene, player);
    }

    private void setPlatformCollision(CubeObject cubeObject, int targetPlatform) {
        cubeObject.setRising(true);
        player.setCurrentPlatform(targetPlatform);
        player.incrementScore();
    }
}
