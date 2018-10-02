package engine.EntityComponentSystem.System;

import engine.EntityComponentSystem.Entity.EntityManager;

public abstract class BaseSystem {
    protected EntityManager entityManager;

    public BaseSystem(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public abstract void update();
}
