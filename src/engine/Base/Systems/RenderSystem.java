package engine.Base.Systems;

import engine.Base.Components.*;
import engine.Base.Utils.ShaderUtils;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.Helper.MatrixHelper;
import engine.Helper.RenderHelper;
import engine.Model.RawModel;
import engine.PostFX.GaussianBlur;
import engine.Scene.Lightning.Shadow;
import engine.Shader.ObjectShader;
import engine.Texture.Texture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.HashSet;

public class RenderSystem extends ExecuteSystem {
    private ObjectShader objectShader;
    private FilterGroup filterGroup;

    public RenderSystem(EntityManager entityManager) {
        super(entityManager);
        objectShader = ShaderUtils.getInstance().retrieveShader(ObjectShader.class);
        filterGroup = new FilterGroup(entityManager).collect(TransformComponent.class, MeshComponent.class);
    }

    @Override
    protected void execute() {
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GaussianBlur gaussianBlur = (GaussianBlur) entityManager.getSingleEntity(EffectComponent.class).getComponent(
                EffectComponent.class).effect;
        Shadow shadow = entityManager.getSingleEntity(ShadowComponent.class).getComponent(ShadowComponent.class).shadow;

        LightComponent lightComponent = entityManager.getSingleEntity(LightComponent.class).getComponent(
                LightComponent.class);
        objectShader.start();
        objectShader.setLight(lightComponent.light.getPosition(), lightComponent.light.getColor());
        objectShader.setDepthBias(shadow.getDepthBias());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        shadow.getDepthTexture().bind();
        objectShader.setDepthTexture(1);

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        gaussianBlur.getResult().bind();
        objectShader.setColorTexture(2);

        HashSet<Entity> entities = filterGroup.getEntities();

        for (Entity entity : entities) {
            MeshComponent meshComponent = entity.getComponent(MeshComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

            if(!meshComponent.active) {
                continue;
            }

            RawModel rawModel = meshComponent.texturedModel.getRawModel();
            Texture texture = meshComponent.texturedModel.getTextures().get(0);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            texture.bind();
            objectShader.setTexture(0);

            RenderHelper.enableVAA(rawModel, 3);
            Matrix4f transformationMatrix = MatrixHelper.
                    createTransformationMatrix(transformComponent.position, transformComponent.rotation,
                            transformComponent.scale);
            objectShader.setTransformationMatrix(transformationMatrix);
            objectShader.setTextureRepeat(1);

            GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            texture.unbind();
        }
        RenderHelper.disableVAA(3);

        objectShader.stop();
        GL11.glDisable(GL11.GL_BLEND);

    }
}
