package com.example.classroommanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;


public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category);
    }

    public void admin(View login){
        startActivity(new Intent(getApplicationContext(),adminlogin.class));
    }
    public void teacher(View login){
        startActivity(new Intent(getApplicationContext(),teacherlogin.class));
    }
    public void student(View login){
        startActivity(new Intent(getApplicationContext(),studentlogin.class));
    }
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Category.this.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id)
            {
                dialogInterface.cancel();
            }
        }).show();
    }
}