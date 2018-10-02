package engine.EntityComponentSystem.Filter;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Event.IEventListener;

import java.util.HashMap;
import java.util.HashSet;

public abstract class ComponentFilter implements IEventListener {
    protected HashMap<Class<? extends IComponent>, ComponentFilter> filters;
    protected HashSet<Entity> entities;

    public ComponentFilter(HashSet<Entity> entities, HashMap<Class<? extends IComponent>, ComponentFilter> filters) {
        this.entities = entities;
        this.filters = filters;
    }

    @Override
    public abstract void onAdded(Entity entity);

    @Override
    public abstract void onRemoved(Entity entity);
}
