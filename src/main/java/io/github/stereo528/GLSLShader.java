package io.github.stereo528;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL46.*;

public class GLSLShader {
    private final int programID;
    private final int timeUniform;
    private final int mouseUniform;
    private final int resolutionUniform;

    public GLSLShader(String vertexShader, String fragmentShader) throws IOException {
        int program = glCreateProgram();


        System.out.println(vertexShader);
        System.out.println(fragmentShader);

        int vertexShaderID = createShader(GLSLShader.class.getResourceAsStream(vertexShader), GL_VERTEX_SHADER);
        int fragmentShaderID = createShader(GLSLShader.class.getResourceAsStream(fragmentShader), GL_FRAGMENT_SHADER);
        System.out.println(vertexShaderID);
        System.out.println(fragmentShaderID);

        glAttachShader(program, vertexShaderID);
        glAttachShader(program, fragmentShaderID);

        this.programID = program;

        glLinkProgram(program);

        timeUniform = glGetUniformLocation(program, "time");
        mouseUniform = glGetUniformLocation(program, "mouse");
        resolutionUniform = glGetUniformLocation(program, "resolution");
    }

    public void use(int width, int height, float mouseX, float mouseY, float time) {
        glUseProgram(this.programID);
        glUniform2f(this.resolutionUniform, width, height);
        glUniform2f(this.mouseUniform, mouseX / width, 1.0f - mouseY / height);
        glUniform1f(this.timeUniform, time);
    }

    private int createShader(InputStream inputStream, int shaderType) throws IOException {
        int shaderID = glCreateShader(shaderType);
        glShaderSource(shaderID, readStreamToString(inputStream));
        glCompileShader(shaderID);
        int compileStatus = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (compileStatus == 0) {
            String error = glGetShaderInfoLog(shaderID, 1024);
            System.err.println(error);
            throw new RuntimeException("Shader compilation failed");
        }
        return shaderID;
    }

    private String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];


        int length;
        while ((length = inputStream.read(buffer)) != -1 ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

}
