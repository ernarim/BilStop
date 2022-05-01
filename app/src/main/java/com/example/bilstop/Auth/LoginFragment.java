package com.example.bilstop.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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


public class LoginFragment extends Fragment {
    private Context context;
    private TextView register, reset;
    private NavController navController;
    private Button login;
    private EditText email, pass;
    private FirebaseAuth auth;
    static int check = -1;

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
        this.context = context;
    }

    private void init(View view) {
        this.login = view.findViewById(R.id.btnAuthLogin);
        this.email = view.findViewById(R.id.edtAuthLoginEmail);
        this.pass = view.findViewById(R.id.edtAuthLoginPassword);
        this.reset = view.findViewById(R.id.txtAuthReset);
        this.register = view.findViewById(R.id.txtAuthRegister);
        this.auth = FirebaseAuth.getInstance();
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
                String username = LoginFragment.this.email.getText().toString();
                String password = LoginFragment.this.pass.getText().toString();
                if(login(username,password)){

                    Intent intent = new Intent(context , MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else{
                    LoginFragment.this.email.setText("");
                    LoginFragment.this.pass.setText("");
                }
            }
        });
    }

    private boolean login(String username , String password){
        if( !username.equals("") && !password.equals("") ){
            this.auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if( task.isSuccessful() ){
                        Intent intent = new Intent(context , MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        check = 0;
                    }
                    else{
                        Toast.makeText(context, "Invalid username or password!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return (check != -1);
        }
        else{
            Toast.makeText(context, "Invalid username or password!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void toRegister() {
        navController.navigate(R.id.action_loginFragment_to_registerFragment);
    }
}