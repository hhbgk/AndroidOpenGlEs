package com.hhbgk.android.opengles.game2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class AirHockey2Activity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean isRenderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportEs2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(new AirHockey2Render(this));
            isRenderSet = true;
        } else {
            isRenderSet = false;
            Toast.makeText(this, "Not support OpenGL ES 2", Toast.LENGTH_LONG).show();
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRenderSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRenderSet) {
            glSurfaceView.onPause();
        }
    }
}
