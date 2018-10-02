package engine.Base.Systems;

import engine.Base.Components.DestroyComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterEventGroup;
import engine.EntityComponentSystem.System.ComponentEventSystem;

import java.util.ArrayList;
import java.util.HashSet;

public class DestroySystem extends ComponentEventSystem {
    public DestroySystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected FilterEventGroup filter(EntityManager entityManager) {
        return new FilterEventGroup(entityManager).observeAdded(DestroyComponent.class);
    }

    @Override
    protected void execute(HashSet<Entity> entities) {
        ArrayList<Entity> entitiesList = new ArrayList<>(entities);

        for (int i = 0; i < entitiesList.size(); i++) {
            entitiesList.get(i).destroy();
        }
    }
}
