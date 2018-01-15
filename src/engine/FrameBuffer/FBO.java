package engine.FrameBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class FBO {
    private final int DEFAULT_BUFFER = 0;

    private int id;

    public FBO() {
        id = glGenFramebuffers();
    }

    public void attachTexture(int texture, int attachment) {
        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texture, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        unbind();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, DEFAULT_BUFFER);
    }

    public int getId() {
        return id;
    }
}
