package com.example.bilstop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bilstop.Auth.AuthenticationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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