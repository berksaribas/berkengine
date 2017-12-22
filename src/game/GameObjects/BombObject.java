package game.GameObjects;

import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.Object.PhysicsComponent;
import org.joml.Vector3f;

public class BombObject extends GameObject {
    enum State{
        SET, EXPLODED
    }

    State state;

    public BombObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(parent, model, position, rotation, scale);
        components.put(PhysicsComponent.class, new PhysicsComponent(this));
        getByComponent(PhysicsComponent.class).disableCollisionWith(PlayerObject.class);
    }

    public void throwBomb(Vector3f direction) {
        state = State.EXPLODED;
        Vector3f momentum = new Vector3f(direction);
        momentum.div(100f, 40f, 100f);
        getByComponent(PhysicsComponent.class).addMomentum(momentum);
    }

}
