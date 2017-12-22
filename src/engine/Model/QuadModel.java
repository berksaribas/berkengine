package engine.Model;

public class QuadModel {
    private RawModel quadModel;
    private static QuadModel ourInstance = new QuadModel();

    public static QuadModel getInstance() {
        return ourInstance;
    }

    private QuadModel() {
        float quadVertices[] = {
                -1.0f,  1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f,  1.0f, 0.0f,
                1.0f, -1.0f, 0.0f
        };

        float quadTexture[] = {
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        quadModel = ModelLoader.getInstance().loadToVAO(quadVertices, quadTexture);
    }

    public RawModel getQuadModel() {
        return quadModel;
    }
}
