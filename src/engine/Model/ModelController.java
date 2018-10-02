package engine.Model;

import java.util.HashMap;

public class ModelController {
    private static ModelController modelController;
    HashMap<Class, TexturedModel> models;

    private ModelController() {
        models = new HashMap<>();
    }

    public static ModelController getModelController() {
        if (modelController == null) {
            modelController = new ModelController();
        }
        return modelController;
    }

    public void addModel(Class cl, TexturedModel texturedModel) {
        models.put(cl, texturedModel);
    }

    public <T extends TexturedModel> T getByModel(Class<T> cl) {
        return cl.cast(models.get(cl));
    }
}
