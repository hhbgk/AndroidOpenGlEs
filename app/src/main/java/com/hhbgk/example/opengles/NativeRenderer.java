package com.hhbgk.example.opengles;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class NativeRenderer implements GLSurfaceView.Renderer {
    private Context context;

    NativeRenderer(Context context) {
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        CodecWrapper.init();
        InputStream inputStream = context.getResources().openRawResource(R.raw.vga);
        byte[] b = null;
        try {
            b = new byte[inputStream.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (b != null){
            try {
                inputStream.read(b);
                CodecWrapper.decode(b, 640, 480);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        CodecWrapper.changed(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        CodecWrapper.render();
    }
}
