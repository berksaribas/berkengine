package game.Models;

import engine.Helper.OBJLoader;
import engine.Model.ModelLoader;
import engine.Model.TexturedModel;

public class TerrainModel  extends TexturedModel {
    public TerrainModel() {
        super();
        getRawModel().setRawModel(OBJLoader.loadOBJ("models/cube.obj"));
        loadTexture("textures/grass.jpg");
        setTextureRepeat(80);
    }
}
