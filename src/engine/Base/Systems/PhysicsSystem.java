package engine.Base.Systems;

import engine.Base.Components.PhysicsComponent;
import engine.Base.Components.TransformComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.Scene.Scene;
import org.joml.Vector3f;

public class PhysicsSystem extends ExecuteSystem {
    private FilterGroup filterGroup;

    public PhysicsSystem(EntityManager entityManager) {
        super(entityManager);
        filterGroup = new FilterGroup(entityManager).collect(PhysicsComponent.class);
    }

    @Override
    protected void execute() {
        for (Entity entity : filterGroup.getEntities()) {
            PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);
            TransformComponent transform = entity.getComponent(TransformComponent.class);

            transform.position.x += physics.force.x * Scene.deltaTime;
            transform.position.y += physics.force.y * Scene.deltaTime;
            transform.position.z += physics.force.z * Scene.deltaTime;
            physics.force = new Vector3f(0);
        }
    }
}
