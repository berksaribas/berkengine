package engine.UI;

import engine.Shader.ShaderProgram;
import engine.Texture.Texture;

public class Quad {

    private float width, height;
    private Texture texture;
    private ShaderProgram shaderProgram;

    public Quad(float width, float height, Texture texture, ShaderProgram shaderProgram) {
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.shaderProgram = shaderProgram;
    }

    public Texture getTexture() {
        return texture;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }
}
