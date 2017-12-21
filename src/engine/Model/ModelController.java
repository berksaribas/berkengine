package engine.Model;

import java.util.HashMap;

public class ModelController {
    HashMap<Class, TexturedModel> models;

    public ModelController() {
        models = new HashMap<>();
    }

    public void addModel(Class cl, TexturedModel texturedModel) {
        models.put(cl, texturedModel);
    }

    public <T extends TexturedModel> T getByModel(Class<T> cl) {
        return cl.cast(models.get(cl));
    }
}
