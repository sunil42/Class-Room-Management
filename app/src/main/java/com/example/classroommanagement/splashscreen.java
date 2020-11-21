package com.example.classroommanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class splashscreen extends AppCompatActivity {
    Animation lines;
    View orange,white,green;
    private static int Timeout = 5500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        orange = findViewById(R.id.orange);
        white = findViewById(R.id.white);
        green = findViewById(R.id.green);

        lines = AnimationUtils.loadAnimation(this,R.anim.lines);//toloadanimations

        orange.setAnimation(lines);
        white.setAnimation(lines);
        green.setAnimation(lines);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(getApplicationContext(),Category.class);
                startActivity(i);
                finish();
            }
        },Timeout);
    }
}