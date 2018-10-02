package engine.EntityComponentSystem.Filter;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Entity.Entity;

import java.util.HashMap;
import java.util.HashSet;

public class ComponentExistsFilter extends ComponentFilter {

    public ComponentExistsFilter(HashSet<Entity> entities, HashMap<Class<? extends IComponent>, ComponentFilter> filters) {
        super(entities, filters);
    }

    @Override
    public void onAdded(Entity entity) {
        if(entity.components.containsAll(filters.keySet())) {
            entities.add(entity);
        }
    }

    @Override
    public void onRemoved(Entity entity) {
        entities.remove(entity);
    }
}