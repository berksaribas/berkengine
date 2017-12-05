package engine.Shader;

import org.joml.Matrix4f;

public class ShadowDebuggerShader extends ShaderProgram{
    private static final String vertexString = "ShadowDebuggerShaderVertex.glsl";
    private static final String fragmentLocation = "ShadowDebuggerShaderFragment.glsl";


    public ShadowDebuggerShader() {
        super(vertexString, fragmentLocation);
    }

    @Override
    protected void getAllUniformLocations() {
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
    }

}
