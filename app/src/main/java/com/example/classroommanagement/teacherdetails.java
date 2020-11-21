package com.example.classroommanagement;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class teacherdetails extends AppCompatActivity
{
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherdetails);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.teacherlist);

        //Query
        Query query = firebaseFirestore.collection("Admin").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("Professor");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("TEACHERS", value.toString());
            }
        });
        
        //Recyleroptions
        FirestoreRecyclerOptions<teachermodel> options = new FirestoreRecyclerOptions.Builder<teachermodel>().setQuery(query, teachermodel.class).build();

        //adapter
        adapter = new FirestoreRecyclerAdapter<teachermodel, teacherViewHolder>(options)
        {
            @NonNull
            @Override
            public teacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacherlist,parent,false);
                return new teacherViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull teacherViewHolder holder, int position, @NonNull teachermodel model)
            {
                holder.tname.setText(model.getName());
                holder.tname.setTextColor(Color.BLACK);
                holder.tdesig.setText(model.getDesignation());
                holder.tdesig.setTextColor(Color.BLACK);
                holder.phone = model.getPhone();
                holder.email = model.getEmail();

            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private class teacherViewHolder extends RecyclerView.ViewHolder
    {
        TextView tname, tdesig;
        ImageButton call, mail;
        String phone, email;
        public teacherViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tname = itemView.findViewById(R.id.tname);
            tdesig = itemView.findViewById(R.id.designation);
            call = itemView.findViewById(R.id.call);
            mail = itemView.findViewById(R.id.mail);

        }

//        private void phonecall()
//        {
//            String Dialer = num.getText().toString();
//            if (ContextCompat.checkSelfPermission(teacherdetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
//            {
//                ActivityCompat.requestPermissions(teacherdetails.this, new String[]{Manifest.permission.CALL_PHONE}, dial);
//            }
//            else
//            {
//                Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Dialer));
//                startActivity(callintent);
//            }
//        }
//
//        public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
//            super.onRequestPermissionsResult(requestCode, permission, grantResults);
//            if (requestCode == dial)
//            {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                {
//                    phonecall();
//                } else
//                {
//                    Toast.makeText(teacherdetails.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}