package com.example.bilstop.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bilstop.MainActivity;
import com.example.bilstop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private Context mContext;
    private TextView register, reset;
    private NavController navController;
    private Button login;
    private EditText mEmail, mPassword;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        action();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    private void init(View view) {
        this.login = view.findViewById(R.id.btnAuthLogin);
        this.mEmail = view.findViewById(R.id.edtAuthLoginEmail);
        this.mPassword = view.findViewById(R.id.edtAuthLoginPassword);
        this.reset = view.findViewById(R.id.txtAuthReset);
        this.register = view.findViewById(R.id.txtAuthRegister);
        this.mAuth = FirebaseAuth.getInstance();
        this.navController = Navigation.findNavController(view);
    }

    private void action(){
        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegister();
            }
        });

        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                login(username,password);
            }
        });

        this.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toReset();
            }
        });
    }

    private void login(String username , String password){
        if( !username.equals("") && !password.equals("") ){
            mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete:success");
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        Log.w(TAG, "onComplete:failed", task.getException());
                        Toast.makeText(mContext, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                        mPassword.setText("");
                    }
                }
            });
        }
        else{
            Toast.makeText(mContext, "Invalid username or password!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toRegister() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void toReset(){
        Intent intent = new Intent(mContext, ResetPasswordActivity.class);
        startActivity(intent);
    }
}