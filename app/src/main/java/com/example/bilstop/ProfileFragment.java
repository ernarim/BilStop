package com.example.bilstop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView nameSurnameText;
    private TextView emailProfileText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameSurnameText = view.findViewById(R.id.nameSurnameText);
        emailProfileText = view.findViewById(R.id.emailProfileText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("currentUser");
        myRef.setValue(new Users("Eren", "Arım", "erenarim7b@gmail.com","1234"));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users currentUser = snapshot.getValue(Users.class);

                Log.e("mesaj", "user" + currentUser.getUser_name());
                nameSurnameText.setText(currentUser.getUser_name() + " " + currentUser.getUser_surname());
                emailProfileText.setText(currentUser.getUser_email());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}