package com.example.bilstop.DataPickers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.R;
import com.example.bilstop.RidesActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class AdapterActivity extends AppCompatActivity {
    private Location locationData;

    private final ArrayList<Ride> rideDataFrom=new ArrayList<>();
    private final ArrayList<Ride> rideDataTo=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rides);

        if(getIntent().getSerializableExtra("object")!=null){
            locationData = (Location) getIntent().getSerializableExtra("object");
            Log.d("demo", locationData.toString());
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("ridesFromBilkent");
        DatabaseReference myRef2 = database.getReference().child("ridesToBilkent");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride dataRide =postSnapshot.getValue(Ride.class);
                    String key= postSnapshot.getKey();
                    dataRide.setRideId(key);
                    Log.d("ride",dataRide.toString());
                    rideDataFrom.add(dataRide);

                    Log.d("kisikey", dataRide.getRideId());
                }
                Log.d("rideDataFrom", rideDataFrom.toString());
                RideDataPicker.setRidesFromBilkent(rideDataFrom);

                Intent intent = new Intent(AdapterActivity.this, RidesActivity.class);
                intent.putExtra("object", locationData);
                intent.putExtra("buttonType", getIntent().getSerializableExtra("buttonType"));
                intent.putExtra("allList", getIntent().getSerializableExtra("allList"));
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Database Error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride dataRide =postSnapshot.getValue(Ride.class);
                    String key= postSnapshot.getKey();
                    dataRide.setRideId(key);
                    Log.d("ride",dataRide.toString());
                    rideDataTo.add(dataRide);

                    Log.d("kisikey", dataRide.getRideId());
                }
                Log.d("rideDataTo", rideDataTo.toString());
                RideDataPicker.setRidestoBilkent(rideDataTo);

                Intent intent = new Intent(AdapterActivity.this, RidesActivity.class);
                intent.putExtra("object", locationData);
                intent.putExtra("buttonType", getIntent().getSerializableExtra("buttonType"));
                intent.putExtra("allList", getIntent().getSerializableExtra("allList"));
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
