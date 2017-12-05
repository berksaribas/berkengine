package game.Skyboxes;

import engine.Texture.Skybox;

public class ClearSkybox extends Skybox {

    static String[] fileNames= {
            "textures/skybox/right.jpg",
            "textures/skybox/left.jpg",
            "textures/skybox/top.jpg",
            "textures/skybox/bottom.jpg",
            "textures/skybox/back.jpg",
            "textures/skybox/front.jpg"
    };

    public ClearSkybox() {
        super(fileNames);
    }
}
