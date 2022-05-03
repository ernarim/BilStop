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
    private ArrayList<Ride> ridesFromBilkent;
    private ArrayList<Ride> ridestoBilkent;

    private RidesRVAdapter adapter;
    private Location locationData;

    private TextView textViewRides;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);

        if(getIntent().getSerializableExtra("object")!=null){
            locationData = (Location) getIntent().getSerializableExtra("object");
        }

        Log.d("from", String.valueOf(getIntent().getSerializableExtra("buttonType")));

        textViewRides = findViewById(R.id.textViewRides);
        Log.d("datapicker", RideDataPicker.getRidesFromBilkent().toString());
        Log.d("datapicker", RideDataPicker.getRidestoBilkent().toString());

        ridesFromBilkent = new ArrayList<Ride>(RideDataPicker.getRidesFromBilkent());
        ridestoBilkent = new ArrayList<Ride>(RideDataPicker.getRidestoBilkent());

        if(getIntent().getSerializableExtra("allList").equals("true")){
            if(getIntent().getSerializableExtra("buttonType").equals("from")){
                textViewRides.setText("All Rides From Bilkent");
                adapter = new RidesRVAdapter(this, ridesFromBilkent);
            }
            else if(getIntent().getSerializableExtra("buttonType").equals("to")){
                textViewRides.setText("All Rides To Bilkent");
                adapter = new RidesRVAdapter(this, ridestoBilkent);
            }
        }
        else{
            if(getIntent().getSerializableExtra("buttonType").equals("from")){
                textViewRides.setText("Bilkent - " + locationData.getLocationName() + " Rides");
                adapter = new RidesRVAdapter(this, ridesFromBilkent);
            }
            else if(getIntent().getSerializableExtra("buttonType").equals("to")){
                textViewRides.setText(locationData.getLocationName() + " - Bilkent Rides");
                adapter = new RidesRVAdapter(this, ridestoBilkent);
            }
        }







        Log.d("ridesActivity", ridesFromBilkent.toString());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}