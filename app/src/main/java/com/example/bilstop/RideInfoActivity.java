package com.example.bilstop;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.bilstop.Classes.Car;
import com.example.bilstop.Classes.Location;
import com.example.bilstop.Classes.Notifications;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.DataPickers.AdapterActivity;
import com.example.bilstop.Models.PolylineData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.squareup.picasso.Picasso;

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
    private TextView textViewCarPlateInfo;
    private ImageView imageViewCarInfo;
    private ImageButton imageButtonSeeProfile;
    private ImageButton imageButtonDeleteProfile;
    private Button requestButton;

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

    private TextView textViewDuration2;
    private TextView textViewDistance2;

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
        textViewCarPlateInfo = findViewById(R.id.textViewCarPlateInfo);
        imageViewCarInfo = findViewById(R.id.imageViewCarInfo);
        textViewDuration2 = findViewById(R.id.textViewDuration2);
        textViewDistance2 = findViewById(R.id.textViewDistance2);
        imageButtonSeeProfile = findViewById(R.id.imbRideInfoSeeProfile);
        imageButtonDeleteProfile = findViewById(R.id.deleteRideButton);
        requestButton = findViewById(R.id.requestButton);

        ride = (Ride) getIntent().getSerializableExtra("ride");

        if(ride.getDriverUid().equals(FirebaseAuth.getInstance().getUid())){
            imageButtonSeeProfile.setVisibility(View.INVISIBLE);
            imageButtonDeleteProfile.setVisibility(View.VISIBLE);
            requestButton.setVisibility(View.INVISIBLE);

        }
        else{

            if(ride.getMyRide()){
                requestButton.setVisibility(View.INVISIBLE);
                imageButtonSeeProfile.setVisibility(View.VISIBLE);
                imageButtonDeleteProfile.setVisibility(View.INVISIBLE);

            }
            else{
                imageButtonSeeProfile.setVisibility(View.VISIBLE);
                imageButtonDeleteProfile.setVisibility(View.INVISIBLE);
                requestButton.setVisibility(View.VISIBLE);
            }
        }

        textViewDriverNameInfo.setText("Driver Name: " + ride.getDriverName());
        textViewRouteInfo.setText(ride.getOrigin().getLocationName() + " - " + ride.getDestination().getLocationName());
        textViewNoOfPasInfo.setText(String.valueOf("Number of passengers: " + ride.getNumberOfPassenger()));
        textViewDateInfo.setText("Date: " + ride.getRideDate());
        textViewHourInfo.setText("Time: " + ride.getRideHour());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefCar = database.getReference("users").child(ride.getDriverUid()).child("car");

        ArrayList<String> carValues = new ArrayList<>();
        myRefCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String value = dataSnapshot.getValue(String.class);
                        Log.d("value", value);
                        carValues.add(value);
                    }
                    if(carValues.size() == 5){
                        textViewCarInfo.setText("Car: " + carValues.get(0) + " " + carValues.get(3));
                        textViewCarPlateInfo.setText("Licence Plate: " + carValues.get(2));
                        Picasso.get().load(carValues.get(4)).into(imageViewCarInfo);

                    }
                }

            }@Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mMapView = findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notifications notification = new Notifications(FirebaseAuth.getInstance().getUid(), ride.getDriverUid(), "currentState" , FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                        , null,ride);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("notifications").child(ride.getDriverUid());
                myRef.push().setValue(notification);
                Intent intent = new Intent(RideInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        imageButtonSeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = getIntent().getExtras().getString("uid");
                Intent intent = new Intent(getApplicationContext(),TargetProfileActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        imageButtonDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("ridesFromBilkent");
                DatabaseReference myRef2 = database.getReference().child("ridesToBilkent");
                DatabaseReference myRef3 = database.getReference().child("myRides").child(FirebaseAuth.getInstance().getUid());

                myRef.child(ride.getRideId()).removeValue();
                myRef2.child(ride.getRideId()).removeValue();
                myRef3.child(ride.getRideId()).removeValue();
                Intent intent = new Intent(RideInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

        startLocation = new com.google.android.gms.maps.model.LatLng(Double.valueOf(startLocationData.getLocationLatitude())
                ,Double.valueOf(startLocationData.getLocationLongitude()));

        finalLocation = new com.google.android.gms.maps.model.LatLng(Double.valueOf(finalLocationData.getLocationLatitude())
                ,Double.valueOf(finalLocationData.getLocationLongitude()));

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

    public void onMapReady(GoogleMap map) {

        googleMap=map;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation,10));

        originMarker = new MarkerOptions().position(startLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_image));

        destinationMarker = new MarkerOptions().position(finalLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        googleMap.addMarker(originMarker);
        googleMap.addMarker(destinationMarker);


        googleMap.getUiSettings().setZoomControlsEnabled(true);


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
                PolylineData polylineData = new PolylineData(polyline,route.legs[0]);

                textViewDuration2.setTextColor(Color.WHITE);
                textViewDistance2.setTextColor(Color.WHITE);
                textViewDuration2.setText("Duration: " + polylineData.getLeg().duration);
                textViewDistance2.setText("Distance: " + polylineData.getLeg().distance);

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




}