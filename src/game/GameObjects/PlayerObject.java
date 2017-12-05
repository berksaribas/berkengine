package game.GameObjects;

import engine.Input.KeyboardHandler;
import engine.Input.MouseHandler;
import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.Physics.AABB;
import game.Models.TerrainModel;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class PlayerObject extends GameObject {
    float speed = 60, deltaY, maxJump = 0;
    State state;

    enum State{
        FALLING, WALKING, JUMPING
    }

    public PlayerObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(parent, model, position, rotation, scale);
        state = State.FALLING;
    }

    @Override
    public void update(float delta) {

        maxJump += 0.03f;

        deltaY = position.y;

        position.y -= delta * (1 + maxJump);

        checkCollisions(delta * (1 + maxJump));

        deltaY -= position.y;

        if(deltaY > 0 && state != State.JUMPING) state = State.FALLING;

        for(int i = 0; i < 2; i++) {
            Vector3f previous = new Vector3f(position);

            if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
                if(i == 0) position.x -= 0.02f * (float) Math.sin(Math.PI / 2 + -rotation.y) * delta * speed;
                else position.z += 0.02f * (float) Math.cos(Math.PI / 2 + -rotation.y) * delta * speed;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
                if(i == 0) position.x -= 0.02f * (float) Math.sin(-rotation.y - Math.PI / 2) * delta * speed;
                else position.z += 0.02f * (float) Math.cos(-rotation.y - Math.PI / 2) * delta * speed;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
                if(i == 0) position.x += 0.02f * (float) Math.sin(-rotation.y) * delta * speed;
                else position.z -= 0.02f * (float) Math.cos(-rotation.y) * delta * speed;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
                if(i == 0) position.x -= 0.02f * (float) Math.sin(-rotation.y) * delta * speed;
                else position.z += 0.02f * (float) Math.cos(-rotation.y) * delta * speed;
            }

            if (checkCollisions(0)) position.set(previous);
        }

        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE) && state == State.WALKING) {
            state = State.JUMPING;
            maxJump = 0;
        }

        if(state == State.JUMPING) {
            position.y += (1.1f - maxJump) * delta * speed * 0.1f;
            maxJump += (1.1f - maxJump) * delta * speed * 0.1f;
        }
        if(maxJump > 1) {
            state = State.FALLING;
            maxJump = 0;
        }


    }

    private boolean checkCollisions(float balanceSpeed) {
        GameObject iterator = parent;

        boolean collided = false;

        for(int i = 0; i < iterator.getChildren().size(); i++) {
            if(iterator.getChildren().get(i) != this) {
                if(collider.intersects(iterator.getChildren().get(i).getCollider())) {
                    position.y += balanceSpeed;
                    state = State.WALKING;
                    collided = true;
                    break;
                }
            }

            if(i == iterator.getChildren().size() - 1 ) {
                if(iterator.getParent() != null) {
                    iterator = iterator.getParent();
                    i = 0;
                }
            }
        }

        return collided;
    }

}
