package engine;

import engine.FrameBuffer.FBO;
import engine.Helper.MatrixHelper;
import engine.Model.QuadModel;
import engine.Model.RawModel;
import engine.Model.TexturedModel;
import engine.Object.GameObject;
import engine.PostFX.GaussianBlur;
import engine.Scene.Lightning.Shadow;
import engine.Shader.ObjectShader;
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
    public GaussianBlur gaussianBlur;
    FBO postProcessor;
    boolean done = false;

    public Renderer() {
        gaussianBlur = new GaussianBlur(4096, 4096);
        postProcessor = new FBO();
    }

    public void prepare() {
        GL11.glClearColor(1f, 1f, 1f , 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void renderShadows(HashMap<TexturedModel, ArrayList<GameObject>> objects, Shadow shadow, ShadowShader shadowShader) {
        shadowShader.start();

        GL11.glDisable(GL11.GL_BLEND);

        shadowShader.setLightSpaceMatrix(shadow.getLightSpaceMatrix());
        GL11.glViewport(0, 0, shadow.getSize(), shadow.getSize());

        shadow.getFbo().bind();

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);

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

        shadow.getFbo().unbind();

        shadowShader.stop();

        applyGaussian(shadow.getColorTexture());
    }

    public void applyGaussian(Texture texture) {
        if(!done) {
            postProcessor.attachTexture(gaussianBlur.getResult().getId(), GL30.GL_COLOR_ATTACHMENT0);
            done = true;
        }

        gaussianBlur.apply(texture, postProcessor, this);
    }


    public void renderQuads(ArrayList<Quad> quads)
    {
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for(int i = 0; i < quads.size(); i++) {
            Quad quad = quads.get(i);

            quad.getShaderProgram().start();

            enableVAA(QuadModel.getInstance().getQuadModel(), 2);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, quad.getTexture().getId());

            Matrix4f transformationMatrix = MatrixHelper.
                    createTransformationMatrix(quad);

            quad.getShaderProgram().setTransformationMatrix(transformationMatrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            disableVAA(2);

            quad.getShaderProgram().stop();
        }
    }

    public void renderSkybox(Skybox skybox) {
        GL11.glCullFace(GL11.GL_BACK);

        GL11.glDisable(GL11.GL_BLEND);

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
        GL11.glCullFace(GL11.GL_BACK);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        for (TexturedModel texturedModel : objects.keySet()) {
            RawModel rawModel = texturedModel.getRawModel();
            Texture texture = texturedModel.getTexture();

            enableVAA(rawModel, 3);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
            objectShader.setTexture(0);

            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadow.getDepthTexture().getId());
            objectShader.setDepthTexture(1);

            GL13.glActiveTexture(GL13.GL_TEXTURE2);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gaussianBlur.getResult().getId());
            objectShader.setColorTexture(2);

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

    public void renderQuad(Texture texture) {
        enableVAA(QuadModel.getInstance().getQuadModel(), 2);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());

        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        disableVAA(2);
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
