package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bilstop.Auth.AuthenticationActivity;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.DataPickers.AdapterActivityMyRides;
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
                Intent intent = new Intent(this , AdapterActivityMyRides.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Email is not verified!", Toast.LENGTH_SHORT).show();
                auth.signOut();
                Intent intent = new Intent(this , AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

}