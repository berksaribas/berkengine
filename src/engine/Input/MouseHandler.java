package engine.Input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseHandler extends GLFWCursorPosCallback {
    private static int mouseX = 0, mouseY = 0;
    private static int mouseDX = 0, mouseDY = 0;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        mouseDX += (int) xpos - mouseX;
        mouseDY += (int) ypos - mouseY;

        mouseX = (int) xpos;
        mouseY = (int) ypos;
    }

    public static int getDX() {
        return mouseDX | (mouseDX = 0);
    }

    public static int getDY() {
        return mouseDY | (mouseDY = 0);
    }
}
