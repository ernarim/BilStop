package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bilstop.Adapters.CustomRecommendationsAdapter;
import com.example.bilstop.Adapters.PlaceAutocompleteAdapter;
import com.example.bilstop.Classes.Location;
import com.example.bilstop.DataPickers.AdapterActivity;
import com.example.bilstop.Models.PlacesAPI.PlacesInfo;
import com.example.bilstop.Models.PlacesAPI.Prediction;
import com.example.bilstop.Models.Root;
import com.example.bilstop.Retrofit.JsonPlaceholder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlacesActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "PlacesActivity";
    private static final String MAPS_API_URL = "https://maps.googleapis.com/";
    private ArrayList<Prediction> placePredictions;
    private Call<PlacesInfo> placesInfoCall;
    private RecyclerView recyclerView;
    private DividerItemDecoration dividerItemDecoration;
    private LocationRequest locationRequest;
    private Retrofit retrofit;
    private String intentPage;
    private String buttonType;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        apiKey = getString(R.string.api_key);
        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setIconified(false);
        buttonType = (String) getIntent().getSerializableExtra("buttonType");
        if (buttonType.equals("from")){
            searchView.setQueryHint("Where are you going from Bilkent?");
        }
        else{
            searchView.setQueryHint("Where is your starting location?");
        }

        recyclerView = findViewById(R.id.recyclerView);
        intentPage = (String) getIntent().getSerializableExtra("intentPage");

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        recyclerView.setLayoutManager(new LinearLayoutManager(PlacesActivity.this));
        dividerItemDecoration = new DividerItemDecoration(PlacesActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        createCustomRecommendationsAdapter();

        retrofit = new Retrofit.Builder()
                .baseUrl(MAPS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholder jsonPlaceholder = retrofit.create(JsonPlaceholder.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String input) {
                input = input.trim();
                if(input.isEmpty()){
                    Log.d(TAG, "onQueryTextChange: empty string");
                    createCustomRecommendationsAdapter();
                }
                else{
                    placesInfoCall = jsonPlaceholder.getPlaces(input, apiKey);

                    placesInfoCall.enqueue(new Callback<PlacesInfo>() {
                        @Override
                        public void onResponse(Call<PlacesInfo> call, Response<PlacesInfo> response) {
                            if (!response.isSuccessful()) {
                                Log.d("retrofit", "Code: " + response.code());
                                return;
                            }

                            PlacesInfo placesInfo = response.body();
                            placePredictions = placesInfo.getPredictions();

                            if (!searchView.getQuery().toString().isEmpty()) {
                                PlaceAutocompleteAdapter adapter = new PlaceAutocompleteAdapter(placePredictions, PlacesActivity.this);
                                if (intentPage.equals("create")) adapter.setToMaps(true);
                                adapter.setExtras(getIntent().getExtras());
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<PlacesInfo> call, Throwable t) {
                            Log.d("retrofit", t.getMessage());
                        }
                    });
                }
                return false;
            }
        });

    }

    public void goToAdapterActivity(){
        Intent intent = new Intent(PlacesActivity.this, AdapterActivity.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("allList","true");
        startActivity(intent);
    }

    public String getButtonType() {
        return buttonType;
    }

    private void createCustomRecommendationsAdapter(){
        CustomRecommendationsAdapter customRecommendationsAdapter = new CustomRecommendationsAdapter(PlacesActivity.this, intentPage);
        recyclerView.setAdapter(customRecommendationsAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (isGPSEnabled()) {
                    getCurrentLocation();
                }else {
                    turnOnGPS();
                }
            }
        }
    }

    public void getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(PlacesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(PlacesActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(PlacesActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult.getLocations().size() > 0){
                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        JsonPlaceholder jsonPlaceholder = retrofit.create(JsonPlaceholder.class);
                                        Call<Root> rootCall = jsonPlaceholder.getPlaceNameFromLatLng(latitude + "," + longitude, apiKey);
                                        rootCall.enqueue(new Callback<Root>() {
                                            @Override
                                            public void onResponse(Call<Root> call, Response<Root> response) {
                                                if (!response.isSuccessful()) {
                                                    Log.d("retrofit", "Code: " + response.code());
                                                    return;
                                                }
                                                Root root = response.body();

                                                String locationName = root.getResults().get(0).getAddress_components().get(1).getShort_name() + " " +
                                                        root.getResults().get(0).getAddress_components().get(2).getShort_name();
                                                String locationID = root.getResults().get(0).getPlace_id();
                                                Location location = new Location(locationName, locationID, latitude, longitude);
                                                Intent intent;
                                                if(intentPage.equals("home")) intent = new Intent(PlacesActivity.this, AdapterActivity.class);
                                                else intent = new Intent(PlacesActivity.this, MapsActivity.class);
                                                intent.putExtras(getIntent().getExtras());
                                                intent.putExtra("allList","false");
                                                intent.putExtra("object", location);
                                                startActivity(intent);

                                            }

                                            @Override
                                            public void onFailure(Call<Root> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                            }, Looper.getMainLooper());
                } else {
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(PlacesActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(PlacesActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
