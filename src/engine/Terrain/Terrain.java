package engine.Terrain;

import engine.Model.ModelLoader;
import engine.Model.RawModel;
import org.joml.SimplexNoise;
import org.joml.Vector3f;

public class Terrain {

    private int verticeCount;
    private float size, height;

    public Terrain(int verticeCount, float size, float height) {
        this.verticeCount = verticeCount;
        this.size = size;
        this.height = height;
    }

    public RawModel generateTerrain() {
        int count = verticeCount;
        int triangleCount = count * count;

        float[] vertices = new float[triangleCount * 3];
        float[] normals = new float[triangleCount * 3];
        int[] indices = new int[6 * (count - 1) * (count - 1)];
        float[] textureCoords = new float[triangleCount*2];

        for(int z = 0; z < count; z++) {
            for(int x = 0; x < count; x++) {
                int currentIndex = z * count + x;

                float verticeXPos = x / (count - 1f) * size;
                float verticeZPos = z / (count - 1f) * size;

                vertices[currentIndex * 3 + 0] = verticeXPos;//x
                vertices[currentIndex * 3 + 1] = getHeight(x, z);//y
                vertices[currentIndex * 3 + 2] = verticeZPos;//z

                Vector3f normal = calculateNormal(x, z);

                normals[currentIndex * 3 + 0] = normal.x;//x
                normals[currentIndex * 3 + 1] = normal.y;//y
                normals[currentIndex * 3 + 2] = normal.z;//z

                textureCoords[currentIndex * 2] = z / (count - 1f);
                textureCoords[currentIndex * 2+1] = x / (count - 1f);
            }
        }

        int indice = 0;

        for(int z = 0; z < count - 1; z++) {
            for(int x = 0; x < count - 1; x++) {
                int topLeft = (z * count) + x;
                int topRight = topLeft + 1;
                int bottomLeft = (z + 1) * count + x;
                int bottomRight = bottomLeft + 1;

                indices[indice ++] = topLeft;
                indices[indice ++] = bottomLeft;
                indices[indice ++] = topRight;
                indices[indice ++] = topRight;
                indices[indice ++] = bottomLeft;
                indices[indice ++] = bottomRight;
            }
        }

        return ModelLoader.getInstance().loadToVAO(vertices, indices, normals, textureCoords);
    }

    private Vector3f calculateNormal(int x, int z) {
        float heightL = getHeight(x - 1, z);
        float heightR = getHeight(x + 1, z);
        float heightD = getHeight(x, z - 1);
        float heightU = getHeight(x, z + 1);

        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalize();

        return normal;
    }

    private float getHeight(int x, int z) {
        return getSmoothedNoise(x, z);
    }

    private float getSmoothedNoise(int x, int z) {
        float xx = x / (float)verticeCount - 0.5f;
        float zz = z / (float)verticeCount - 0.5f;

        float result = (SimplexNoise.noise(xx, zz) * 1f
                + SimplexNoise.noise(2 * xx, 2 * zz) * 0.5f
                + SimplexNoise.noise(4 * xx, 4 * zz) * 0.25f) * height;
        return result;
    }
}
