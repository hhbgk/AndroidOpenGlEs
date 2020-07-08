package com.hhbgk.android.opengles.first;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Des:
 * Author: Bob
 * Date:20-7-8
 * UpdateRemark:
 */
public class FirstRender implements GLSurfaceView.Renderer {
    private String tag = getClass().getSimpleName();

    /**
     * 当surface被创建时会被调用此方法；当设备被唤醒或从其他activity切换回来时也会被调用
     * @param gl ES 1.0才用到
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //RGBA:红、绿、蓝、半透明度/透明度
        GLES20.glClearColor(1.0f, 0.0f, 0.0f,0.0f);
        Log.e(tag, "onSurfaceCreated");
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
    }
}
