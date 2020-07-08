package com.hhbgk.android.opengles.util;

import android.opengl.GLES20;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;

/**
 * Des:
 * Author: Bob
 * Date:20-7-8
 * UpdateRemark:
 */
public class ShaderHelper {
    private static String tag = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            Log.e(tag, "Could not create new shader:" + shaderCode);
            return 0;
        }

        // 有了着色器对象之后就可以调用glShaderSource()上传源代码
        GLES20.glShaderSource(shaderObjectId, shaderCode);
        // 读入代码之后就可以编译这个着色器了
        GLES20.glCompileShader(shaderObjectId);
        final int[] compileStatus = new int[1];
        // 0表示在compileStatus数组的第0个位置存储GL_COMPILE_STATUS信息
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.i(tag, "Results of compiling source:\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId));
        // 验证编译状态：
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            Log.e(tag, "Compilation of shader failed.");
            return 0;
        }
        // 如果没问题，就把这个id返回来
        return shaderObjectId;
    }

    // 链接到程序
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // 新建程序对象
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            Log.e(tag, "Could not create new program");
            return 0;
        }
        // 附上着色器
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);

        // 链接程序
        GLES20.glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        // 检查链接是否成功
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Log.i(tag, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectId));

        // 验证链接状态并返回程序对象ID
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            Log.e(tag, "Linking of program failed.");
            return 0;
        }
        return programObjectId;
    }

    /**
     * 验证OpenGL程序的对象
     * @return
     */
    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.e(tag, "Results of validating program: " + validateStatus[0] + "\nLog:"
                + GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }
}
