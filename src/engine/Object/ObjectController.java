package engine.Object;

import engine.Model.TexturedModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectController {
    private HashMap<TexturedModel, ArrayList<GameObject>> objects;

    public ObjectController() {
        objects = new HashMap<>();
    }

    public void addObject(GameObject object) {
        if(objects.get(object.getModel()) == null ) {
            objects.put(object.getModel(), new ArrayList<>());
        }

        objects.get(object.getModel()).add(object);
    }

    public void removeObject(GameObject object) {
        object.parent.children.remove(object);
        objects.get(object.getModel()).remove(object);
    }

    public HashMap<TexturedModel, ArrayList<GameObject>> getObjects() {
        return objects;
    }
}
