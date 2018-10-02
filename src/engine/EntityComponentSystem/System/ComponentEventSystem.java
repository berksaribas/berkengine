package engine.EntityComponentSystem.System;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Event.EventCollection;
import engine.EntityComponentSystem.Filter.FilterEventGroup;

import java.util.HashMap;
import java.util.HashSet;

public abstract class ComponentEventSystem extends BaseSystem {
    private HashMap<Class<? extends IComponent>, EventCollection> listeners = new HashMap<>();
    private FilterEventGroup filterGroup;

    public ComponentEventSystem(EntityManager entityManager) {
        super(entityManager);
        this.filterGroup = filter(entityManager);
    }

    protected abstract FilterEventGroup filter(EntityManager entityManager);

    public void update() {
        long time = System.currentTimeMillis();
        execute(filterGroup.getEntities());
        System.out.println(getClass() + " execution took: " + (System.currentTimeMillis() - time) + " miliseconds");
    }

    protected abstract void execute(HashSet<Entity> entities);
}
