package engine.Texture;

public class ImageData {
    private int[] data;
    private int width, height;

    public ImageData(int[] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public int[] getData() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
