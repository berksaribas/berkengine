package engine.UI;

import engine.Shader.QuadShader;
import engine.Texture.Texture;
import org.joml.Vector3f;

public class Quad {

    private Texture texture;
    private QuadShader shaderProgram;
    protected Vector3f position, rotation, scale;

    public Quad(Vector3f position, Vector3f scale, Texture texture, QuadShader shaderProgram) {
        this.texture = texture;
        this.shaderProgram = shaderProgram;
        this.position = position;
        this.rotation = new Vector3f(0);
        this.scale = scale;
    }

    public Texture getTexture() {
        return texture;
    }

    public QuadShader getShaderProgram() {
        return shaderProgram;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }
}
