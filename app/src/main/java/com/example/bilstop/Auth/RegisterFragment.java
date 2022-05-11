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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RegisterFragment extends Fragment {
    private Button register;
    private Context context;
    private EditText email, pass, name, surname;
    private FirebaseAuth auth;
    private TextView login;
    private NavController navController;
    private NavOptions navOptions;
    private ArrayList<String> mails;

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
        mails = new ArrayList<>();
        mails.add("@ug.bilkent.edu.tr");
        mails.add("@bilkent.edu.tr");
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
        Users user = new Users("null", 0, email, new ArrayList<>(), auth.getUid(), name, "null", surname);
        reference.setValue(user);
    }

    private boolean register(String email, String password, String name, String surname) {
        if (!email.equals("") && !password.equals("")) {
            boolean isBilkent = mails.contains(email.substring(email.indexOf("@")));
            if (password.length() > 6 && password.length() < 17 && isBilkent) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Succesfully registered. Please verify your account.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification();
                            user.updateProfile( new UserProfileChangeRequest.Builder().setDisplayName(name + " " + surname).build());
                            registerToDatabase(name, surname, email);
                        } else {
                            Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else if( !isBilkent ){
                Toast.makeText(context, "Enter a Bilkent-related mail address", Toast.LENGTH_SHORT).show();
                return false;
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

    private void toLogin() {
        navController.navigate(R.id.action_registerFragment_to_loginFragment, null, navOptions);
    }

}