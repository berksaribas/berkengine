package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.Texture.Skybox;

public class SkyboxComponent implements IComponent {
    public Skybox skybox;

    public SkyboxComponent(Skybox skybox) {
        this.skybox = skybox;
    }
}
