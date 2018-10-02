package engine.Model;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ModelLoader {
    private static ModelLoader ourInstance = new ModelLoader();

    private ArrayList<Integer> vaos = new ArrayList<>();
    private ArrayList<Integer> vbos = new ArrayList<>();

    public static ModelLoader getInstance() {
        return ourInstance;
    }

    private ModelLoader() {

    }

    public RawModel loadToVAO(float[] vertices, int[] indices, float[] normals, float[] textureCoordinates) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 3, normals);
        storeDataInAttributeList(2, 2, textureCoordinates);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadToVAO(float[] vertices, int[] indices, float[] normals) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadToVAO(float[] vertices) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 3, vertices);
        unbindVAO();
        return new RawModel(vaoID, vertices.length / 3);
    }

    ;

    public RawModel loadToVAO(float[] vertices, float[] textureCoordinates) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoordinates);
        unbindVAO();
        return new RawModel(vaoID, vertices.length / 3);
    }

    ;

    private void storeDataInAttributeList(int attributeNumber, int size, float[] vertices) {
        int vboID = GL15.glGenBuffers();
        FloatBuffer buffer = createFloatBuffer(vertices);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        IntBuffer buffer = createIntBuffer(indices);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
