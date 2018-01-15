package engine.Shader;

import org.joml.Matrix4f;

public class QuadShader extends ShaderProgram{
    private static final String vertexString = "QuadShaderVertex.glsl";
    private static final String fragmentLocation = "QuadShaderFragment.glsl";
    private int transformationMatrix;

    public QuadShader() {
        super(vertexString, fragmentLocation);
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrix = getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
    }

    public void setTransformationMatrix(Matrix4f matrix) {
        loadMatrix(transformationMatrix, matrix);
    }

}
