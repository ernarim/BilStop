package com.example.bilstop.Auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

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
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    private Context context;
    private TextView login;
    private FirebaseAuth auth;
    private NavController navController;
    private NavOptions navOptions;
    private EditText email, pass;
    private Button register;
    public  int check = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
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
        this.context = context;
    }

    private void init(View view) {
        this.email = view.findViewById(R.id.edtAuthRegisterEmail);
        this.pass = view.findViewById(R.id.edtAuthRegisterPassword);
        this.register = view.findViewById(R.id.btnAuthRegister);
        this.login = view.findViewById(R.id.txtAuthLogin);
        this.auth = FirebaseAuth.getInstance();
        this.navController = Navigation.findNavController(view);
        this.navOptions = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();
    }

    private void action(){
        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin();
            }
        });

        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = RegisterFragment.this.email.getText().toString();
                String password = RegisterFragment.this.pass.getText().toString();
                if(register(username,password)){
                    toLogin();
                }
                else{
                    RegisterFragment.this.email.setText("");
                    RegisterFragment.this.pass.setText("");
                }
            }
        });
    }

    private boolean register(String username, String password) {
        if (!username.equals("") && !password.equals("")) {
            if (password.length() > 6 && password.length() < 17) {
                auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Succesfully registered. Please verify your account.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else {
                Toast.makeText(context, "Password is either too short or too long!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(context, "Please give valid information", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toLogin(){
        navController.navigate(R.id.action_registerFragment_to_loginFragment, null, navOptions);
    }
}