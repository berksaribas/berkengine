package engine.EntityComponentSystem.Event;

import engine.EntityComponentSystem.Entity.Entity;

import java.util.HashSet;

public class EventCollection {
    private IEventListener eventListener;
    private HashSet<Entity> entities;

    public EventCollection(IEventListener eventListener, HashSet<Entity> entities) {
        this.eventListener = eventListener;
        this.entities = entities;
    }

    public IEventListener getEventListener() {
        return eventListener;
    }

    public HashSet<Entity> getEntities() {
        return entities;
    }
}
