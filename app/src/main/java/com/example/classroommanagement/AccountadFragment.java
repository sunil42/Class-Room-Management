package com.example.classroommanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class AccountadFragment extends Fragment {

    TextView name,email,phone;
    FirebaseFirestore fb;
    FirebaseAuth auth;
    Button logout;
    String userId;

    public AccountadFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Account");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accountad, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.adminname);
        email = view.findViewById(R.id.adminmail);
        phone = view.findViewById(R.id.adminphone);

        logout = view.findViewById(R.id.logout);
        fb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        DocumentReference documentReference = fb.collection("Admin").document(getActivity().getIntent().getStringExtra("email").toString());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    name.setText(documentSnapshot.getString("Name"));
                    phone.setText(documentSnapshot.getString("Phone"));
                    email.setText(documentSnapshot.getString("Email"));
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(),adminlogin.class));
                getActivity().finish();
            }
        });
    }
}