package engine.EntityComponentSystem.Event;

import engine.EntityComponentSystem.Entity.Entity;

public interface IEventListener {
    public void onAdded(Entity entity);

    public void onRemoved(Entity entity);
}
