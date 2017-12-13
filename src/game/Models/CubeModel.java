package game.Models;

import engine.Helper.OBJLoader;
import engine.Model.TexturedModel;

public class CubeModel extends TexturedModel {
    public CubeModel() {
        super();
        getRawModel().setRawModel(OBJLoader.loadOBJ("models/cube.obj"));
        loadTexture("textures/test.jpg");
    }
}
