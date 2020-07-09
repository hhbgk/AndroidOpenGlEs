package com.hhbgk.android.opengles.game2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.hhbgk.android.opengles.R;
import com.hhbgk.android.opengles.util.ShaderHelper;
import com.hhbgk.android.opengles.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Des:
 * Author: Bob
 * Date:20-7-9
 * UpdateRemark:
 */
public class AirHockey2Render implements GLSurfaceView.Renderer {
    private String tag = getClass().getSimpleName();
    private static final int POSITION_COMPONENT_COUNT = 2;
    //告诉OpenGL把颜色作为一个顶点属性读入
    private static final int COLOR_COMPONENT_COUNT = 3;
    // 一个java float占32位，也就是4个字节
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private Context context;
    private int program;
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;

    public AirHockey2Render(Context context) {
        this.context = context;
        // 定义三角形的时候总是以逆时针的顺序排列顶点，这称为卷曲顺序（winding order）。
        // 在任何地方都使用这种顺序可以优化性能
        float[] tableVerticesWithTriangles = {
                //Triangle Fan
                0f,    0f,    1f,   1f,  1f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f,  0f, 1f, 0f, 0f,

                // Mallets
                0f, -0.25f, 0f, 0f, 1f,
                0f, 0.25f,  1f, 0f, 0f
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    /**
     * 当surface被创建时会被调用此方法；当设备被唤醒或从其他activity切换回来时也会被调用
     * @param gl ES 1.0才用到
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //RGBA:红、绿、蓝、半透明度/透明度
        GLES20.glClearColor(0.0f, 0.0f, 0.0f,0.0f);
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader2);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader2);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        ShaderHelper.validateProgram(program);
        GLES20.glUseProgram(program);

        // 调用glGetUniformLocation获取uniform位置。
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        // 获取属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        // 关联属性与顶点数据的数组
        // 移动缓冲区指针的位置到开头处
        vertexData.position(0);
        // 告诉OpenGL到vertexData里面去寻找顶点数据
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

         vertexData.position(POSITION_COMPONENT_COUNT);
         GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
                 false, STRIDE, vertexData);
         GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    /**
     * 每次surface尺寸变化时
     * @param gl ES 1.0才用到
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口大小，即告诉OpenGL surface大小
        GLES20.glViewport(0, 0, width, height);
        Log.e(tag, "onSurfaceChanged");
    }

    /**
     * 当绘制每一帧时，此方法会被调用
     * @param gl ES 1.0才用到
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //清除屏幕，这会擦除屏幕上的所有颜色，并用之前的glClearColor()调用定义的颜色填充整个屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 绘制桌子背景边框
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
        // 绘制桌子中间线
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
        // 绘制木槌
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);
        // 绘制木槌
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}
