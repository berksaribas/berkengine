package engine.EntityComponentSystem.Entity;

import engine.EntityComponentSystem.Component.IComponent;

import java.util.HashSet;

public class Entity {
    private EntityManager entityManager;
    public HashSet<Class<? extends IComponent>> components;

    public Entity(EntityManager entityManager) {
        this.entityManager = entityManager;
        entityManager.saveEntity(this);
        components = new HashSet<>();
    }

    public void addComponent(IComponent component) {
        entityManager.addComponent(this, component);
    }

    public void removeComponent(Class<? extends IComponent> cl) {
        entityManager.removeComponent(this, cl);
    }

    public <T extends IComponent> T getComponent(Class<T> cl) {
        return entityManager.getComponent(this, cl);
    }

    public void destroy() {
        entityManager.destroyEntity(this);
    }
}
