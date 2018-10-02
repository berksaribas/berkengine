package engine.Shader;

import org.joml.Matrix4f;

public class SkyboxShader extends ShaderProgram {
    private static final String vertexString = "SkyboxShaderVertex.glsl";
    private static final String fragmentLocation = "SkyboxShaderFragment.glsl";

    private int projectionMatrix, viewMatrix;

    public SkyboxShader() {
        super(vertexString, fragmentLocation);
    }

    @Override
    protected void getAllUniformLocations() {
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void setProjectionMatrix(Matrix4f matrix) {
        loadMatrix(projectionMatrix, matrix);
    }

    public void setCamera(Matrix4f matrix) {
        Matrix4f edited = new Matrix4f(matrix);
        edited.m03(0);
        edited.m13(0);
        edited.m23(0);
        edited.m33(1);
        edited.m30(0);
        edited.m31(0);
        edited.m32(0);
        loadMatrix(viewMatrix, edited);
    }
}
