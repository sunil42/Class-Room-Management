package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class adminlogin extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    Button btnlogin, btnfp, btnsignup;
    EditText adminemail, adminpassword;
    ProgressBar progressBar;
    CheckBox checkBox;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adminlogin);

        btnlogin = findViewById(R.id.login);
        btnfp = findViewById(R.id.forgotpassword);
        btnsignup = findViewById(R.id.signup);

        adminemail = findViewById(R.id.email);
        adminpassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.pro);
        checkBox = findViewById(R.id.check);
        auth = FirebaseAuth.getInstance();
//        boolean isChecked = false;
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putBoolean("isChecked", isChecked);
//                editor.commit();
//            }
//        });
//
//        SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
//        isChecked = settings1.getBoolean("isChecked", false);
//
//        if (isChecked) {
//            Intent i = new Intent(adminlogin.this, navadmin.class);
//            startActivity(i);
//        } else {
//            Intent i = new Intent(adminlogin.this, adminlogin.class);
//            startActivity(i);
//        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyBoard();
                final String email = adminemail.getText().toString().trim();
                final String password = adminpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    adminemail.setError("Email is Required.");
                    adminpassword.setError("Password is Required.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                // authenticate the user
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(adminlogin.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(),navadmin.class);
                                    i.putExtra("email",email);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(adminlogin.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });

            }//onclick
        });

        btnfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),forgotpassword.class));
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),adminsignup.class));
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
