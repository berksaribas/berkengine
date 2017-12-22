package engine.UI;

import engine.Shader.ShaderProgram;
import engine.Texture.Texture;

public class Quad {

    private float width, height;
    private Texture texture;
    private ShaderProgram shaderProgram;

    public Quad() {

    }

    public Texture getTexture() {
        return texture;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }
}
