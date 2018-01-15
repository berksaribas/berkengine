package engine.Shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ShaderLoader {

    public static int loadShader(String fileName, int type) {
        String shaderLines = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("shaders/" + fileName));
            String line;
            while((line = reader.readLine()) != null) {
                shaderLines += line + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderLines);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Shader compilation error");
        }

        return shaderID;
    }
}
