package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.Scene.Lightning.Shadow;

public class ShadowComponent implements IComponent {
    public Shadow shadow;

    public ShadowComponent(Shadow shadow) {
        this.shadow = shadow;
    }
}
