package engine.Model;

public class RawModel {
    int vaoID, vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public RawModel() {
    }

    public void setRawModel(RawModel rawModel) {
        this.vaoID = rawModel.vaoID;
        this.vertexCount = rawModel.vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
