package game.Controllers;

import engine.Input.KeyboardHandler;
import engine.Model.ModelController;
import engine.Object.GameObject;
import engine.Object.ObjectController;
import engine.Object.PhysicsComponent;
import engine.Scene.Camera.FollowerCamera;
import game.GameObjects.BombObject;
import game.GameObjects.PlayerObject;
import game.Models.BombModel;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class BombController {
    private boolean thrown = false;
    private BombObject bombObject;
    private GameObject scene;
    private PlayerObject player;
    private ObjectController objectController;
    private ModelController modelController;

    public BombController(ObjectController objectController, ModelController modelController, GameObject scene, PlayerObject player) {
        this.scene = scene;
        this.player = player;
        this.objectController = objectController;
        this.modelController = modelController;
    }

    public void handle(FollowerCamera camera) {
        if(KeyboardHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT) && !thrown) {
            bombObject = new BombObject(scene, modelController.getByModel(BombModel.class), new Vector3f(camera.getEye()), new Vector3f(0, 0, 0), new Vector3f(0.08f, 0.01f, 0.08f));
            objectController.addObject(bombObject);
            bombObject.getByComponent(PhysicsComponent.class).addMomentum(player.getByComponent(PhysicsComponent.class).getMomentum()); // initial moment
            bombObject.throwBomb(new Vector3f((float) Math.sin(-player.getRotation().y) * 5 * (float)(Math.PI / 2 - Math.abs(camera.yAngle)), camera.getCenter().y - camera.getEye().y,-(float) Math.cos(-player.getRotation().y) * 5 * (float)(Math.PI / 2  - Math.abs(camera.yAngle))));
            thrown = true;
        }

        if(KeyboardHandler.isKeyDown(GLFW_KEY_LEFT_CONTROL) && thrown) {
            Vector3f explosion = new Vector3f(player.getPosition()).sub(bombObject.getPosition());
            float length = explosion.length();
            explosion.normalize();

            explosion.div(Math.max(length * length * 1 / 2, 0.20f));
            if(length < 2f) {
                player.getByComponent(PhysicsComponent.class).addMomentum(explosion);
            }
            objectController.removeObject(bombObject);
            thrown = false;
        }
    }

}
