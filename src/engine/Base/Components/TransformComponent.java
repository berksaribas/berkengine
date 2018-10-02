package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import org.joml.Vector3f;

public class TransformComponent implements IComponent {
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public TransformComponent(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
}
