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

import com.example.bilstop.R;
import com.example.bilstop.Classes.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterFragment extends Fragment {
    private Button register;
    private Context context;
    private EditText email, pass, name, surname;
    private FirebaseAuth auth;
    private TextView login;
    private NavController navController;
    private NavOptions navOptions;

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

    //Initializes all private variables
    private void init(View view) {
        this.email = view.findViewById(R.id.edtAuthRegisterEmail);
        this.pass = view.findViewById(R.id.edtAuthRegisterPassword);
        this.name = view.findViewById(R.id.editTextPersonName);
        this.surname = view.findViewById(R.id.editTextPersonSurname);
        this.register = view.findViewById(R.id.btnAuthRegister);
        this.login = view.findViewById(R.id.txtAuthLogin);
        this.auth = FirebaseAuth.getInstance();
        this.navController = Navigation.findNavController(view);
        this.navOptions = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();
    }

    private void action() {
        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin();
            }
        });

        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = RegisterFragment.this.email.getText().toString();
                String password = RegisterFragment.this.pass.getText().toString();
                String name = RegisterFragment.this.name.getText().toString();
                String surname = RegisterFragment.this.surname.getText().toString();
                if (register(email, password, name, surname)) {
                    toLogin();
                } else {
                    RegisterFragment.this.email.setText("");
                    RegisterFragment.this.pass.setText("");
                }
            }
        });
    }

    private void registerToDatabase(String name, String surname, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("surname", surname);
        map.put("email", email);
        map.put("id",auth.getUid());
        map.put("profilePicture", "null");
        map.put("about", "null");
        map.put("friends", "null");
        map.put("cars", "null");
        map.put("createdRides", "null");
        map.put("rides", "null");
        map.put("activeRide", "null");
        map.put("chats", "null");
        map.put("notifications", "null");
        map.put("driverRating", "null");
        reference.setValue(map);
    }

    private boolean register(String email, String password, String name, String surname) {
        if (!email.equals("") && !password.equals("")) {
            if (password.length() > 6 && password.length() < 17) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Succesfully registered. Please verify your account.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification();
                            registerToDatabase(name, surname, email);
                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(context, "Password is either too short or too long!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(context, "Please give valid information", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toLogin() {
        navController.navigate(R.id.action_registerFragment_to_loginFragment, null, navOptions);
    }
}