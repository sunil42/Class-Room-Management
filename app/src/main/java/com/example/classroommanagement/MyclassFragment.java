package com.example.classroommanagement;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyclassFragment extends Fragment {

    CardView teacher,student,details;
    private String Adminemail;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("My Class");
        View view = inflater.inflate(R.layout.fragment_myclass, container, false);
        Adminemail = getActivity().getIntent().getStringExtra("email");
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        teacher = view.findViewById(R.id.addteacher);
        student = view.findViewById(R.id.addstudent);
        details = view.findViewById(R.id.classdetails);

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(),addteacher.class);
                i.putExtra("email",Adminemail);
                startActivity(i);
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),addstudent.class);
                i.putExtra("email",Adminemail);
                startActivity(i);
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),classdetails.class));
            }
        });

    }

}