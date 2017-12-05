package engine.Scene.Lightning;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Light {
    Vector4f position;
    Vector3f color;

    public Light(Vector3f position, Vector3f color, int type) {
        this.position = new Vector4f(position.x, position.y, position.z, type);
        this.color = color;
    }

    public Vector4f getPosition() {
        return position;
    }

    public void setPosition(Vector4f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
