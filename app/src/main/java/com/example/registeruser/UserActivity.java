package com.example.registeruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    private Button logout;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserActivity.this, MainActivity.class));
            }
        });

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId= user.getUid();

        final TextView greetings= (TextView) findViewById(R.id.textView5);
        final TextView emailtxt= (TextView) findViewById(R.id.emailinput);
        final TextView fullnames= (TextView) findViewById(R.id.namesInput);
        final TextView age= (TextView) findViewById(R.id.ageinput);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile= snapshot.getValue(Users.class);
                String fullname=userProfile.names;
                String email=userProfile.email;
                String ages=userProfile.age;

                greetings.setText("Welcome, "+fullname);
                emailtxt.setText(email);
                fullnames.setText(fullname);
                age.setText(ages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "Something wrong happened", Toast.LENGTH_SHORT).show();

            }
        });


    }
}