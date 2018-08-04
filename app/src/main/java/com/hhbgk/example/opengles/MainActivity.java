package com.hhbgk.example.opengles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    String tag = getClass().getSimpleName();
    GLSurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLSurfaceView = findViewById(R.id.gl_surface_view);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(2);

            // Set the native renderer to our demo renderer,defined below.
            mGLSurfaceView.setRenderer(new NativeRenderer(this));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(tag, "onStart ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(tag, "onRestart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(tag, "onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(tag, " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(tag, " onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(tag, " onDestroy");
        CodecWrapper.destroy();
    }
}
