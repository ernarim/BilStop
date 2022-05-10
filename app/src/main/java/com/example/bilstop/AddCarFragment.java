package com.example.bilstop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bilstop.Classes.Car;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddCarFragment extends Fragment {


    private EditText licencePlate, brand, model, color;
    private Button addButton;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private Car car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_car, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(auth.getUid()).child("car");

        licencePlate = (EditText) view.findViewById(R.id.licence_plate);
        brand = (EditText) view.findViewById(R.id.brand);
        model = (EditText) view.findViewById(R.id.model);
        color = (EditText) view.findViewById(R.id.color);

        ArrayList<String> carValues = new ArrayList<>();
        reference.child("car").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String value = dataSnapshot.getValue(String.class);
                        Log.d("value", value);
                        carValues.add(value);
                    }
                    car = new Car(carValues.get(0),carValues.get(1),carValues.get(2),carValues.get(3));
                    licencePlate.setText(car.getLicencePlate());
                    brand.setText(car.getBrand());
                    model.setText(car.getModel());
                    color.setText(car.getColor());
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
                Navigation.findNavController(view).navigate(R.id.action_addCarFragment_to_profileFragment);
            }

        });

        return view;
    }

}