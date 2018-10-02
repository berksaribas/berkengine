package engine.Base.Systems;

import engine.Base.Components.CameraComponent;
import engine.Base.Components.SkyboxComponent;
import engine.Base.Utils.ShaderUtils;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.Model.RawModel;
import engine.Scene.Camera.Camera;
import engine.Scene.Scene;
import engine.Shader.SkyboxShader;
import engine.Texture.Skybox;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SkyboxSystem extends ExecuteSystem {
    private SkyboxShader skyboxShader;
    private Camera camera;
    private FilterGroup filterGroup;

    public SkyboxSystem(EntityManager entityManager) {
        super(entityManager);
        skyboxShader = ShaderUtils.getInstance().retrieveShader(SkyboxShader.class);
        filterGroup = new FilterGroup(entityManager).collect(SkyboxComponent.class);
    }

    @Override
    protected void execute() {
        skyboxShader.start();

        if (camera == null) {
            camera = entityManager.getSingleEntity(CameraComponent.class).getComponent(CameraComponent.class).camera;
        }

        camera.update(skyboxShader);
        GL11.glViewport(0, 0, Scene.WIDTH, Scene.HEIGHT);

        for (Entity entity : filterGroup.getEntities()) {
            GL11.glCullFace(GL11.GL_BACK);
            GL11.glDisable(GL11.GL_BLEND);

            Skybox skybox = entity.getComponent(SkyboxComponent.class).skybox;

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
        skyboxShader.stop();
    }
}
