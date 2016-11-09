package ru.climbing.itmo.itmoclimbing.graphicPart.utils;

import android.content.Context;
import android.opengl.GLES20;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by Игорь on 17.10.2016.
 */

public class ShaderUtils {
    public static int createProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Can't create openGL program");
        }
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            throw new RuntimeException("Can't link openGL program");
        }
        return programId;
    }

    public static int createShader(Context context, int type, int shaderRawId) {
        String shaderText = FIleUtils.readTextFromRaw(context, shaderRawId);
        return createShader(type, shaderText);
    }

    public static int createShader(int type, String shaderText) {
        final int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            throw new RuntimeException("Can't create openGL shader");
        }
        glShaderSource(shaderId, shaderText);
        glCompileShader(shaderId);
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderId);
            String typeStr = "";
            if (type == GL_VERTEX_SHADER) {
                typeStr = "vertex";
            } else {
                typeStr = "fragment";
            }
            throw new RuntimeException("Can't compile openGL " + typeStr + " shader");
        }
        return shaderId;
    }
}
