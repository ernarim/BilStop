package com.example.bilstop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private TextView nameSurnameText, emailProfileText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameSurnameText = view.findViewById(R.id.nameSurnameText);
        emailProfileText = view.findViewById(R.id.emailProfileText);

        //Get data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("currentUser");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                Log.d("demo", "current user is " + currentUser);
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



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button btn = view.findViewById(R.id.test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent( getContext() , MainActivity.class );
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}