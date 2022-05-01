package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.DataPickers.AdapterActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;
import java.util.Arrays;

public class PlacesActivity extends AppCompatActivity implements Serializable {

    private PlacesClient placesClient;
    private Button goToMapButton;
    private Location location=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        goToMapButton=findViewById(R.id.goToMapButtpn);


        if(getIntent().getSerializableExtra("intentPage").equals("home")){
            goToMapButton.setText("List all rides");
            if(getIntent().getSerializableExtra("buttonType").equals("from")){
                goToMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), AdapterActivity.class);
                        intent.putExtra("buttonType", getIntent().getSerializableExtra("buttonType"));
                        intent.putExtra("object",location);
                        intent.putExtra("allList","true");
                        startActivity(intent);
                    }
                });
            }
            else if(getIntent().getSerializableExtra("buttonType").equals("to")){
                goToMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), AdapterActivity.class);
                        intent.putExtra("buttonType", getIntent().getSerializableExtra("buttonType"));
                        intent.putExtra("object",location);
                        intent.putExtra("allList","true");
                        startActivity(intent);
                    }
                });
            }
        }
        else{
            goToMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("object",location);
                    startActivity(intent);
                }
            });
        }


        if(!Places.isInitialized()){
            // Initialize the SDK
            Places.initialize(getApplicationContext(),getString(R.string.api_key));
        }
        // Create a new PlacesClient instance
        placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountries("TR");
        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("demo", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());

                //Location data to send Maps Activity
                location = new Location(place.getName(), place.getId(), String.valueOf(place.getLatLng().latitude),String.valueOf(place.getLatLng().longitude) );

                if(getIntent().getSerializableExtra("intentPage").equals("create")){
                    Intent intent = new Intent(PlacesActivity.this, MapsActivity.class);
                    intent.putExtra("buttonType", getIntent().getSerializableExtra("buttonType"));
                    intent.putExtra("object",location);
                    startActivity(intent);
                }
                else if(getIntent().getSerializableExtra("intentPage").equals("home")){
                    Intent intent = new Intent(PlacesActivity.this, AdapterActivity.class);
                    intent.putExtra("buttonType", getIntent().getSerializableExtra("buttonType"));
                    intent.putExtra("object",location);
                    startActivity(intent);
                }

            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("demo", "An error occurred: " + status);
            }
        });
    }
}