package engine.Base.Systems;

import engine.Base.Components.QuadComponent;
import engine.Base.Utils.ShaderUtils;
import engine.EntityComponentSystem.Entity.Entity;
import engine.EntityComponentSystem.Entity.EntityManager;
import engine.EntityComponentSystem.Filter.FilterGroup;
import engine.EntityComponentSystem.System.ExecuteSystem;
import engine.Helper.MatrixHelper;
import engine.Helper.RenderHelper;
import engine.Model.QuadModel;
import engine.Shader.QuadShader;
import engine.UI.Quad;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class QuadSystem extends ExecuteSystem {
    private QuadShader quadShader;
    private FilterGroup filterGroup;

    public QuadSystem(EntityManager entityManager) {
        super(entityManager);
        quadShader = ShaderUtils.getInstance().retrieveShader(QuadShader.class);
        filterGroup = new FilterGroup(entityManager).collect(QuadComponent.class);
    }

    @Override
    protected void execute() {
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        quadShader.start();

        for (Entity entity : filterGroup.getEntities()) {
            Quad quad = entity.getComponent(QuadComponent.class).quad;

            RenderHelper.enableVAA(QuadModel.getInstance().getQuadModel(), 2);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, quad.getTexture().getId());

            Matrix4f transformationMatrix = MatrixHelper.
                    createTransformationMatrix(quad);

            quadShader.setTransformationMatrix(transformationMatrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        RenderHelper.disableVAA(2);

        quadShader.stop();
    }
}
