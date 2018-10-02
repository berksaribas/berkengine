package game.Systems;

import engine.Base.Components.CameraComponent;
import engine.Base.Components.PhysicsComponent;
import engine.Base.Components.TransformComponent;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.Input.KeyboardHandler;
import engine.Scene.Camera.FollowerCamera;
import engine.Scene.Scene;
import game.Components.PlayerComponent;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerSystem extends ExecuteSystem {
    private FilterGroup filterGroup;

    public PlayerSystem(EntityManager entityManager) {
        super(entityManager);
        filterGroup = new FilterGroup(entityManager).collect(PlayerComponent.class);
    }

    @Override
    protected void execute() {
        CameraComponent cameraComponent = entityManager.getSingleEntity(CameraComponent.class).getComponent(CameraComponent.class);
        ((FollowerCamera) cameraComponent.camera).handleFPSMovement(Scene.deltaTime);

        for (Entity entity : filterGroup.getEntities()) {
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
            PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
            PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);

            playerComponent.speed = 0.4f;
            if (physicsComponent.force.y < 0f) {
                playerComponent.state = PlayerComponent.State.FALLING;
            } else if (physicsComponent.force.y > 0f) {
                playerComponent.state = PlayerComponent.State.JUMPING;
            } else {
                playerComponent.state = PlayerComponent.State.WALKING;
                playerComponent.speed = 1.5f;
            }

            Vector3f force = physicsComponent.force;

            float zFactor = 0, xFactor = 0;

            if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
                xFactor += -(float) Math.sin(Math.PI / 2 + -transformComponent.rotation.y) * playerComponent.speed;
                zFactor += (float) Math.cos(Math.PI / 2 + -transformComponent.rotation.y) * playerComponent.speed;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
                xFactor += -(float) Math.sin(-Math.PI / 2 + -transformComponent.rotation.y) * playerComponent.speed;
                zFactor += (float) Math.cos(-Math.PI / 2 + -transformComponent.rotation.y) * playerComponent.speed;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
                xFactor += (float) Math.sin(-transformComponent.rotation.y) * playerComponent.speed;
                zFactor += -(float) Math.cos(-transformComponent.rotation.y) * playerComponent.speed;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
                xFactor += -(float) Math.sin(-transformComponent.rotation.y) * playerComponent.speed;
                zFactor += (float) Math.cos(-transformComponent.rotation.y) * playerComponent.speed;
            }

            if ((force.x >= 0 && xFactor > force.x) || (force.x <= 0 && xFactor < force.x)) {
                force.x = xFactor;
            } else if ((force.x > 0 && xFactor < 0) || (force.x < 0 && xFactor > 0)) {
                force.x += xFactor * 0.3f;
            }

            if ((force.z >= 0 && zFactor > force.z) || (force.z <= 0 && zFactor < force.z)) {
                force.z = zFactor;
            } else if ((force.z > 0 && zFactor < 0) || (force.z < 0 && zFactor > 0)) {
                force.z += zFactor * 0.3f;
            }

            if (KeyboardHandler.isKeyDown(GLFW_KEY_SPACE) && playerComponent.state == PlayerComponent.State.WALKING) {
                physicsComponent.force.x += 0;
                physicsComponent.force.y += 2.5f;
                physicsComponent.force.z += 0;
            }
        }
    }
}
