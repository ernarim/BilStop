package com.example.bilstop.DataPickers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.R;
import com.example.bilstop.RidesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterActivity extends AppCompatActivity {
    private Location locationData;

    private final ArrayList<Ride> rideDataFrom=new ArrayList<>();
    private final ArrayList<Ride> rideDataTo=new ArrayList<>();

    private Intent intent;


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

                    if(locationData!=null){
                        dataRide.setDistanceFromLocation(calculateDistance(locationData,dataRide.getDestination()));
                        Log.d("distance", String.valueOf(dataRide.getDistanceFromLocation())) ;
                    }

                    Log.d("ride",dataRide.toString());
                    if(locationData!=null){
                        if(dataRide.getDistanceFromLocation()<0.001){
                            rideDataFrom.add(dataRide);
                        }
                    }
                    else{
                        rideDataFrom.add(dataRide);
                    }
                    Log.d("kisikey", dataRide.getRideId());
                }
                Log.d("rideDataFrom", rideDataFrom.toString());
                RideDataPicker.setRidesFromBilkent(rideDataFrom);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Database Error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        Query orderByDistance2 = myRef2.orderByChild("distanceFromLocation");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride dataRide =postSnapshot.getValue(Ride.class);
                    String key= postSnapshot.getKey();
                    dataRide.setRideId(key);

                    if(locationData!=null){
                        dataRide.setDistanceFromLocation(calculateDistance(locationData,dataRide.getOrigin()));
                        Log.d("distance", String.valueOf(dataRide.getDistanceFromLocation())) ;
                    }

                    Log.d("ride",dataRide.toString());

                    if(locationData!=null){
                        if(dataRide.getDistanceFromLocation()<0.007013218885794777){
                            rideDataTo.add(dataRide);
                        }
                    }
                    else{
                        rideDataTo.add(dataRide);
                    }

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

    private double calculateDistance(Location firstLocation, Location secondLocation){
        double distance = 0;
        distance =Math.sqrt(Math.pow( Double.valueOf(firstLocation.getLocationLatitude()) - Double.valueOf(secondLocation.getLocationLatitude()),2)
                + Math.pow( Double.valueOf(firstLocation.getLocationLongitude()) - Double.valueOf(secondLocation.getLocationLongitude()),2));

        return distance;
    }

}
