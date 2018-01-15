package game.GameObjects;

import engine.Model.TexturedModel;
import engine.Object.GameObject;
import org.joml.Vector3f;

public class CubeObject extends GameObject{

    private boolean rising = false;
    private float rised = 0;

    public CubeObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(parent, model, position, rotation, scale);
        textureRepeat = (int)(scale.x / 0.1f);
        textureRepeat = (int)(scale.y / 0.1f) > textureRepeat ? (int)(scale.y / 0.1f) : textureRepeat;
        textureRepeat = (int)(scale.z / 0.1f) > textureRepeat ? (int)(scale.z / 0.1f) : textureRepeat;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(rising) {
            rise(delta);
        }
    }

    public boolean isRising() {
        return rising;
    }

    public void setRising(boolean rising) {
        this.rising = rising;
        rised = 0f;
    }

    public void rise(float delta) {
        position.y += 2f * delta;
        rised += 2f * delta;

        position.x += 1f * delta;

        if(rised >= 8) {
            setRising(false);
        }
    }
}
