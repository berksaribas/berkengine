package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.Scene.Lightning.Light;

public class LightComponent implements IComponent {
    public Light light;

    public LightComponent(Light light) {
        this.light = light;
    }
}
