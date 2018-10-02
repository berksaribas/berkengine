package engine.Base.Systems;

import engine.Base.Components.CameraComponent;
import engine.Base.Utils.ShaderUtils;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.Shader.ObjectShader;

public class CameraSystem extends ExecuteSystem {
    private FilterGroup filterGroup;

    public CameraSystem(EntityManager entityManager) {
        super(entityManager);
        filterGroup = new FilterGroup(entityManager).collect(CameraComponent.class);
    }

    @Override
    protected void execute() {
        for (Entity entity : filterGroup.getEntities()) {
            CameraComponent cameraComponent = entity.getComponent(CameraComponent.class);
            cameraComponent.camera.update(ShaderUtils.getInstance().retrieveShader(ObjectShader.class));
        }
    }
}
