package engine.Shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ObjectShader extends ShaderProgram{
    private static final String vertexString = "ObjectShaderVertex.glsl";
    private static final String fragmentLocation = "ObjectShaderFragment.glsl";

    private int transformationMatrix, projectionMatrix, viewMatrix, cameraPosition, lightPosition, lightColor, textureRepeat, depthBias;
    private int textureSampler, shadowMap;

    public ObjectShader() {
        super(vertexString, fragmentLocation);
        loadInt(getUniformLocation("textureSampler"), 0);
        loadInt(getUniformLocation("shadowMap"), 1);

    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrix = getUniformLocation("transformationMatrix");
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");
        cameraPosition = getUniformLocation("cameraPosition");
        lightPosition = getUniformLocation("lightPosition");
        lightColor = getUniformLocation("lightColor");
        textureRepeat = getUniformLocation("textureRepeat");
        depthBias = getUniformLocation("depthBias");
        textureSampler = getUniformLocation("textureSampler");
        shadowMap = getUniformLocation("shadowMap");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "normal");
        bindAttribute(2, "textureCoordinates");
    }

    public void setTransformationMatrix(Matrix4f matrix) {
        loadMatrix(transformationMatrix, matrix);
    }

    public void setProjectionMatrix(Matrix4f matrix) {
        loadMatrix(projectionMatrix, matrix);
    }

    public void setLight(Vector4f pos, Vector3f col) {
        loadVector4(lightPosition, pos);
        loadVector3(lightColor, col);
    }

    public void setCamera(Matrix4f matrix, Vector3f pos) {
        loadMatrix(viewMatrix, matrix);
        loadVector3(cameraPosition, pos);
    }

    public void setDepthBias(Matrix4f matrix) {
        loadMatrix(depthBias, matrix);
    }

    public void setTexture(int i) {
        loadInt(textureSampler, i);
    }

    public void setDepthTexture(int i) {
        loadInt(shadowMap, i);
    }

    public void setTextureRepeat(int repeat) {
        loadInt(textureRepeat, repeat);
    }
}
