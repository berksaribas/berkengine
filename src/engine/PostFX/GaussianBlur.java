package engine.PostFX;

import engine.FrameBuffer.FBO;
import engine.Renderer;
import engine.Shader.HorizontalBlurShader;
import engine.Shader.VerticalBlurShader;
import engine.Texture.Texture;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL30.GL_RG;
import static org.lwjgl.opengl.GL30.GL_RG32F;

public class GaussianBlur extends Effect{
    private VerticalBlurShader verticalBlurShader;
    private HorizontalBlurShader horizontalBlurShader;

    public GaussianBlur(int width, int height) {
        verticalBlurShader = new VerticalBlurShader();
        horizontalBlurShader = new HorizontalBlurShader();

        this.width = width;
        this.height = height;

        result = new Texture(width, height, GL_LINEAR, GL_RG32F, GL_RG, GL_FLOAT);
    }

    @Override
    public void apply(Texture texture, FBO fbo, Renderer renderer) {
        fbo.bind();

        clearAndSet();

        horizontalBlurShader.start();

        renderer.renderQuad(texture);

        verticalBlurShader.start();

        renderer.renderQuad(result);

        fbo.unbind();
    }

}
