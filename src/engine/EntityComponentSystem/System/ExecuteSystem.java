package engine.EntityComponentSystem.System;

import engine.EntityComponentSystem.Entity.EntityManager;

public abstract class ExecuteSystem extends BaseSystem {
    public ExecuteSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void update() {
        long time = System.currentTimeMillis();
        execute();
        System.out.println(getClass() + " execution took: " + (System.currentTimeMillis() - time) + " miliseconds");
    }

    protected abstract void execute();
}
