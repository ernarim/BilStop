package com.example.bilstop;

import com.example.bilstop.Models.PolylineData;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapsSdkInitializedCallback,  OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private GeoApiContext mGeoApiContext =null;
    private GoogleMap googleMap;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();
    private Location locationData=null;
    private FloatingActionButton fab;
    private MarkerOptions destinationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), Renderer.LATEST, this);


        if((Location) getIntent().getSerializableExtra("object")!=null){
            //Location data coming from Autocomplete Places Fragment
            locationData = (Location) getIntent().getSerializableExtra("object");
            Log.d("location", locationData.getLocationName() + " " +  locationData.getLocationLatitude() + " " + locationData.getLocationLongitude());
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateDirections(destinationMarker);
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
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        googleMap=gMap;

        if(locationData!=null){
            //Creating destination point according to data coming from Autocomplete Fragment
            LatLng destination = new LatLng(Double.valueOf(locationData.getLocationLatitude()) , Double.valueOf(locationData.getLocationLongitude()));
            destinationMarker = new MarkerOptions().position(destination).title("Bilkent");
            googleMap.addMarker(destinationMarker);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination,15));
        }
        else{
            LatLng ankara = new LatLng(39.91331640578498, 32.85483867821641);
            destinationMarker= new MarkerOptions().position(ankara).title("Bilkent");
            googleMap.addMarker(destinationMarker);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara,15));
        }


        //calculateDirections(marker);
        //calculateDirections(marker2);

        GoogleMapOptions options = new GoogleMapOptions();

        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false)
                .zoomControlsEnabled(true);

        googleMap.getUiSettings().setZoomControlsEnabled(true);


        googleMap.setOnPolylineClickListener(this);

        // add marker on the screen when touch somewhere on the map
        googleMap.setOnMapClickListener(this::onMapClick);


    }

    private void calculateDirections(MarkerOptions marker){
        Log.d("demo", "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                marker.getPosition().latitude,
                marker.getPosition().longitude
        );

        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        com.google.maps.model.LatLng ankara = new com.google.maps.model.LatLng(39.87498706924094, 32.747574449779364);

        directions.alternatives(true);
        directions.origin(ankara);
        Log.d("demo", "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
//                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
//                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
//                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
//                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                Log.d("demo", "onResult: successfully retrieved directions.");
                addPolylinesToMap(result);

            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("demo", "calculateDirections: Failed to get directions: " + e.getMessage() );

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
                double duration = 999999999;
                for(DirectionsRoute route: result.routes){
                    Log.d("demo", "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }

                    Polyline polyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.grew));
                    polyline.setClickable(true);
                    polyline.setWidth(17);
                    mPolyLinesData.add(new PolylineData(polyline,route.legs[0]));

                    // highlight the fastest route and adjust camera
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

        int routePadding = 50;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
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

                LatLng startLocation = new LatLng(
                        polylineData.getLeg().startLocation.lat,
                        polylineData.getLeg().startLocation.lng
                );

                Marker marker1 = googleMap.addMarker(new MarkerOptions()
                        .position(startLocation).title("Bilkent")
                );

                LatLng endLocation = new LatLng(
                        polylineData.getLeg().endLocation.lat,
                        polylineData.getLeg().endLocation.lng
                );

                Marker marker2 = googleMap.addMarker(new MarkerOptions()
                        .position(endLocation).title("Ankara")
                        .snippet("Duration: " + polylineData.getLeg().duration)
                );

                marker1.showInfoWindow();
                marker2.showInfoWindow();
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.grew));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }


    public void onMapClick(LatLng latLng){
        // Creating a marker

        // Setting the position for the marker
        destinationMarker.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        destinationMarker.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        googleMap.addMarker(destinationMarker);
    }


}

