package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.Model.TexturedModel;

public class MeshComponent implements IComponent {
    public TexturedModel texturedModel;
    public boolean active = true;

    public MeshComponent(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }
}
