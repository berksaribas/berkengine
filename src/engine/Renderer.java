package engine;

import engine.Helper.MatrixHelper;
import engine.Model.ModelLoader;
import engine.Model.RawModel;
import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.Scene.Lightning.Shadow;
import engine.Shader.ObjectShader;
import engine.Shader.ShadowDebuggerShader;
import engine.Shader.ShadowShader;
import engine.Texture.Skybox;
import engine.Texture.Texture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;

public class Renderer {

    RawModel quad;
    ShadowDebuggerShader shadowDebuggerShader;

    float quadVertices[] = {
            // positions
            -1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f,  1.0f, 0.0f,
            1.0f, -1.0f, 0.0f
    };

    float quadTexture[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    public Renderer() {
        quad = ModelLoader.getInstance().loadToVAO(quadVertices, quadTexture);
        shadowDebuggerShader = new ShadowDebuggerShader();
    }

    public void prepare() {
        GL11.glClearColor(0.0f, 0.8f, 0.8f , 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void renderShadows(HashMap<TexturedModel, ArrayList<GameObject>> objects, Shadow shadow, ShadowShader shadowShader) {
        //GL11.glCullFace(GL11.GL_FRONT);
        shadowShader.setLightSpaceMatrix(shadow.getLightSpaceMatrix());
        GL11.glViewport(0, 0, shadow.WIDTH, shadow.HEIGHT);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, shadow.getDepthMapFBO());
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        for (TexturedModel texturedModel : objects.keySet()) {
            RawModel rawModel = texturedModel.getRawModel();

            enableVAA(rawModel);
            ArrayList<GameObject> gameObjects = objects.get(texturedModel);
            for(int i = 0; i < gameObjects.size(); i++) {
                GameObject object = gameObjects.get(i);
                Matrix4f transformationMatrix = MatrixHelper.
                        createTransformationMatrix(object);
                shadowShader.setTransformationMatrix(transformationMatrix);
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            disableVAA();
        }
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glCullFace(GL11.GL_BACK);

        //renderQuad(shadow);
    }

    void renderQuad(Shadow shadow)
    {
        shadowDebuggerShader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadow.getDepthMapTexture());
        GL30.glBindVertexArray(quad.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
        shadowDebuggerShader.stop();
    }

    public void renderSkybox(Skybox skybox) {
        RawModel rawModel = skybox.getRawModel();

        GL11.glDepthMask(false);
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getTextureID());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        GL11.glDepthMask(true);

    }

    public void renderObject(HashMap<TexturedModel, ArrayList<GameObject>> objects, ObjectShader objectShader, Shadow shadow) {
        for (TexturedModel texturedModel : objects.keySet()) {
            RawModel rawModel = texturedModel.getRawModel();
            Texture texture = texturedModel.getTexture();

            enableVAA(rawModel);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
            objectShader.setTexture(0);

            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadow.getDepthMapTexture());
            objectShader.setDepthTexture(1);

            ArrayList<GameObject> gameObjects = objects.get(texturedModel);
            for(int i = 0; i < gameObjects.size(); i++) {
                GameObject object = gameObjects.get(i);
                Matrix4f transformationMatrix = MatrixHelper.
                        createTransformationMatrix(object);
                objectShader.setTransformationMatrix(transformationMatrix);
                objectShader.setTextureRepeat(texturedModel.getTextureRepeat());

                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            texture.unbind();

            disableVAA();
        }
    }

    private void enableVAA(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private void disableVAA() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
