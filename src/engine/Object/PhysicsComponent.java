package engine.Object;

import org.joml.Vector3f;

public class PhysicsComponent extends Component {

    Vector3f momentum = new Vector3f(0, 0, 0);
    GameObject gameObject;

    public PhysicsComponent(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void act(float delta) {
        addMomentum(new Vector3f(0, -0.1f * delta, 0));

        gameObject.getPosition().y += momentum.y;
        checkCollisions(1, momentum);

        gameObject.getPosition().x += momentum.x;
        checkCollisions(0, momentum);

        gameObject.getPosition().z += momentum.z;
        checkCollisions(2, momentum);

    }

    public void addMomentum(Vector3f momentum) {
        this.momentum.add(momentum);
    }

    public void setMomentum(Vector3f momentum) {
        this.momentum.set(momentum);
    }

    public boolean checkCollisions(int cord, Vector3f momentum) {
        GameObject iterator = gameObject.getParent();

        boolean collided = false;

        for(int i = 0; i < iterator.getChildren().size(); i++) {
            if(iterator.getChildren().get(i) != gameObject) {
                if(gameObject.getCollider().intersects(iterator.getChildren().get(i).getCollider())) {
                    if(cord == 0) {
                        gameObject.getPosition().x += -momentum.x;
                        momentum.x = 0;
                    } else if(cord == 1) {
                        gameObject.getPosition().y += -momentum.y;
                        momentum.y = 0;
                    } else if(cord == 2) {
                        gameObject.getPosition().z += -momentum.z;
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
