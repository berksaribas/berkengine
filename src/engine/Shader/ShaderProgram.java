package engine.Shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    int vertexShaderProgram, fragmentShaderProgram, programID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexShader, String fragmentShader) {
        vertexShaderProgram = ShaderLoader.loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
        fragmentShaderProgram = ShaderLoader.loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();

        GL20.glAttachShader(programID, vertexShaderProgram);
        GL20.glAttachShader(programID, fragmentShaderProgram);
        bindAttributes();

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    protected abstract void bindAttributes();

    protected void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector3(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadVector4(int location, Vector4f value) {
        GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    protected void loadBoolean(int location, boolean value) {
        float val = value ? 1 : 0;
        GL20.glUniform1f(location, val);
    }

    protected void loadMatrix(int location, Matrix4f value) {
        GL20.glUniformMatrix4fv(location, false, value.get(matrixBuffer));
    }

    protected void bindAttribute(int attribute, String name) {
        GL20.glBindAttribLocation(programID, attribute, name);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderProgram);
        GL20.glDetachShader(programID, fragmentShaderProgram);
        GL20.glDeleteShader(vertexShaderProgram);
        GL20.glDeleteShader(fragmentShaderProgram);
        GL20.glDeleteProgram(programID);
    }
}
