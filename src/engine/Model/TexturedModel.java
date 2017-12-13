package engine.Model;

import engine.Texture.Texture;

public class TexturedModel{
    private Texture texture;
    private RawModel rawModel;

    public TexturedModel(RawModel rawModel, String fileName) {
        this.rawModel = rawModel;
        this.texture = new Texture(fileName);
    }

    public TexturedModel() {
        rawModel = new RawModel();
    }

    public void loadTexture(String fileName) {
        texture = new Texture(fileName);
    }

    public Texture getTexture() {
        return texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }
}
