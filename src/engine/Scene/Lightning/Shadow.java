package engine.Scene.Lightning;

import engine.FrameBuffer.FBO;
import engine.Texture.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Shadow {
    private FBO fbo;
    private Texture depthTexture, colorTexture;

    private final float NEAR = -1f, FAR = 10f, RADIUS = 4f;
    private Matrix4f lightProjection, lightView, biasMatrix;
    private int size;

    public Shadow(Vector3f lightPos, int size) {
        this.size = size;
        fbo = new FBO();
        createDepthTexture(size, size);
        createColorTexture(size, size);
        fbo.attachTexture(depthTexture.getId(), GL_DEPTH_ATTACHMENT);
        fbo.attachTexture(colorTexture.getId(), GL_COLOR_ATTACHMENT0);
        createLightFrustum();
        createLightView(lightPos);
        createBiasMatrix();
    }

    public void createDepthTexture(int width, int height) {
        depthTexture = new Texture(width, height, GL_NEAREST, GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT, GL_FLOAT);
        setBorder(depthTexture);
    }

    public void createColorTexture(int width, int height) {
        colorTexture = new Texture(width, height, GL_LINEAR, GL_RG32F, GL_RG, GL_FLOAT);
        setBorder(colorTexture);
    }

    public void setBorder(Texture texture) {
        float borderColor[] = {1.0f, 1.0f, 1.0f, 1.0f};

        texture.bind();
        texture.setFloatParam(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
        texture.unbind();
    }

    public void createLightFrustum() {
        lightProjection = new Matrix4f();
        lightProjection.ortho(-RADIUS, RADIUS, -RADIUS, RADIUS, NEAR, FAR);
    }

    public void createLightView(Vector3f pos) {
        lightView = new Matrix4f();
        lightView.lookAt(pos, new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
    }

    public void createBiasMatrix() {
        biasMatrix = new Matrix4f();
        biasMatrix.scale(0.5f);
        biasMatrix.setTranslation(0.5f, 0.5f, 0.5f);
    }

    public FBO getFbo() {
        return fbo;
    }

    public Texture getDepthTexture() {
        return depthTexture;
    }

    public Texture getColorTexture() {
        return colorTexture;
    }

    public void updateLightView(Vector3f direction, Vector3f pos) {
        lightView = new Matrix4f();

        lightView.lookAt(new Vector3f(direction.x + pos.x, direction.y + pos.y, direction.z + pos.z), pos,
                new Vector3f(0, 1, 0));

        lightView.m30(lightView.m30() - (lightView.m30() % (2f * RADIUS / size)));
        lightView.m31(lightView.m31() - (lightView.m31() % (2f * RADIUS / size)));
        lightView.m32(lightView.m32() - (lightView.m32() % (2f * RADIUS / size)));
    }

    public Matrix4f getLightSpaceMatrix() {
        Matrix4f lightSpaceMatrix = new Matrix4f();
        lightProjection.mul(lightView, lightSpaceMatrix);
        return lightSpaceMatrix;
    }

    public Matrix4f getDepthBias() {
        Matrix4f result = new Matrix4f();
        biasMatrix.mul(getLightSpaceMatrix(), result);
        return result;
    }

    public int getSize() {
        return size;
    }
}
