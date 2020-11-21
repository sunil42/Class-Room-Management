package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class studentlogin extends AppCompatActivity {

    EditText studentemail, studentpassword;
    ProgressBar progressBar;
    Button btnlogin, btnfp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_studentlogin);

        auth = FirebaseAuth.getInstance();
        btnlogin = findViewById(R.id.login);
        btnfp = findViewById(R.id.forgotpassword);

        studentemail = findViewById(R.id.email);
        studentpassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.pro);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                hideKeyBoard();
                String email = studentemail.getText().toString().trim();
                String password = studentpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    studentemail.setError("Email is Required.");
                    studentpassword.setError("Password is Required.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                // authenticate the user
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(studentlogin.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),navstudent.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(studentlogin.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        btnfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),forgotpassword.class));
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
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}