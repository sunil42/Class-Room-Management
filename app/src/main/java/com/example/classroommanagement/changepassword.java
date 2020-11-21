package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changepassword extends AppCompatActivity {
    FirebaseAuth auth;
    Button changepassword;
    ProgressBar progressBar;
    EditText old,newp,cnewp;
    FirebaseUser firebaseuser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_changepassword);
        old= findViewById(R.id.oldpass);
        newp = findViewById(R.id.newpass);
        cnewp = findViewById(R.id.cnewpass);
        changepassword = findViewById(R.id.done);
        progressBar = findViewById(R.id.pro);

        auth = FirebaseAuth.getInstance();
        firebaseuser = auth.getCurrentUser();

        changepassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                hideKeyBoard();
                if (cnewp.getText().toString().matches(newp.getText().toString()))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    final FirebaseUser user;
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    final String email = user.getEmail();
                    String oldpassword=old.getText().toString();
                    final String newpassword=newp.getText().toString();

                    AuthCredential credential = EmailAuthProvider.getCredential(email,oldpassword);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                user.updatePassword(newpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(changepassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),MyclassFragment.class));
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(changepassword.this, "Error", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(changepassword.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(changepassword.this, "Password confirmation doesn't match Password", Toast.LENGTH_LONG).show();
                }
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