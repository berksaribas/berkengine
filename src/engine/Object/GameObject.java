package engine.Object;

import engine.Model.TexturedModel;
import engine.Physics.AABB;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObject {
    protected TexturedModel model;
    protected Vector3f position, rotation, scale;
    protected ArrayList<GameObject> children;
    protected GameObject parent;
    protected AABB collider;
    protected int textureRepeat;
    protected HashMap<Class, Component> components;

    public GameObject(GameObject parent, TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale){
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.children = new ArrayList<>();
        this.parent = parent;
        this.collider = new AABB(position, new Vector3f(scale.x, scale.y, scale.z));
        if(parent != null) {
            parent.children.add(this);
        }
        textureRepeat = 1;
        components = new HashMap<>();
    }

    public <T extends Component> T getByComponent(Class<T> cl) {
        return cl.cast(components.get(cl));
    }

    public ArrayList<GameObject> getChildren() {
        return children;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public GameObject getParent() {
        return parent;
    }

    public void update(float delta) {
        for(Class entity : components.keySet()) {
            components.get(entity).act(delta);
        }
    }

    public AABB getCollider() {
        return collider;
    }

    public int getTextureRepeat() {
        return textureRepeat;
    }

    public void setTextureRepeat(int textureRepeat) {
        this.textureRepeat = textureRepeat;
    }
}
