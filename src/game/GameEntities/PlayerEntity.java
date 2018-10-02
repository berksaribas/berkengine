package game.GameEntities;

import engine.Base.Components.MeshComponent;
import engine.Base.Components.PhysicsComponent;
import engine.Base.Components.TransformComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.Model.ModelController;
import game.Components.PlayerComponent;
import game.Models.CubeModel;
import org.joml.Vector3f;

public class PlayerEntity extends Entity {

    public PlayerEntity(EntityManager entityManager, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(entityManager);
        addComponent(new TransformComponent(position, rotation, scale));
        MeshComponent meshComponent = new MeshComponent(ModelController.getModelController().getByModel(CubeModel.class));
        meshComponent.active = false;
        addComponent(meshComponent);
        addComponent(new PhysicsComponent(new Vector3f(0)));
        addComponent(new PlayerComponent(PlayerComponent.State.FALLING, 1f));
    }

}
