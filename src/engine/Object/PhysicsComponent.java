package engine.Object;

import engine.Physics.OnCollideWith;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;

public class PhysicsComponent extends Component {

    Vector3f momentum = new Vector3f(0, 0, 0);
    GameObject gameObject;
    HashSet<Class> disabledCollisions;
    HashMap<GameObject, OnCollideWith> onCollideWith;

    public PhysicsComponent(GameObject gameObject) {
        this.gameObject = gameObject;
        disabledCollisions = new HashSet<>();
        onCollideWith = new HashMap<>();
    }

    @Override
    public void act(float delta) {
        addMomentum(new Vector3f(0, -7f * delta, 0));

        gameObject.getPosition().y += momentum.y * delta;
        checkCollisions(1, momentum, delta);

        gameObject.getPosition().x += momentum.x  * delta;
        checkCollisions(0, momentum, delta);

        gameObject.getPosition().z += momentum.z  * delta;
        checkCollisions(2, momentum, delta);

        if(momentum.y == 0) {
            momentum.x -= momentum.x;
            momentum.z -= momentum.z;
        }

    }

    public void disableCollisionWith(Class disabled) {
        disabledCollisions.add(disabled);
    }

    public float getVelocity() {
        return (float)Math.sqrt(momentum.x * momentum.x + momentum.z * momentum.z);
    }

    public void addMomentum(Vector3f momentum) {
        this.momentum.add(momentum);
    }

    public void setMomentum(Vector3f momentum) {
        this.momentum.set(momentum);
    }

    public void setOnCollideWith(GameObject object, OnCollideWith onCollision) {
        onCollideWith.put(object, onCollision);
    }

    public boolean checkCollisions(int cord, Vector3f momentum, float delta) {
        GameObject iterator = gameObject.getParent();

        boolean collided = false;

        for(int i = 0; i < iterator.getChildren().size(); i++) {
            if(disabledCollisions.contains(iterator.getChildren().get(i).getClass())){
                collided = false;
            } else if(iterator.getChildren().get(i) != gameObject) {
                if(gameObject.getCollider().intersects(iterator.getChildren().get(i).getCollider())) {
                    if(cord == 0) {
                        gameObject.getPosition().x += -momentum.x * delta;
                        momentum.x = 0;
                    } else if(cord == 1) {
                        gameObject.getPosition().y += -momentum.y * delta;
                        momentum.y = 0;

                        if(onCollideWith.get(iterator.getChildren().get(i)) != null) {
                            onCollideWith.get(iterator.getChildren().get(i)).onCollide();
                        }

                    } else if(cord == 2) {
                        gameObject.getPosition().z += -momentum.z * delta;
                        momentum.z = 0;
                    }

                    collided = true;
                    break;
                }
            }

            if(i == iterator.getChildren().size() - 1 ) {
                if(iterator.getParent() != null) {
                    iterator = iterator.getParent();
                    i = 0;
                }
            }
        }

        return collided;
    }

    public Vector3f getMomentum() {
        return momentum;
    }
}
