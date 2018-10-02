package engine.EntityComponentSystem.Filter;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;

import java.util.HashMap;
import java.util.HashSet;

public class FilterGroup {
    private HashMap<Class<? extends IComponent>, ComponentFilter> filters = new HashMap<>();
    private EntityManager entityManager;
    private HashSet<Entity> entities = new HashSet<>();

    public FilterGroup(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public HashSet<Entity> getEntities() {
        return new HashSet<>(entities);
    }

    @SafeVarargs
    public final FilterGroup collect(Class<? extends IComponent>... components) {
        for (Class<? extends IComponent> component : components) {
            ComponentExistsFilter componentExistsFilter = new ComponentExistsFilter(entities, filters);
            filters.put(component, componentExistsFilter);
            entityManager.addComponentAddedEvent(component, componentExistsFilter);
        }

        return finish();
    }

    private FilterGroup finish() {
        HashSet<Entity> newEntities = null;

        for (Class<? extends IComponent> aClass : filters.keySet()) {
            if(newEntities == null) {
                newEntities = new HashSet<>(entityManager.collectByComponent(aClass));
            }
            newEntities.retainAll(entityManager.collectByComponent(aClass));
        }

        if(newEntities != null) {
            entities.addAll(newEntities);
        }
        return this;
    }
}
