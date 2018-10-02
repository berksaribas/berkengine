package game.Scenes;

import engine.EntityComponentSystem.System.BaseSystem;
import engine.Scene.Scene;
import game.Systems.GameInitSystem;
import game.Systems.PlayerSystem;

import java.util.ArrayList;

public class GameScene extends Scene {
    public GameScene(int width, int height) {
        super(width, height);
    }

    @Override
    public void registerSystems(ArrayList<BaseSystem> systems) {
        systems.add(new GameInitSystem(getEntityManager()));
        systems.add(new PlayerSystem(getEntityManager()));
    }
}
