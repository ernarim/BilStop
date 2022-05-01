package com.example.bilstop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.DataPickers.RideDataPicker;
import com.example.bilstop.Adapters.RidesRVAdapter;
import com.example.bilstop.Classes.Ride;

import java.io.Serializable;
import java.util.ArrayList;

public class RidesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Ride> rides;
    private RidesRVAdapter adapter;
    private Location locationData;

    private TextView textViewRides;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        locationData = (Location) getIntent().getSerializableExtra("object");
        Log.d("from", String.valueOf(getIntent().getSerializableExtra("buttonType")));

        textViewRides = findViewById(R.id.textViewRides);

        Serializable buttonType = getIntent().getSerializableExtra("buttonType");
        Serializable allList = getIntent().getSerializableExtra("allList");

        if(buttonType!=null){
            if(allList.equals("true")){
                textViewRides.setText("All Rides:");
            }
            else{
                if(buttonType.equals("from")){
                    textViewRides.setText("Bilkent - " + locationData.getLocationName() + " Rides");
                }
                else if(buttonType.equals("to")){
                    textViewRides.setText(locationData.getLocationName() + " - Bilkent Rides");
                }
            }
        }

        rides =new ArrayList<Ride>(RideDataPicker.getRidesFromBilkent());

        Log.d("ridesActivity", rides.toString());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RidesRVAdapter(this, rides);
        recyclerView.setAdapter(adapter);

    }

}