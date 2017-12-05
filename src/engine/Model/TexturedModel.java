package engine.Model;

import engine.Texture.Texture;

public class TexturedModel{
    private Texture texture;
    private RawModel rawModel;
    private int textureRepeat;

    public TexturedModel(RawModel rawModel, String fileName) {
        this.rawModel = rawModel;
        this.texture = new Texture(fileName);
    }

    public TexturedModel() {
        rawModel = new RawModel();
    }

    public void loadTexture(String fileName) {
        texture = new Texture(fileName);
        textureRepeat = 1;
    }

    public Texture getTexture() {
        return texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public int getTextureRepeat() {
        return textureRepeat;
    }

    public void setTextureRepeat(int textureRepeat) {
        this.textureRepeat = textureRepeat;
    }
}
