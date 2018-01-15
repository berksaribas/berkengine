package engine.Shader;

public class VerticalBlurShader extends ShaderProgram {
    private static final String vertexString = "BlurShaderVertex.glsl";
    private static final String fragmentLocation = "VerticalBlurShaderFragment.glsl";

    private int height;

    public VerticalBlurShader() {
        super(vertexString, fragmentLocation);
    }

    @Override
    protected void getAllUniformLocations() {
        height = getUniformLocation("height");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
    }

    public void setWidth(float heightValue) {
        loadFloat(height, heightValue);
    }
}
