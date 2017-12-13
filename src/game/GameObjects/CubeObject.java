package game.GameObjects;

import engine.Model.TexturedModel;
import engine.Object.GameObject;
import org.joml.Vector3f;

public class CubeObject extends GameObject{

    public CubeObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        super(parent, model, position, rotation, scale);
        textureRepeat = (int)(scale.x / 0.1f);
        textureRepeat = (int)(scale.y / 0.1f) > textureRepeat ? (int)(scale.y / 0.1f) : textureRepeat;
        textureRepeat = (int)(scale.z / 0.1f) > textureRepeat ? (int)(scale.z / 0.1f) : textureRepeat;
        System.out.println(textureRepeat);
    }
}
