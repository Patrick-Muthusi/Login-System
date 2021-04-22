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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgotPassword;
    private EditText emailText, passwordText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Button loginbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register= (TextView) findViewById(R.id.register);

        register.setOnClickListener(this);
        emailText= findViewById(R.id.email);
        passwordText= findViewById(R.id.password);

        loginbtn= findViewById(R.id.login);
        loginbtn.setOnClickListener(this);
        
        mAuth=FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressbar);

        forgotPassword=findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, Register_User.class));
                break;

            case R.id.login:
                sinIn();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;

        }
    }

    private void sinIn() {
        String email= emailText.getText().toString().trim();
        String password= passwordText.getText().toString().trim();

        if (email.isEmpty()){
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Please provide a valid Email Address");
            emailText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                  if (user.isEmailVerified()){
                      //redirect to user profile.
                      startActivity(new Intent(MainActivity.this, UserActivity.class));
                  }else{
                      user.sendEmailVerification();
                      Toast.makeText(MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                  }


              }else{
                  Toast.makeText(MainActivity.this, "Failed to login, please check your details", Toast.LENGTH_SHORT).show();
              }
            }
        });

    }
}