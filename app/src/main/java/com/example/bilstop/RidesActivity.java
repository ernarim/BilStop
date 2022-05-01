package com.example.bilstop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bilstop.DataPickers.RideDataPicker;
import com.example.bilstop.Adapters.RidesRVAdapter;
import com.example.bilstop.Classes.Ride;

import java.util.ArrayList;

public class RidesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Ride> rides;
    private RidesRVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rides);

        rides =new ArrayList<Ride>(RideDataPicker.getRides());

        Log.d("ridesActivity", rides.toString());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RidesRVAdapter(this, rides);
        recyclerView.setAdapter(adapter);

    }

}