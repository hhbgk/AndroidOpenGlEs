package com.hhbgk.android.opengles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hhbgk.android.opengles.first.FirstOpenGlEsActivity;
import com.hhbgk.android.opengles.game.AirHockeyActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickFirstOpenGLES(View view) {
        startActivity(new Intent(this, FirstOpenGlEsActivity.class));
    }

    public void onClickAirHockey(View view) {
        startActivity(new Intent(this, AirHockeyActivity.class));
    }
}
