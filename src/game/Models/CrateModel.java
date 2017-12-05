package game.Models;

import engine.Helper.OBJLoader;
import engine.Model.TexturedModel;

public class CrateModel extends TexturedModel {
    public CrateModel() {
        super();
        getRawModel().setRawModel(OBJLoader.loadOBJ("models/barrel.obj"));
        loadTexture("textures/barrel.jpg");
    }
}
