package engine.Base.Systems;

import engine.Base.Components.DestroyComponent;
import engine.Base.Components.EffectComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.FrameBuffer.FBO;
import org.lwjgl.opengl.GL30;

public class EffectSystem extends ExecuteSystem {
    private FBO postProcessor = new FBO();
    private FilterGroup filterGroup;

    public EffectSystem(EntityManager entityManager) {
        super(entityManager);
        filterGroup = new FilterGroup(entityManager).collect(EffectComponent.class);
    }

    @Override
    protected void execute() {
        for (Entity entity : filterGroup.getEntities()) {
            EffectComponent effectComponent = entity.getComponent(EffectComponent.class);

            postProcessor.attachTexture(effectComponent.effect.getResult().getId(), GL30.GL_COLOR_ATTACHMENT0);
            effectComponent.effect.apply(effectComponent.texture, postProcessor);
            entity.addComponent(new DestroyComponent());
        }
    }
}
