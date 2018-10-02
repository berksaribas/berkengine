package engine.EntityComponentSystem.Filter;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;

import java.util.HashMap;
import java.util.HashSet;

public class FilterEventGroup {
    private HashMap<Class<? extends IComponent>, ComponentFilter> filters = new HashMap<>();
    private EntityManager entityManager;
    private HashSet<Entity> entities = new HashSet<>();

    public FilterEventGroup(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public HashSet<Entity> getEntities() {
        HashSet<Entity> returnEntities = null;

        for (Class<? extends IComponent> aClass : filters.keySet()) {
            if (returnEntities == null) {
                returnEntities = new HashSet<>(((ComponentAddedFilter) filters.get(aClass)).onAddedEntities);
            }
            returnEntities.retainAll(((ComponentAddedFilter) filters.get(aClass)).onAddedEntities);
            ((ComponentAddedFilter) filters.get(aClass)).onAddedEntities.clear();
        }

        return returnEntities;
    }

    public FilterEventGroup observeAdded(Class<? extends IComponent> component) {
        ComponentAddedFilter componentAddedFilter = new ComponentAddedFilter(entities, filters);
        filters.put(component, componentAddedFilter);
        entityManager.addComponentAddedEvent(component, componentAddedFilter);

        return finish();
    }

    private FilterEventGroup finish() {
        return this;
    }
}
