package game;

import engine.DisplayManager;
import game.Scenes.GameScene;

public class  Main {

    public static int WIDTH = 1600, HEIGHT = 900;

    public static void main(String[] args) {

        DisplayManager displayManager = new DisplayManager("BerkEngine", WIDTH, HEIGHT);
        displayManager.init();

        GameScene scene = new GameScene(WIDTH, HEIGHT);

        while(!displayManager.closeRequested()) {
            if(displayManager.getDelta() > 0) {
                scene.loop(displayManager.getDelta());
            }
            displayManager.update();
        }

        displayManager.closeDisplay();
    }
}
