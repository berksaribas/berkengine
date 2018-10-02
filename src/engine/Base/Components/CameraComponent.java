package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.Scene.Camera.Camera;

public class CameraComponent implements IComponent {
    public Camera camera;

    public CameraComponent(Camera camera) {
        this.camera = camera;
    }
}
