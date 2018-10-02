package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.UI.Quad;

public class QuadComponent implements IComponent {
    public Quad quad;

    public QuadComponent(Quad quad) {
        this.quad = quad;
    }
}
