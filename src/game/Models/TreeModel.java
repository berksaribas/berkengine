package game.Models;

import engine.Helper.OBJLoader;
import engine.Model.TexturedModel;

public class TreeModel extends TexturedModel {
    public TreeModel() {
        super();
        getRawModel().setRawModel(OBJLoader.loadOBJ("models/box.obj"));
        loadTexture("textures/box.jpg");
    }
}
