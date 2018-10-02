package game.GameEntities;

import engine.Base.Components.MeshComponent;
import engine.Base.Components.TransformComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.Model.ModelController;
import game.Models.CubeModel;
import org.joml.Vector3f;

public class CubeEntity extends Entity {

    public CubeEntity(EntityManager entityManager, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(entityManager);
        addComponent(new TransformComponent(position, rotation, scale));
        addComponent(new MeshComponent(ModelController.getModelController().getByModel(CubeModel.class)));
    }
}
