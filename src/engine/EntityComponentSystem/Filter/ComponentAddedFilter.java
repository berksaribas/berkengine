package engine.EntityComponentSystem.Filter;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Entity.Entity;

import java.util.HashMap;
import java.util.HashSet;

public class ComponentAddedFilter extends ComponentFilter {
    HashSet<Entity> onAddedEntities = new HashSet<>();

    public ComponentAddedFilter(HashSet<Entity> entities, HashMap<Class<? extends IComponent>, ComponentFilter> filters) {
        super(entities, filters);
    }

    @Override
    public void onAdded(Entity entity) {
        onAddedEntities.add(entity);
    }

    @Override
    public void onRemoved(Entity entity) {
        onAddedEntities.remove(entity);
    }
}
