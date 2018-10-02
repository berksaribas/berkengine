package engine.Scene.Camera;

import engine.Shader.ObjectShader;
import engine.Shader.SkyboxShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f matrix;
    protected Vector3f eye, center, up;

    public Camera() {
        matrix = new Matrix4f();
        up = new Vector3f(0, 1, 0);
    }

    public Matrix4f getViewMatrix() {
        matrix.identity();
        matrix.setLookAt(eye, center, up);
        return matrix;
    }


    public void update(ObjectShader shader) {
        shader.start();
        shader.setCamera(getViewMatrix(), eye);
        shader.stop();
    }

    public void update(SkyboxShader skyboxShader) {
        skyboxShader.setCamera(getViewMatrix());
    }

    public Vector3f getEye() {
        return eye;
    }

    public Vector3f getCenter() {
        return center;
    }
}
