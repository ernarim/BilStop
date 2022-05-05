package com.example.bilstop.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bilstop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText email;
    private Button reset;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
        action();
    }

    private void init(){
        this.email = findViewById(R.id.edtResetPassword);
        this.reset = findViewById(R.id.btnResetPassword);
        this.auth = FirebaseAuth.getInstance();
    }

    private void action(){
        this.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                if( !mail.equals("") ){
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ResetPasswordActivity.this, "Problem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}