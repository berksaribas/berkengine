package game.GameObjects;

import engine.Input.KeyboardHandler;
import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.Object.PhysicsComponent;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class PlayerObject extends GameObject {
    float speed = 60, deltaY, maxJump = 0;
    State state;
    Vector3f initialPosition;

    enum State{
        FALLING, WALKING, JUMPING
    }

    public PlayerObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(parent, model, position, rotation, scale);
        state = State.FALLING;
        initialPosition = new Vector3f(position);
        components.put(PhysicsComponent.class, new PhysicsComponent(this));
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(position.y < -2f) {
            position.set(initialPosition);
        }

        if(getByComponent(PhysicsComponent.class).getMomentum().y < 0f) {
            state = State.FALLING;
        } else if(getByComponent(PhysicsComponent.class).getMomentum().y > 0f) {
            state = State.JUMPING;
        } else {
            state = State.WALKING;
        }

        System.out.println(state);

        Vector3f momentum = getByComponent(PhysicsComponent.class).getMomentum();

        momentum.x = 0;
        momentum.z = 0;

        if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
            getByComponent(PhysicsComponent.class).setMomentum(
                    new Vector3f(momentum.x + -0.02f * (float) Math.sin(Math.PI / 2 + -rotation.y) * delta * speed,
                    momentum.y,
                    momentum.z +0.02f * (float) Math.cos(Math.PI / 2 + -rotation.y) * delta * speed));
        }

        if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {

            getByComponent(PhysicsComponent.class).setMomentum(
                    new Vector3f(momentum.x + -0.02f * (float) Math.sin(-Math.PI / 2 + -rotation.y) * delta * speed,
                    momentum.y,
                    momentum.z + 0.02f * (float) Math.cos(-Math.PI / 2 + -rotation.y) * delta * speed));
        }

        if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
            getByComponent(PhysicsComponent.class).setMomentum(
                    new Vector3f(momentum.x + 0.02f * (float) Math.sin(-rotation.y) * delta * speed,
                    momentum.y,
                    momentum.z + -0.02f * (float) Math.cos(-rotation.y) * delta * speed));
        }

        if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
            getByComponent(PhysicsComponent.class).setMomentum(
                    new Vector3f(momentum.x + -0.02f * (float) Math.sin(-rotation.y) * delta * speed,
                    momentum.y,
                    momentum.z + 0.02f * (float) Math.cos(-rotation.y) * delta * speed));
        }

        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE) && state == State.WALKING) {
            getByComponent(PhysicsComponent.class).addMomentum(new Vector3f(0,2f * delta,0));
        }

    }

}
