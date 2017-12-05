package engine.Physics;

import org.joml.Vector3f;

public class AABB {
    Vector3f position;
    Vector3f extend;

    public AABB(Vector3f position, Vector3f extend) {
        this.extend = extend;
        this.position = position;
    }

    public boolean intersects(AABB object) {
        float or = object.position.x + object.extend.x;
        float ol = object.position.x - object.extend.x;

        float ou = object.position.y + object.extend.y;
        float ob = object.position.y - object.extend.y;

        float of = object.position.z + object.extend.z;
        float on = object.position.z - object.extend.z;

        float tr = position.x + extend.x;
        float tl = position.x - extend.x;

        float tu = position.y + extend.y;
        float tb = position.y - extend.y;

        float tf = position.z + extend.z;
        float tn = position.z - extend.z;


        if((or > tl && or - tl > (object.extend.x + extend.x) * 2)) {
            return false;
        } else if(tl > ol && tr - ol > (object.extend.x + extend.x) * 2) {
            return false;
        }

        if((ou > tb && ou - tb > (object.extend.y + extend.y) * 2)) {
            return false;
        } else if(tu > ob && tu - ob > (object.extend.y + extend.y) * 2) {
            return false;
        }

        if((of > tn && of - tn > (object.extend.z + extend.z) * 2)) {
            return false;
        } else if(tf > on && tf - on > (object.extend.z + extend.z) * 2) {
            return false;
        }

        return true;
    }
}
