package engine.Shader;

public class HorizontalBlurShader extends ShaderProgram {
    private static final String vertexString = "BlurShaderVertex.glsl";
    private static final String fragmentLocation = "HorizontalBlurShaderFragment.glsl";

    private int width;

    public HorizontalBlurShader() {
        super(vertexString, fragmentLocation);
    }

    @Override
    protected void getAllUniformLocations() {
        width = getUniformLocation("width");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
    }

    public void setWidth(float heightValue) {
        loadFloat(width, heightValue);
    }
}
