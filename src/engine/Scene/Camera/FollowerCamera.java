package engine.Scene.Camera;

import engine.Helper.MatrixHelper;
import engine.Input.MouseHandler;
import engine.Object.GameObject;
import engine.Scene.Camera.Camera;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FollowerCamera extends Camera {

    GameObject object;
    float eyeHeight;
    public float yAngle;

    public FollowerCamera(GameObject object, float eyeHeight) {
        super();
        this.object = object;
        this.eyeHeight = eyeHeight;
        yAngle = 0;
        followObject();
    }
    public Vector3f getRotation(Matrix4f matrix){
        float yaw = 0.0f;
        float pitch = 0.0f;
        float roll = 0.0f;
        if(matrix.m00() == 1.0f || matrix.m00() == -1.0f){
            yaw = (float)Math.atan2(matrix.m02(), matrix.m23());
            //pitch and roll remain = 0;
        }else{
            yaw = (float) Math.atan2(-matrix.m20(), matrix.m00());
            pitch = (float) Math.asin(matrix.m10());
            roll = (float) Math.atan2(-matrix.m12(), matrix.m11());
        }
        return new Vector3f(yaw, pitch, roll);
    }

    public void followObject() {
        Vector3f pos = new Vector3f();

        Matrix4f playerRealPosition = MatrixHelper.createTransformationMatrix(object);

        AxisAngle4f rotation = new AxisAngle4f();

        playerRealPosition.getRotation(rotation);

        playerRealPosition.getTranslation(pos);

        Vector3f rot = new Vector3f(rotation.x * rotation.angle, rotation.y * rotation.angle, rotation.z * rotation.angle);

        eye = new Vector3f((float)(pos.x - 0.025f * Math.sin(rot.y)), pos.y + 0.25f, (float)(pos.z - 0.025f * Math.cos(rot.y)));

        center = new Vector3f((float)((eye.x - 1 * Math.cos(yAngle) * Math.sin(rot.y)) ), eye.y + (float)(1 * Math.sin(yAngle)), (float)((eye.z - 1 * Math.cos(yAngle) * Math.cos(rot.y)) ));

        up = new Vector3f((float)(Math.sin(rot.y) * Math.sin(yAngle)) , (float) Math.cos(yAngle), (float) (Math.cos(rot.y) * Math.sin(yAngle)));
    }

    public void handleFPSMovement(float delta) {

        object.getRotation().y -= (float) MouseHandler.getDX() / (60000f * delta + 0.0000001f);

        yAngle -= MouseHandler.getDY() / (30000f * delta + 0.0000001f);
        if(yAngle > Math.PI / 2) yAngle = (float)(Math.PI / 2);
        if(yAngle < -Math.PI / 2) yAngle = (float)(-Math.PI / 2);

    }



}
