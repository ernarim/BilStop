package com.example.bilstop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bilstop.Classes.Car;
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

import java.util.ArrayList;

public class AddCarFragment extends Fragment {


    private EditText licencePlate, brand, model, color;
    private Button addButton;
    private ImageView carPP;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private Uri filePath;

    private Car car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_car, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(auth.getUid()).child("car");

        licencePlate = view.findViewById(R.id.licence_plate);
        brand = view.findViewById(R.id.brand);
        model = view.findViewById(R.id.model);
        color = view.findViewById(R.id.color);
        carPP = view.findViewById(R.id.imvCarPP);

        ArrayList<String> carValues = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String value = dataSnapshot.getValue(String.class);
                        System.out.println(value);
                        carValues.add(value);
                    }

                    if(carValues.size() == 5){
                        car = new Car(carValues.get(0), carValues.get(1), carValues.get(2), carValues.get(3), carValues.get(4));
                        licencePlate.setText(car.getColor());
                        brand.setText(car.getBrand());
                        model.setText(car.getModel());
                        color.setText(car.getLicencePlate());
                        if( !car.getPp().equals("null") ){
                            Picasso.get().load(car.getPp()).into(carPP);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("Licence Plate").setValue(licencePlate.getText().toString());
                reference.child("Brand").setValue(brand.getText().toString());
                reference.child("Model").setValue(model.getText().toString());
                reference.child("Color").setValue(color.getText().toString());

                reference.orderByKey().equalTo("PP").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }
                        else{
                            reference.child("PP").setValue("null");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                StorageReference stref = FirebaseStorage.getInstance().getReference().child("carProfilePictures").child(auth.getUid() + ".jpg");
                if (filePath != null) {
                    stref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    reference.child("PP").setValue(task.getResult().toString());
                                    Navigation.findNavController(view).navigate(R.id.action_addCarFragment_to_profileFragment);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();//test
                            reference.child("PP").setValue("null");
                            Navigation.findNavController(view).navigate(R.id.action_addCarFragment_to_profileFragment);
                        }
                    });
                }
                else {
                    Navigation.findNavController(view).navigate(R.id.action_addCarFragment_to_profileFragment);
                }

            }

        });

        carPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 5);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            Picasso.get().load(filePath).into(carPP);
        }
    }

}