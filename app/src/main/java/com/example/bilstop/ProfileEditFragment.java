package com.example.bilstop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bilstop.Classes.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileEditFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private String profilePictureUriString;//from firebase
    private Uri filePath;//from device

    private Button saveButton, reset;
    private ImageView edit;
    private CircleImageView image;
    private EditText nameProfileEdit, surnameProfileEdit, emailProfileEdit, aboutMeProfileEdit, carsProfileEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        init(view);
        getInfo();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", nameProfileEdit.getText().toString());
                userInfo.put("surname", surnameProfileEdit.getText().toString());
                userInfo.put("email", emailProfileEdit.getText().toString());
                userInfo.put("about", aboutMeProfileEdit.getText().toString());


                StorageReference stref = storageReference.child("userProfilePictures").child( auth.getUid() + ".jpg");
                if (filePath != null) {
                    stref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            userInfo.put("profilePicture", task.getResult().toString());
                                            reference.updateChildren(userInfo);
                                            Navigation.findNavController(view).navigate(R.id.action_profileEditFragment_to_profileFragment);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();//test
                                    reference.updateChildren(userInfo);
                                    Navigation.findNavController(view).navigate(R.id.action_profileEditFragment_to_profileFragment);
                                }
                            });
                } else {
                    reference.updateChildren(userInfo);
                    Navigation.findNavController(view).navigate(R.id.action_profileEditFragment_to_profileFragment);
                }


            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Please check your mail to reset your password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 5);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void action() {

    }

    private void init(View view) {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(auth.getUid());

        saveButton = view.findViewById(R.id.saveButton);
        nameProfileEdit = view.findViewById(R.id.nameProfileEdit);
        surnameProfileEdit = view.findViewById(R.id.surnameProfileEdit);
        emailProfileEdit = view.findViewById(R.id.emailProfileEdit);
        aboutMeProfileEdit = view.findViewById(R.id.aboutMeProfileEdit);
        image = view.findViewById(R.id.civEditProfilePP);
        reset = view.findViewById(R.id.btnProfileEditReset);
        edit = view.findViewById(R.id.imgEditProfilePP);
    }

    private void getInfo() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                nameProfileEdit.setText(currentUser.getName());
                surnameProfileEdit.setText(currentUser.getSurname());
                emailProfileEdit.setText(currentUser.getEmail());
                aboutMeProfileEdit.setText(currentUser.getAbout());
                profilePictureUriString = currentUser.getProfilePicture();
                if (!currentUser.getProfilePicture().equals("null")) {
                    Picasso.get().load(currentUser.getProfilePicture()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            Picasso.get().load(filePath).into(image);
        }
    }
}