package engine.Base.Utils;

import engine.Shader.ShaderProgram;

import java.util.HashMap;

public class ShaderUtils {
    private HashMap<Class<? extends ShaderProgram>, ShaderProgram> shaderPrograms = new HashMap<>();
    private static ShaderUtils ourInstance = new ShaderUtils();

    public static ShaderUtils getInstance() {
        return ourInstance;
    }

    private ShaderUtils() {
    }

    public void registerShader(ShaderProgram shaderProgram) {
        shaderPrograms.put(shaderProgram.getClass(), shaderProgram);
    }

    public <T extends ShaderProgram> T retrieveShader(Class<T> cl) {
        return cl.cast(shaderPrograms.get(cl));
    }
}
