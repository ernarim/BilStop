package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bilstop.Auth.AuthenticationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if( user == null ){
            Intent intent = new Intent(this , AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            if( user.isEmailVerified() ){

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                DatabaseReference myRef2 = database.getReference("currentUser");

                Query condition = myRef.orderByChild("user_email").equalTo(user.getEmail());
                condition.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d: snapshot.getChildren()){
                            Users user = d.getValue(Users.class);
                            myRef2.setValue(user);
                            Log.d("Demo", "Current user is " + user.getUser_name());

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.d("Demo", "Failed to read value.", error.toException());
                    }
                });

                Intent intent = new Intent(this , BottomNavActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Email is not verified!", Toast.LENGTH_SHORT).show();
                user.sendEmailVerification();
                auth.signOut();
                Intent intent = new Intent(this , AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

}