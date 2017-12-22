package engine;

import engine.Helper.MatrixHelper;
import engine.Model.QuadModel;
import engine.Model.RawModel;
import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.Scene.Lightning.Shadow;
import engine.Shader.ObjectShader;
import engine.Shader.ShadowDebuggerShader;
import engine.Shader.ShadowShader;
import engine.Texture.Skybox;
import engine.Texture.Texture;
import engine.UI.Quad;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;

public class Renderer {

    ShadowDebuggerShader shadowDebuggerShader;

    public Renderer() {
        shadowDebuggerShader = new ShadowDebuggerShader();
    }

    public void prepare() {
        GL11.glClearColor(0.0f, 0.8f, 0.8f , 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void renderShadows(HashMap<TexturedModel, ArrayList<GameObject>> objects, Shadow shadow, ShadowShader shadowShader) {
        GL11.glCullFace(GL11.GL_FRONT);
        shadowShader.setLightSpaceMatrix(shadow.getLightSpaceMatrix());
        GL11.glViewport(0, 0, shadow.getSize(), shadow.getSize());
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, shadow.getFbo().getId());
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        for (TexturedModel texturedModel : objects.keySet()) {
            RawModel rawModel = texturedModel.getRawModel();

            enableVAA(rawModel, 3);
            ArrayList<GameObject> gameObjects = objects.get(texturedModel);
            for(int i = 0; i < gameObjects.size(); i++) {
                GameObject object = gameObjects.get(i);
                Matrix4f transformationMatrix = MatrixHelper.
                        createTransformationMatrix(object);
                shadowShader.setTransformationMatrix(transformationMatrix);
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            disableVAA(3);
        }
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glCullFace(GL11.GL_BACK);
    }

    void renderQuad(Quad quad, Shadow shadow)
    {
        quad.getShaderProgram().start();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, quad.getTexture().getId());

        GL30.glBindVertexArray(QuadModel.getInstance().getQuadModel().getVaoID());

        enableVAA(QuadModel.getInstance().getQuadModel(), 2);

        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        disableVAA(2);

        quad.getShaderProgram().stop();
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

            enableVAA(rawModel, 3);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
            objectShader.setTexture(0);

            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadow.getTexture().getId());
            objectShader.setDepthTexture(1);

            ArrayList<GameObject> gameObjects = objects.get(texturedModel);
            for(int i = 0; i < gameObjects.size(); i++) {
                GameObject object = gameObjects.get(i);
                Matrix4f transformationMatrix = MatrixHelper.
                        createTransformationMatrix(object);
                objectShader.setTransformationMatrix(transformationMatrix);
                objectShader.setTextureRepeat(object.getTextureRepeat());

                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            texture.unbind();

            disableVAA(3);
        }
    }

    private void enableVAA(RawModel rawModel, int count) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        for(int i = 0; i < count; i++) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    private void disableVAA(int count) {
        for(int i = 0; i < count; i++) {
            GL20.glDisableVertexAttribArray(i);
        }
        GL30.glBindVertexArray(0);
    }
}
