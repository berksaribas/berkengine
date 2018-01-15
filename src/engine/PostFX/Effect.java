package engine.PostFX;

import engine.FrameBuffer.FBO;
import engine.Renderer;
import engine.Texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public abstract class Effect {
    protected Texture result;
    protected int width, height;

    public abstract void apply(Texture texture, FBO fbo, Renderer renderer);

    public Texture getResult() {
        return result;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected void clearAndSet() {
        GL11.glViewport(0, 0, width, height);

        GL11.glClearColor(1f, 1f, 1f , 1);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }

}
