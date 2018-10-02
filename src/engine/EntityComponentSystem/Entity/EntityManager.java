package engine.EntityComponentSystem.Entity;

import engine.EntityComponentSystem.Component.IComponent;
import engine.EntityComponentSystem.Event.IEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EntityManager {
    private HashMap<Class<? extends IComponent>, HashMap<Entity, IComponent>> components;
    private HashMap<Class<? extends IComponent>, ArrayList<IEventListener>> componentsAdded;
    private HashMap<Integer, Entity> entities;

    private int lastId = 0;

    public EntityManager() {
        components = new HashMap<>();
        componentsAdded = new HashMap<>();
        entities = new HashMap<>();
    }

    public Entity createEntity() {
        Entity e = new Entity(this);
        entities.put(lastId, e);
        lastId++;

        return e;
    }

    public void saveEntity(Entity e) {
        entities.put(lastId, e);
        lastId++;
    }

    public void destroyEntity(Entity e) {
        entities.remove(e);
        for (Class<? extends IComponent> component : e.components) {
            components.get(component).remove(e);

            ArrayList<IEventListener> componentAdded = componentsAdded.get(component);
            if (componentAdded != null) {
                for (IEventListener event : componentAdded) {
                    event.onRemoved(e);
                }
            }

        }
        e = null;
    }

    public HashSet<Entity> collectByComponent(Class<? extends IComponent> component) {
        if (components.get(component) == null) {
            return new HashSet<>();
        }
        return new HashSet<>(components.get(component).keySet());
    }

    public void addComponentAddedEvent(Class<? extends IComponent> component, IEventListener e) {
        if (componentsAdded.get(component) == null) {
            componentsAdded.put(component, new ArrayList<>());
        }

        componentsAdded.get(component).add(e);
    }

    public void removeComponentAddedEvent(Class<? extends IComponent> component, IEventListener e) {
        componentsAdded.get(component).remove(e);
    }

    public Entity getSingleEntity(Class<? extends IComponent> component) {
        int size = components.get(component).size();

        if (size > 1) {
            throw new RuntimeException("There are more than 1 entity with this component!");
        } else if (size < 1) {
            throw new RuntimeException("There is no entities with this component!");
        }

        return components.get(component).keySet().iterator().next();
    }

    public <T extends IComponent> T getComponent(Entity entity, Class<T> cl) {
        return cl.cast(components.get(cl).get(entity));
    }

    public void addComponent(Entity entity, IComponent component) {
        if (components.get(component.getClass()) == null) {
            components.put(component.getClass(), new HashMap<>());
        }

        components.get(component.getClass()).put(entity, component);
        entity.components.add(component.getClass());

        ArrayList<IEventListener> componentAdded = componentsAdded.get(component.getClass());
        if (componentAdded != null) {
            for (IEventListener e : componentAdded) {
                e.onAdded(entity);
            }
        }
    }

    public void removeComponent(Entity entity, Class<? extends IComponent> cl) {
        components.get(cl).remove(entity);

        entity.components.remove(cl);

        ArrayList<IEventListener> componentAdded = componentsAdded.get(cl);
        if (componentAdded != null) {
            for (IEventListener e : componentAdded) {
                e.onRemoved(entity);
            }
        }
    }
}
