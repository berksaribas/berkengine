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
import engine.Shader.ShadowShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ShadowSystem extends ExecuteSystem {
    private GaussianBlur gaussianBlur = new GaussianBlur(4096, 4096);
    private ShadowShader shadowShader;
    private Shadow shadow;
    private FilterGroup filterGroup;

    public ShadowSystem(EntityManager entityManager) {
        super(entityManager);
        shadowShader = ShaderUtils.getInstance().retrieveShader(ShadowShader.class);
        filterGroup = new FilterGroup(entityManager).collect(TransformComponent.class, MeshComponent.class);
    }

    @Override
    protected void execute() {
        //TODO: Move this to another system
        GL11.glClearColor(1f, 1f, 1f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        LightComponent lightComponent = entityManager.getSingleEntity(LightComponent.class).getComponent(
                LightComponent.class);
        CameraComponent cameraComponent = entityManager.getSingleEntity(CameraComponent.class).getComponent(
                CameraComponent.class);

        shadow = entityManager.getSingleEntity(ShadowComponent.class).getComponent(ShadowComponent.class).shadow;
        shadow.updateLightView(new Vector3f(lightComponent.light.getPosition().x,
                        lightComponent.light.getPosition().y,
                        lightComponent.light.getPosition().z),
                cameraComponent.camera.getEye());

        shadowShader.start();

        GL11.glDisable(GL11.GL_BLEND);

        shadowShader.setLightSpaceMatrix(shadow.getLightSpaceMatrix());
        GL11.glViewport(0, 0, shadow.getSize(), shadow.getSize());

        shadow.getFbo().bind();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);

        for (Entity entity : filterGroup.getEntities()) {
            MeshComponent meshComponent = entity.getComponent(MeshComponent.class);
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);

            RawModel rawModel = meshComponent.texturedModel.getRawModel();

            RenderHelper.enableVAA(rawModel, 3);

            Matrix4f transformationMatrix = MatrixHelper.
                    createTransformationMatrix(transformComponent.position, transformComponent.rotation,
                            transformComponent.scale);
            shadowShader.setTransformationMatrix(transformationMatrix);
            GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        }

        RenderHelper.disableVAA(3);

        shadow.getFbo().unbind();

        EffectComponent effectComponent = new EffectComponent(gaussianBlur, shadow.getColorTexture());

        entityManager.createEntity().addComponent(effectComponent);
    }
}
