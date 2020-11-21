package com.example.classroommanagement;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addstudent extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText studentenroll, studentname, studentemail, studentphone, studentpass, studentcpass;
    Spinner coursee, departmentt;
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
        setContentView(R.layout.activity_addstudent);

        btnregister = findViewById(R.id.Register);
        progressBar = findViewById(R.id.pro);
        coursee = findViewById(R.id.course);
        departmentt = findViewById(R.id.department);

        studentenroll = findViewById(R.id.studentenroll);
        studentname  = findViewById(R.id.studentname);
        studentemail = findViewById(R.id.studentemail);
        studentphone = findViewById(R.id.studentphone);
        studentpass = findViewById(R.id.password);
        studentcpass = findViewById(R.id.cpassword);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                hideKeyBoard();
                if (studentcpass.getText().toString().matches(studentpass.getText().toString()))
                {
                    if (studentenroll.getText().toString().matches("") || studentname.getText().toString().matches("")||
                        studentemail.getText().toString().matches("") || studentphone.getText().toString().matches("")||
                        studentpass.getText().toString().matches("") || studentcpass.getText().toString().matches(""))
                    {
                        Toast.makeText(addstudent.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        final String sname = studentname.getText().toString();
                        final String semail = studentemail.getText().toString();
                        final String phone = studentphone.getText().toString();
                        final String enrollment = studentenroll.getText().toString();
                        String password = studentpass.getText().toString().trim();
                        final String course = coursee.getSelectedItem().toString();
                        final String dept = departmentt.getSelectedItem().toString();

                        if (password.length() < 6)
                        {
                            studentpass.setError("Password Must be >= 6 Characters");
                            return;
                        }

                        progressBar.setVisibility(View.VISIBLE);
                        auth.createUserWithEmailAndPassword(semail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final FirebaseUser fuser = auth.getCurrentUser();
                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(addstudent.this, "Verification email has been Sent.", Toast.LENGTH_SHORT).show();
                                            userId = fuser.getUid();

                                            Log.d("Email Intent: ", getIntent().getStringExtra("email"));
                                            DocumentReference documentReference =  firestore.collection("Admin")
                                                                                            .document(getIntent().getStringExtra("email").toString())
                                                                                            .collection("Student").document(semail);
                                            Map<String, String> Student = new HashMap<>();
                                            Student.put("Name", sname);
                                            Student.put("Email", semail);
                                            Student.put("Phone", phone);
                                            Student.put("Course",course);
                                            Student.put("Department",dept);
                                            Student.put("Enrollment Number", enrollment);
                                            Student.put("Uid",userId);
                                            documentReference.set(Student).addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(addstudent.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
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
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Verification email not sent " + e.getMessage());
                                        }
                                    });//Send verification link

//                                    userId = fuser.getUid();
//
//                                    DocumentReference documentReference =  firestore.collection("Admin")
//                                            .document(getIntent().getStringExtra("email").toString())
//                                            .collection("Student").document(semail);
//                                    Map<String, String> Student = new HashMap<>();
//                                    Student.put("Name", sname);
//                                    Student.put("Email", semail);
//                                    Student.put("Phone", phone);
//                                    Student.put("Course",course);
//                                    Student.put("Department",dept);
//                                    Student.put("Enrollment Number", enrollment);
//                                    Student.put("Uid",userId);
//                                    documentReference.set(Student).addOnSuccessListener(new OnSuccessListener<Void>()
//                                    {
//                                        @Override
//                                        public void onSuccess(Void aVoid)
//                                        {
//                                            progressBar.setVisibility(View.GONE);
//                                            Toast.makeText(addstudent.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(), navadmin.class));
//                                            finish();
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener()
//                                    {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e)
//                                        {
//                                            progressBar.setVisibility(View.GONE);
//                                            Log.d(TAG, "onFailure: " + e.getMessage());
//                                        }
//                                    });
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(addstudent.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(addstudent.this, "Password confirmation doesn't match Password", Toast.LENGTH_LONG).show();
                }





            }//endofOnClick
        });
    }//endofOncreate
    private void hideKeyBoard() {
        if (getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}