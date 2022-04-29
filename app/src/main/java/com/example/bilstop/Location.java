package com.example.bilstop;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private String locationName;
    private String locationID;
    private LatLng locationLatLng;

    public Location(String locationName, String locationID, LatLng locationLatLng){
        this.locationName=locationName;
        this.locationID=locationID;
        this.locationLatLng=locationLatLng;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setLocationLatLng(LatLng locationLatLng) {
        this.locationLatLng = locationLatLng;
    }

    public String getLocationName() {
        return locationName;
    }

    public LatLng getLocationLatLng() {
        return locationLatLng;
    }

    public String getLocationID() {
        return locationID;
    }
}

