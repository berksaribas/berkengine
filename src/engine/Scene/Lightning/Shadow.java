package engine.Scene.Lightning;

import engine.FrameBuffer.FBO;
import engine.Texture.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Shadow {
    private FBO fbo;
    private Texture texture;

    private final float NEAR = -1f, FAR = 100f;
    private Matrix4f lightProjection, lightView, biasMatrix;
    private int size;

    public Shadow(Vector3f lightPos, int size) {
        this.size = size;
        fbo = new FBO();
        createShadowTexture(size, size);
        fbo.attachTexture(texture.getId(), GL_DEPTH_ATTACHMENT);
        createLightFrustum();
        createLightView(lightPos);
        createBiasMatrix();
    }

    public void createShadowTexture(int width, int height) {
        texture = new Texture(width, height, GL_DEPTH_COMPONENT, GL_FLOAT);

        float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };

        texture.bind();
        texture.setFloatParam(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
        texture.unbind();
    }

    public void createLightFrustum() {
        lightProjection = new Matrix4f();
        lightProjection.ortho(-4, 4, -4, 4, NEAR, FAR);
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

    public Texture getTexture() {
        return texture;
    }

    public void updateLightView(Vector3f direction, Vector3f pos) {
        lightView = new Matrix4f();
        lightView.lookAt(new Vector3f(direction.x + pos.x, direction.y + pos.y, direction.z + pos.z), pos, new Vector3f(0, 1, 0));
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
