package com.example.bilstop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;


public class ProfileEditFragment extends Fragment {

    private Button saveButton;
    private ImageView edit;
    private EditText nameProfileEdit, surnameProfileEdit, emailProfileEdit, aboutMeProfileEdit, carsProfileEdit;
    private Users currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        saveButton = view.findViewById(R.id.saveButton);
        nameProfileEdit = view.findViewById(R.id.nameProfileEdit);
        surnameProfileEdit = view.findViewById(R.id.surnameProfileEdit);
        emailProfileEdit = view.findViewById(R.id.emailProfileEdit);
        aboutMeProfileEdit = view.findViewById(R.id.aboutMeProfileEdit);
        edit = view.findViewById(R.id.edit);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("currentUser");
        DatabaseReference myRef2 = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(Users.class);
                Log.d("demo", "Current user is " + currentUser);
                nameProfileEdit.setText(currentUser.getUser_name());
                surnameProfileEdit.setText(currentUser.getUser_surname());
                emailProfileEdit.setText(currentUser.getUser_email());
                aboutMeProfileEdit.setText(currentUser.getUser_about());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("user_name", nameProfileEdit.getText().toString());
                userInfo.put("user_surname", surnameProfileEdit.getText().toString());
                userInfo.put("user_email", emailProfileEdit.getText().toString());
                userInfo.put("user_about", aboutMeProfileEdit.getText().toString());

                myRef2.child(currentUser.getUser_key()).updateChildren(userInfo);
                myRef.updateChildren(userInfo);
                Navigation.findNavController(view).navigate(R.id.action_profileEditFragment_to_profileFragment);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}