package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bilstop.Classes.Location;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.Models.PolylineData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class RideInfoActivity extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mMapView;
    private TextView textViewDriverNameInfo;
    private TextView textViewRouteInfo;
    private TextView textViewNoOfPasInfo;
    private TextView textViewDateInfo;
    private TextView textViewHourInfo;
    private TextView textViewCarInfo;
    private ImageView imageViewCarInfo;

    private GeoApiContext mGeoApiContext =null;
    private GoogleMap googleMap;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();

    private MarkerOptions originMarker;
    private MarkerOptions destinationMarker;

    private Ride ride;
    private Location startLocationData;
    private Location finalLocationData;

    private com.google.android.gms.maps.model.LatLng startLocation;
    private com.google.android.gms.maps.model.LatLng finalLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        if(mGeoApiContext == null){
            mGeoApiContext= new GeoApiContext.Builder().apiKey(getString(R.string.api_key)).build();
        }

        textViewDriverNameInfo = findViewById(R.id.textViewDriverNameInfo);
        textViewRouteInfo = findViewById(R.id.textViewRouteInfo);
        textViewNoOfPasInfo = findViewById(R.id.textViewNoOfPasInfo);
        textViewDateInfo = findViewById(R.id.textViewDateInfo);
        textViewHourInfo = findViewById(R.id.textViewHourInfo);
        textViewCarInfo = findViewById(R.id.textViewCarInfo);
        imageViewCarInfo = findViewById(R.id.imageViewCarInfo);

        ride = (Ride) getIntent().getSerializableExtra("ride");
        textViewDriverNameInfo.setText(ride.getDriverName());
        textViewRouteInfo.setText(ride.getOrigin().getLocationName() + " - " + ride.getDestination().getLocationName());
        textViewNoOfPasInfo.setText(String.valueOf(ride.getNumberOfPassenger()));
        textViewDateInfo.setText(ride.getRideDate());
        textViewHourInfo.setText(ride.getRideHour());
        //textViewCarInfo.setText(ride.get());

        mMapView = findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);


    }


    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("AIzaSyBJfW0T6RwjB1KsMjyLSTibv_Z58TaWLeE");
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync( this);

        startLocationData = ride.getOrigin();
        finalLocationData = ride.getDestination();

        startLocation = new com.google.android.gms.maps.model.LatLng(Double.valueOf(startLocationData.getLocationLatitude()),Double.valueOf(startLocationData.getLocationLongitude()));
        originMarker = new MarkerOptions().position(startLocation).title("Bilkent");

        calculateDirections();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mMapView.onSaveInstanceState(outState);
    }
    @SuppressLint("MissingPermission")

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }


    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap map) {

        googleMap=map;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);


    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
                Log.d("direction", "calculateDirections: routes: " + result.routes[0].toString());
                Log.d("direction", "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d("direction", "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d("direction", "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

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


                DirectionsRoute route = result.routes[ride.getPolylineIndex()-1];
                Log.d("demo", "run: leg: " + route.legs[0].toString());
                List<LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<com.google.android.gms.maps.model.LatLng> newDecodedPath = new ArrayList<>();

                // This loops through all the LatLng coordinates of ONE polyline.
                for(com.google.maps.model.LatLng latLng: decodedPath) {

//              Log.d(TAG, "run: latlng: " + latLng.toString());
                    newDecodedPath.add(new com.google.android.gms.maps.model.LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }

                Polyline polyline = googleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                polyline.setClickable(true);
                polyline.setWidth(18);
                mPolyLinesData.add(new PolylineData(polyline,route.legs[0]));

                // highlight the fastest route and adjust camera
                double tempDuration = route.legs[0].duration.inSeconds;
                if(tempDuration < duration){
                    duration = tempDuration;
                    zoomRoute(polyline.getPoints());
                }

            }

        });
    }

    public void zoomRoute(List<com.google.android.gms.maps.model.LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (com.google.android.gms.maps.model.LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 50;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    /*public void onPolylineClick(Polyline polyline) {

        int index = 0;
        for(PolylineData polylineData: mPolyLinesData){
            index++;
            Log.d("demo", "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                polylineData.getPolyline().setZIndex(1);

                com.google.android.gms.maps.model.LatLng startLocation = new com.google.android.gms.maps.model.LatLng(
                        polylineData.getLeg().startLocation.lat,
                        polylineData.getLeg().startLocation.lng
                );

                Marker marker1 = googleMap.addMarker(new MarkerOptions()
                        .position(startLocation).title("Bilkent")
                );

                marker1.showInfoWindow();
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.grew));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }*/


}