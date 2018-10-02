package game.Components;

import engine.EntityComponentSystem.Component.IComponent;

public class PlayerComponent implements IComponent {
    public enum State {
        FALLING, WALKING, JUMPING
    }

    public State state;
    public float speed;

    public PlayerComponent(State state, float speed) {
        this.state = state;
        this.speed = speed;
    }
}
