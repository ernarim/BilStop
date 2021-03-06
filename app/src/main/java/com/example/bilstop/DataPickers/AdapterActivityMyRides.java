package com.example.bilstop.DataPickers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bilstop.Auth.AuthenticationActivity;
import com.example.bilstop.BottomNavActivity;
import com.example.bilstop.Classes.Car;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.R;
import com.example.bilstop.RidesActivity;
import com.example.bilstop.RidesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class AdapterActivityMyRides extends AppCompatActivity {

    private ArrayList<Ride> rideDataMy=new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef3 = database.getReference("myRides").child(FirebaseAuth.getInstance().getUid());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_screen);



        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rideDataMy.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Ride dataRide =postSnapshot.getValue(Ride.class);
                    String key= postSnapshot.getKey();
                    dataRide.setRideId(key);
                    Log.d("rideId", dataRide.getRideId());
                    rideDataMy.add(dataRide);
                }
                RideDataPicker.setMyRides(rideDataMy);
                Intent intent = new Intent(AdapterActivityMyRides.this, BottomNavActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}
