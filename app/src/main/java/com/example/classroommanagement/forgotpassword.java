package com.example.classroommanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {


    public static final String TAG= "TAG";
    EditText email;
    Button btntologin, send;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgotpassword);

        email = findViewById(R.id.resetpasswordemail);
        btntologin = findViewById(R.id.login2);
        send = findViewById(R.id.send);

        btntologin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                hideKeyBoard();
                String resetemail = email.getText().toString();
                if(TextUtils.isEmpty(resetemail))
                {
                    email.setError("Email is Required.");
                    return;
                }
                else
                {
                    auth.sendPasswordResetEmail(resetemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful())
                          {
                             Log.d(TAG, "Email sent.");
                             Toast.makeText(getApplicationContext(), "Password reset Link has been Sent to your e-mail address.", Toast.LENGTH_SHORT).show();
                          }
                          else
                          {
                            Toast.makeText(forgotpassword.this, "Error ! Reset password Link Can not be sent." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                          }
                       }
                    });
                }
            }
        });
    }//endofOncreate

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