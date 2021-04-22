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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register_User extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText name, age, emailText, password;
    private TextView banner, registerUser;
    ProgressBar progressBar;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__user);
        mAuth = FirebaseAuth.getInstance();
        name=(EditText) findViewById(R.id.names);
        age=findViewById(R.id.age);
        emailText=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        progressBar=findViewById(R.id.progressbar);
        banner=findViewById(R.id.banner);
        banner.setOnClickListener(this);

        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
        }

    }

    private void registerUser() {
        String names= name.getText().toString().trim();
        String ages=age.getText().toString().trim();
        String emails=emailText.getText().toString().trim();
        String passwords=password.getText().toString().trim();

        if (names.isEmpty()){
            name.setError("Full names are required");
            name.requestFocus();
            return;
        }
        if (ages.isEmpty()){
            age.setError("Age is required");
            age.requestFocus();
            return;
        }
        if (emails.isEmpty()){
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        }
        if (passwords.isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
            emailText.setError("Please provide a valid email address");
            emailText.requestFocus();
            return;

        }
        if (passwords.length() <6){
            password.setError("Password should be six characters and above");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Users user=new Users(names, ages, emails);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                    Toast.makeText(Register_User.this, "Registration Successiful", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                                else{
                                        Toast.makeText(Register_User.this, "Registration did not succeed try again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(Register_User.this, "Registration did not succeed", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        }

                });


    }
}