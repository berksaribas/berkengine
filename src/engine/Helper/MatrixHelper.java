package engine.Helper;

import engine.UI.Quad;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixHelper {
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotateXYZ(rotation);
        matrix.scale(scale);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Quad quad) {
        Matrix4f matrix = createTransformationMatrix(quad.getPosition(), quad.getRotation(), quad.getScale());
        return matrix;
    }

    public static Matrix4f createProjectionMatrix(float width, float height, float fov, float nearPlane, float farPlane) {
        Matrix4f matrix = new Matrix4f();
        matrix.perspective((float) (Math.toRadians(fov)), width / height, nearPlane, farPlane);

        return matrix;
    }
}
