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


public class adminsignup extends AppCompatActivity {

    public static final String TAG= "TAG";
    EditText adminname,adminemail,adminphone,adminpass,admincpass;
    Button btnca, btntologin;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adminsignup);

        btnca = findViewById(R.id.createaccount);
        btntologin = findViewById(R.id.login1);
        progressBar = findViewById(R.id.pro);

        adminname = findViewById(R.id.adminname);
        adminemail = findViewById(R.id.adminemail);
        adminphone = findViewById(R.id.adminphone);
        adminpass = findViewById(R.id.password1);
        admincpass = findViewById(R.id.cpassword1);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
//        if(auth.getCurrentUser()!=null)
//        {
//            startActivity(new Intent(getApplicationContext(),navadmin.class));
//            finish();
//        }
        btntologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnca.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                hideKeyBoard();
                if (admincpass.getText().toString().matches(adminpass.getText().toString()))
                {
                    if (adminname.getText().toString().matches("") || adminemail.getText().toString().matches("") ||
                        adminphone.getText().toString().matches("") || adminpass.getText().toString().matches("") ||
                        admincpass.getText().toString().matches(""))
                    {
                        Toast.makeText(adminsignup.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        final String name = adminname.getText().toString();
                        final String email = adminemail.getText().toString();
                        final String phone = adminphone.getText().toString();
                        String password = adminpass.getText().toString().trim();

                        if (password.length() < 6)
                        {
                            adminpass.setError("Password Must be >= 6 Characters");
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
                                            Toast.makeText(adminsignup.this, "Verification email has been Sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Verification email not sent " + e.getMessage());
                                        }
                                    });//Send verification link

                                    final String userId = auth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firestore.collection("Admin").document(email);
                                    Map<String, String> Admin = new HashMap<>();
                                    Admin.put("Name", name);
                                    Admin.put("Email", email);
                                    Admin.put("Phone", phone);
                                    Admin.put("Uid",userId);
                                    documentReference.set(Admin).addOnSuccessListener(new OnSuccessListener<Void>()
                                    {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(adminsignup.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(),navadmin.class);
                                            i.putExtra("email",email);
                                            startActivity(i);
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
                                    Toast.makeText(adminsignup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(adminsignup.this, "Password confirmation doesn't match Password", Toast.LENGTH_LONG).show();
                }
            } //endofonclick
        }); //endofonclicklistener
    } //endofoncreate
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