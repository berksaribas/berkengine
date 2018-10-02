package engine.Helper;


import engine.Model.QuadModel;
import engine.Model.RawModel;
import engine.Texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderHelper {
    private static RawModel previous = null;

    public static void renderQuad(Texture texture) {
        enableVAA(QuadModel.getInstance().getQuadModel(), 2);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());

        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        disableVAA(2);
    }

    public static void enableVAA(RawModel rawModel, int count) {
        if (rawModel == previous) {
            return;
        }

        disableVAA(count);

        previous = rawModel;

        GL30.glBindVertexArray(rawModel.getVaoID());
        for (int i = 0; i < count; i++) {
            GL20.glEnableVertexAttribArray(i);
        }
    }

    public static void disableVAA(int count) {
        for (int i = 0; i < count; i++) {
            GL20.glDisableVertexAttribArray(i);
        }
        GL30.glBindVertexArray(0);
    }
}
