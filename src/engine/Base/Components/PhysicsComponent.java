package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import org.joml.Vector3f;

public class PhysicsComponent implements IComponent {
    public Vector3f force;

    public PhysicsComponent(Vector3f force) {
        this.force = force;
    }
}
