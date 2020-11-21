package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class addteacher extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText teachername, teacheremail, teacherphone, teacherpass,teachercpass;
    Spinner department, designation;
    FirebaseAuth auth;
    String userId;
    Button btnregister;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addteacher);

        btnregister = findViewById(R.id.Register);
        progressBar = findViewById(R.id.pro);
        department = findViewById(R.id.department);
        designation = findViewById(R.id.designation);

        teachername = findViewById(R.id.teachername);
        teacheremail = findViewById(R.id.teacheremail);
        teacherphone = findViewById(R.id.teacherphone);
        teacherpass = findViewById(R.id.password);
        teachercpass = findViewById(R.id.cpassword);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                hideKeyBoard();
                if (teachercpass.getText().toString().matches(teacherpass.getText().toString()))
                {
                    if (teachername.getText().toString().matches("") || teacheremail.getText().toString().matches("") ||
                        teacherphone.getText().toString().matches("") || teacherpass.getText().toString().matches("") ||
                        teachercpass.getText().toString().matches(""))
                    {
                        Toast.makeText(addteacher.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        final String name = teachername.getText().toString();
                        final String email = teacheremail.getText().toString();
                        final String phone = teacherphone.getText().toString();
                        String password = teacherpass.getText().toString().trim();
                        final String dept = department.getSelectedItem().toString();
                        final String desig = designation.getSelectedItem().toString();

                        if (password.length() < 6)
                        {
                            teacherpass.setError("Password Must be >= 6 Characters");
                            return;
                        }

                        progressBar.setVisibility(View.VISIBLE);
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    FirebaseUser fuser = auth.getCurrentUser();
                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(addteacher.this, "Verification email has been Sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Verification email not sent " + e.getMessage());
                                        }
                                    });//Send verification link

                                    userId = fuser.getUid();
//                                    Log.d("email",getIntent().getStringExtra("email"));

                                    DocumentReference documentReference =  firestore.collection("Admin")
                                            .document(getIntent().getStringExtra("email").toString())
                                            .collection("Professor").document(email);
                                    Map<String, String> Professor = new HashMap<>();
                                    Professor.put("Name", name);
                                    Professor.put("Email", email);
                                    Professor.put("Phone", phone);
                                    Professor.put("Desgination",desig);
                                    Professor.put("Department",dept);
                                    Professor.put("Uid",userId);
                                    documentReference.set(Professor).addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(addteacher.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), navadmin.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Log.d(TAG, "onFailure: " + e.getMessage());
                                        }
                                    });
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(addteacher.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(addteacher.this, "Password confirmation doesn't match Password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void hideKeyBoard()
    {
        if (getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}