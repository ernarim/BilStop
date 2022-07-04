package com.example.bilstop.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Timestamp;

public class Location implements Serializable {
    private String locationName;
    private String locationID;
    private double locationLatitude;
    private double locationLongitude;

    public Location(String locationName, String locationID, double locationLatitude, double locationLongitude) {
        this.locationName = locationName;
        this.locationID = locationID;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public Location(String locationID, double locationLatitude, double locationLongitude) {
        this.locationID = locationID;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public Location(double locationLatitude, double locationLongitude) {
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public Location(){}

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }
}

