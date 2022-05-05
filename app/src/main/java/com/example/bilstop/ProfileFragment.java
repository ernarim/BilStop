package com.example.bilstop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bilstop.Classes.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private TextView nameSurnameText, emailProfileText, aboutMeText;
    private ImageButton editImageButton;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameSurnameText = view.findViewById(R.id.nameSurnameText);
        emailProfileText = view.findViewById(R.id.emailProfileText);
        editImageButton = view.findViewById(R.id.editImageButton);
        aboutMeText = view.findViewById(R.id.aboutMeText);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(auth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                Log.d("demo", "Current user is " + currentUser);
                nameSurnameText.setText(currentUser.getName());
                emailProfileText.setText(currentUser.getEmail());
                aboutMeText.setText(currentUser.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_profileEditFragment);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOGIN","login");
                auth.signOut();
                Intent intent = new Intent( getContext() , MainActivity.class );
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}