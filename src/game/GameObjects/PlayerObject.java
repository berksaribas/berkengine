package game.GameObjects;

import engine.Input.KeyboardHandler;
import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.Object.PhysicsComponent;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerObject extends GameObject {
    float speed = 1f;
    State state;
    Vector3f initialPosition;
    int score = 0;
    int currentPlatform = 0;
    OnReset onReset;

    enum State{
        FALLING, WALKING, JUMPING
    }

    public PlayerObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(parent, model, position, rotation, scale);
        state = State.FALLING;
        initialPosition = new Vector3f(position);
        components.put(PhysicsComponent.class, new PhysicsComponent(this));
        getByComponent(PhysicsComponent.class).disableCollisionWith(BombObject.class);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(position.y < -3f) {
            position.set(initialPosition);
            getByComponent(PhysicsComponent.class).setMomentum(new Vector3f(0));
            score = 0;
            currentPlatform = 0;
            if(onReset != null) {
                onReset.reset();
            }
        }

        speed = 0.4f;
        if(getByComponent(PhysicsComponent.class).getMomentum().y < 0f) {
            state = State.FALLING;
        } else if(getByComponent(PhysicsComponent.class).getMomentum().y > 0f) {
            state = State.JUMPING;
        } else {
            state = State.WALKING;
            speed = 1.5f;
        }

        Vector3f momentum = getByComponent(PhysicsComponent.class).getMomentum();

        float zFactor = 0, xFactor = 0;

        if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
            xFactor += -(float) Math.sin(Math.PI / 2 + -rotation.y) * speed;
            zFactor += (float) Math.cos(Math.PI / 2 + -rotation.y) * speed;
        }

        if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
            xFactor += -(float) Math.sin(-Math.PI / 2 + -rotation.y) * speed;
            zFactor += (float) Math.cos(-Math.PI / 2 + -rotation.y) * speed;
        }

        if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
            xFactor += (float) Math.sin(-rotation.y) * speed;
            zFactor += -(float) Math.cos(-rotation.y) * speed;
        }

        if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
            xFactor += -(float) Math.sin(-rotation.y) * speed;
            zFactor += (float) Math.cos(-rotation.y) * speed;
        }

        if((momentum.x >= 0 && xFactor > momentum.x ) || (momentum.x <= 0 && xFactor < momentum.x)) {
            momentum.x = xFactor;
        } else if((momentum.x > 0 && xFactor < 0) || (momentum.x < 0 && xFactor > 0)) {
            momentum.x += xFactor * 0.3f;
        }

        if((momentum.z >= 0 && zFactor > momentum.z ) || (momentum.z <= 0 && zFactor < momentum.z)) {
            momentum.z = zFactor;
        } else if((momentum.z > 0 && zFactor < 0) || (momentum.z < 0 && zFactor > 0)) {
            momentum.z += zFactor * 0.3f;
        }

        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE) && state == State.WALKING) {
            getByComponent(PhysicsComponent.class).addMomentum(new Vector3f(0,2.5f,0));
        }

    }

    public void setOnReset(OnReset onReset) {
        this.onReset = onReset;
    }

    public int getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(int currentPlatform) {
        this.currentPlatform = currentPlatform;
    }

    public void incrementScore() {
        score++;
        System.out.println("Score : " + score);
    }
}
