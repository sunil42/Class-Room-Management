package com.example.classroommanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class studentdetails extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentdetails);

        recyclerView = findViewById(R.id.studentlist);

        String[] studentname = {"Jeevan","Ayush","Rishabh","Rahul","Himanshu","Mohit","Manish","Jeevan","Ayush","Rishabh","Rahul","Himanshu","Mohit","Manish"};
        String[] enroll= {"01090102018","02090102018","03090102018","04090102018","05090102018","06090102018","07090102018","01090102018","02090102018","03090102018","04090102018","05090102018","06090102018","07090102018"};

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new studentadapter(studentname, enroll));
    }
}