package game.Skyboxes;

import engine.Texture.Skybox;

public class ClearSkybox extends Skybox {

    static String[] fileNames = {
            "textures/skybox/brightdayrt.jpg",
            "textures/skybox/brightdaylf.jpg",
            "textures/skybox/brightdayup.jpg",
            "textures/skybox/brightdaydn.jpg",
            "textures/skybox/brightdaybk.jpg",
            "textures/skybox/brightdayft.jpg"
    };

    public ClearSkybox() {
        super(fileNames);
    }
}
