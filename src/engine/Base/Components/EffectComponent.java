package engine.Base.Components;

import engine.EntityComponentSystem.Component.IComponent;
import engine.PostFX.Effect;
import engine.Texture.Texture;

public class EffectComponent implements IComponent {
    public Effect effect;
    public Texture texture;

    public EffectComponent(Effect effect, Texture texture) {
        this.effect = effect;
        this.texture = texture;
    }
}
