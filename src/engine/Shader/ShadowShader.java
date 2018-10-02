package engine.Shader;

import org.joml.Matrix4f;

public class ShadowShader extends ShaderProgram {
    private static final String vertexString = "ShadowShaderVertex.glsl";
    private static final String fragmentLocation = "ShadowShaderFragment.glsl";

    private int transformationMatrix, lightSpaceMatrix;

    public ShadowShader() {
        super(vertexString, fragmentLocation);
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrix = getUniformLocation("transformationMatrix");
        lightSpaceMatrix = getUniformLocation("lightSpaceMatrix");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void setLightSpaceMatrix(Matrix4f matrix) {
        loadMatrix(lightSpaceMatrix, matrix);
    }

    public void setTransformationMatrix(Matrix4f matrix) {
        loadMatrix(transformationMatrix, matrix);
    }

}
