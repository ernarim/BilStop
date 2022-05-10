package com.example.bilstop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.Models.PolylineData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapsSdkInitializedCallback,  OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private GeoApiContext mGeoApiContext = null;
    private GoogleMap googleMap;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();
    private Location startLocationData = null;
    private Location finalLocationData = null;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private TextView titleTextView;
    private TextView durationTextView;
    private TextView distanceTextView;

    private PolylineData selectedPolyline;
    private int selectedPolylineIndex;

    private Ride ride = new Ride();
    private String buttonType;
    private MarkerOptions marker1;
    private MarkerOptions marker2;
    private LatLng startLocation;
    private LatLng finalLocation;

    private String locationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        MapsInitializer.initialize(getApplicationContext(), Renderer.LATEST, this);

        buttonType = (String) getIntent().getSerializableExtra("buttonType");
        if((Location) getIntent().getSerializableExtra("object") != null){
            //Location data coming from Autocomplete Places Fragment
            if(buttonType.equals("from")){
                finalLocationData = (Location) getIntent().getSerializableExtra("object");
                locationName = finalLocationData.getLocationName();
                finalLocation = new LatLng(Double.valueOf(finalLocationData.getLocationLatitude()),
                        Double.valueOf(finalLocationData.getLocationLongitude()));
                marker1 = new MarkerOptions().position(finalLocation).title("from: " + finalLocationData.getLocationName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                Log.d("location", finalLocationData.getLocationName() + " " +  finalLocationData.getLocationLatitude() + " " + finalLocationData.getLocationLongitude());
            }
            else{
                startLocationData = (Location) getIntent().getSerializableExtra("object");
                locationName= startLocationData.getLocationName();
                startLocation = new LatLng(Double.valueOf(startLocationData.getLocationLatitude()),
                        Double.valueOf(startLocationData.getLocationLongitude()));
                marker1 = new MarkerOptions().position(startLocation).title("to: " + startLocationData.getLocationName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                Log.d("location", startLocationData.getLocationName() + " " +  startLocationData.getLocationLatitude() + " " + startLocationData.getLocationLongitude());
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setVisibility(View.INVISIBLE);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleTextView.setText("Choose the appropriate route");
                durationTextView.setTextColor(Color.WHITE);
                distanceTextView.setTextColor(Color.WHITE);
                calculateDirections();
                googleMap.setOnMapClickListener(null);
                if(buttonType.equals("from")){ marker2 = new MarkerOptions().position(startLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_image));}
                else{ marker2 = new MarkerOptions().position(finalLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_image));}
                googleMap.addMarker(marker2);
                fab.setVisibility(View.INVISIBLE);
                fab2.setVisibility(View.VISIBLE);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, FinalizeRideActivity.class);
                int polylinePointsLength = selectedPolyline.getPolyline().getPoints().size();
                Location origin = new Location(String.valueOf(selectedPolyline.getPolyline().getPoints().get(0).latitude),String.valueOf(selectedPolyline.getPolyline().getPoints().get(0).longitude));
                Location destination = new Location(String.valueOf(selectedPolyline.getPolyline().getPoints().get(polylinePointsLength-1).latitude),String.valueOf(selectedPolyline.getPolyline().getPoints().get(polylinePointsLength-1).longitude));
                if(buttonType.equals("from")){
                   origin.setLocationName("Bilkent");
                   destination.setLocationName(locationName);
                   Log.d("finallocation", finalLocationData.getLocationName() + " " +  finalLocationData.getLocationLatitude() + " " + finalLocationData.getLocationLongitude());
                }

                else{
                    origin.setLocationName(locationName);
                    destination.setLocationName("Bilkent");
                    Log.d("startlocation", startLocationData.getLocationName() + " " +  startLocationData.getLocationLatitude() + " " + startLocationData.getLocationLongitude());
                }

                ride.setOrigin(origin); ride.setDestination(destination);
                Log.d("polyline" , String.valueOf(selectedPolyline.getPolyline().getPoints().get(0).latitude) );
                intent.putExtras(getIntent().getExtras());
                intent.putExtra("ride", ride);
                startActivity(intent);
            }
        });

        if(mGeoApiContext == null){
            mGeoApiContext= new GeoApiContext.Builder().apiKey(getString(R.string.api_key)).build();
        }
    }
    @Override
    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap=gMap;

        Location bilkent = new Location("Bilkent University", "id", "39.87012508171328", "32.74890133665072");
        Location locationData = (Location) getIntent().getSerializableExtra("object");
        if(locationData != null){
            //Creating destination point according to data coming from Autocomplete Fragment
            if(buttonType.equals("from")){
                startLocationData = bilkent;
                startLocation = new LatLng(Double.valueOf(startLocationData.getLocationLatitude()), Double.valueOf(startLocationData.getLocationLongitude()));
                finalLocationData = locationData;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLocation,15));
            }
            else{
                startLocationData = locationData;
                finalLocationData = bilkent;
                finalLocation = new LatLng(Double.valueOf(finalLocationData.getLocationLatitude()), Double.valueOf(finalLocationData.getLocationLongitude()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation,15));
            }
        }

        GoogleMapOptions options = new GoogleMapOptions();

        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(true)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false)
                .zoomControlsEnabled(true)
                .mapToolbarEnabled(false);

        googleMap.addMarker(marker1);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnPolylineClickListener(this);

        // add marker on the screen when touch somewhere on the map
        googleMap.setOnMapClickListener(this::onMapClick);

    }

    private void calculateDirections(){
        Log.d("demo", "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng origin = new com.google.maps.model.LatLng(
                Double.valueOf(startLocationData.getLocationLatitude()),
                Double.valueOf(startLocationData.getLocationLongitude()));

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                Double.valueOf(finalLocationData.getLocationLatitude()),
                Double.valueOf(finalLocationData.getLocationLongitude())
        );

        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(origin);

        Log.d("demo", "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }
            @Override
            public void onFailure(Throwable e) {
            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("demo", "run: result routes: " + result.routes.length);
                if(mPolyLinesData.size() > 0){
                    for(PolylineData polylineData: mPolyLinesData){
                        polylineData.getPolyline().remove();
                    }
                    mPolyLinesData.clear();
                    mPolyLinesData = new ArrayList<>();
                }
                double duration = Integer.MAX_VALUE;
                for(DirectionsRoute route: result.routes){
                    Log.d("demo", "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<LatLng> newDecodedPath = new ArrayList<>();

                    for(com.google.maps.model.LatLng latLng: decodedPath){

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }

                    Polyline polyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.grew));
                    polyline.setClickable(true);
                    polyline.setWidth(18);
                    mPolyLinesData.add(new PolylineData(polyline,route.legs[0]));

                    double tempDuration = route.legs[0].duration.inSeconds;
                    if(tempDuration < duration){
                        duration = tempDuration;
                        onPolylineClick(polyline);
                        zoomRoute(polyline.getPoints());
                    }

                }
            }
        });
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 200;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                300,
                null
        );
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        int index = 0;
        for(PolylineData polylineData: mPolyLinesData){
            index++;
            Log.d("demo", "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                polylineData.getPolyline().setZIndex(1);

                durationTextView.setText("Duration: " + polylineData.getLeg().duration);
                distanceTextView.setText("Distance: " + polylineData.getLeg().distance);
                selectedPolyline = polylineData;
                selectedPolylineIndex=index;
                ride.setPolylineIndex(selectedPolylineIndex);

                Log.d("polylineindex", String.valueOf(ride.getPolylineIndex()));
                Log.d("Polyline", selectedPolyline.toString());

            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.grew));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }


    public void onMapClick(LatLng latLng){

        marker1.position(latLng);

        marker1.title(latLng.latitude + " : " + latLng.longitude);

        googleMap.clear();

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        googleMap.addMarker(marker1);

        if(buttonType.equals("from")){
            finalLocationData = new Location("", "?", String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
            finalLocation = latLng;
        }
        else{
            startLocationData = new Location("", "?", String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
            startLocation = latLng;
        }
    }


}