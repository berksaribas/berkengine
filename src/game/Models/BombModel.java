package game.Models;

import engine.Helper.OBJLoader;
import engine.Model.TexturedModel;

public class BombModel extends TexturedModel {
    public BombModel() {
        super();
        getRawModel().setRawModel(OBJLoader.loadOBJ("models/cube.obj"));
        loadTexture("textures/bomb.png");
    }
}
