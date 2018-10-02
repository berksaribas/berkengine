package engine.Model;

import engine.Texture.Texture;

import java.util.ArrayList;

public class TexturedModel {
    private ArrayList<Texture> textures;
    private RawModel rawModel;

    public TexturedModel(RawModel rawModel) {
        this.rawModel = rawModel;
        this.textures = new ArrayList<>();
    }

    public TexturedModel() {
        rawModel = new RawModel();
        this.textures = new ArrayList<>();
    }

    public void loadTexture(String fileName) {
        textures.add(new Texture(fileName));
    }

    public ArrayList<Texture> getTextures() {
        return textures;
    }

    public RawModel getRawModel() {
        return rawModel;
    }
}
