package engine.Scene.Lightning;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class Shadow {
    private int depthMapFBO, depthMapTexture;
    public int WIDTH = 4096, HEIGHT = 4096;
    private final float NEAR = -1f, FAR = 10f;
    private Matrix4f lightProjection, lightView, biasMatrix;

    public Shadow(Vector3f lightPos) {
        depthMapFBO = glGenFramebuffers();
        createShadowTexture(WIDTH, HEIGHT);
        attachTextureToFBO();
        createLightFrustrum();
        createLightView(lightPos);
        createBiasMatrix();
    }

    public void createShadowTexture(int width, int height) {
        depthMapTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthMapTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
    }

    public void attachTextureToFBO() {
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMapTexture, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void createLightFrustrum() {
        lightProjection = new Matrix4f();
        lightProjection.ortho(-2, 2, -2, 2, NEAR, FAR);
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

    public int getDepthMapFBO() {
        return depthMapFBO;
    }

    public int getDepthMapTexture() {
        return depthMapTexture;
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
}
