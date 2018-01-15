package engine.UI;

import java.util.ArrayList;

public class UIController {
    ArrayList<Quad> quads;

    public UIController() {
        quads = new ArrayList<>();
    }

    public void addNewUIElement(Quad quad) {
        quads.add(quad);
    }

    public ArrayList<Quad> getQuads() {
        return quads;
    }
}
