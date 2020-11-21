package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class updateadminprofile extends AppCompatActivity
{
    EditText name, email, phone;
    Button update, cancel;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateadminprofile);
        update = findViewById(R.id.updprofile);
        cancel = findViewById(R.id.cnl);

        name = findViewById(R.id.aname);
        email = findViewById(R.id.aemail);
        phone = findViewById(R.id.aphone);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseFirestore.collection("Admin").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
         .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                    name.setText(documentSnapshot.getString("Name"));
                    phone.setText(documentSnapshot.getString("Phone"));
                    email.setText(documentSnapshot.getString("Email"));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                hideKeyBoard();
                progressDialog.show();
                final String adminname = name.getText().toString().trim();
                final String adminemail = email.getText().toString().trim();
                final String adminphone = phone.getText().toString().trim();

                if(adminname.isEmpty() || adminemail.isEmpty() || adminphone.isEmpty())
                {
                    progressDialog.dismiss();
                    Toast.makeText(updateadminprofile.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    firebaseFirestore.collection("Admin").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                    .update("Name",adminname,"Email",adminemail,"Phone",adminphone)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            progressDialog.dismiss();
                            if(task.isComplete())
                            {
                                Toast.makeText(updateadminprofile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(updateadminprofile.this, "Please try again later", Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(getApplicationContext(), navadmin.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(updateadminprofile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), navadmin.class));
                finish();
            }
        });

    }
    private void hideKeyBoard() {
        if (getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}