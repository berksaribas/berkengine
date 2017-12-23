package engine.Texture;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Texture {

    private int width, height;
    private int id;

    public Texture(String path) {
        ImageData imageData = TextureLoader.loadFile(path);

        width = imageData.getWidth();
        height = imageData.getHeight();

        int[] data = imageData.getData();

        generateTexture();

        bind();

        setIntParam(GL_TEXTURE_WRAP_S, GL_REPEAT);
        setIntParam(GL_TEXTURE_WRAP_T, GL_REPEAT);

        setIntParam(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        setIntParam(GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        enableAnisotrophicFiltering();

        applyTexture(data, GL_RGBA, GL_UNSIGNED_BYTE);

        unbind();

    }

    public Texture(int width, int height, int format, int type) {
        this.width = width;
        this.height = height;

        generateTexture();

        bind();

        setIntParam(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        setIntParam(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        setIntParam(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        setIntParam(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        applyTexture(format, type);

        unbind();
    }

    private void generateTexture() {
        id = glGenTextures();
    }

    public void setIntParam(int pname, int param) {
        glTexParameteri(GL_TEXTURE_2D, pname, param);
    }

    public void setFloatParam(int pname, int param, float[] arr) {
        glTexParameterfv(pname, param, arr);
    }

    private void enableAnisotrophicFiltering() {
        float amount = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
    }

    private void applyTexture(int[] data, int format, int type) {

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length << 2)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format,
                type, buffer);

        GL30.glGenerateMipmap(GL_TEXTURE_2D);
    }

    private void applyTexture(int format, int type) {
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format,
                type, 0);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }
}
