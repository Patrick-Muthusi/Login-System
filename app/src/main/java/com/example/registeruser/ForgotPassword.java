package com.example.registeruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emaileditText;
    private Button resetPassword;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPassword=findViewById(R.id.button);

        emaileditText = findViewById(R.id.enteremail);
        progressBar= findViewById(R.id.progressbar);
        
        auth=FirebaseAuth.getInstance();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasword();
            }
        });
    }

    private void resetPasword() {
        String email= emaileditText.getText().toString().trim();
        if (email.isEmpty()){
            emaileditText.setError("Email is required");
            emaileditText.requestFocus();
            return;
        }
        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emaileditText.setError("Please enter a valid email address");
            emaileditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Please check your email to reset password", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this, "Something wrong happened, please try again", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
            }
        });
    }
}