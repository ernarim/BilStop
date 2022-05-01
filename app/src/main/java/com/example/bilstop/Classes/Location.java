package com.example.bilstop.Classes;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Location implements Serializable {
    private String locationName;
    private String locationID;
    private String locationLatitude;
    private String locationLongitude;

    public Location(String locationName, String locationID, String locationLatitude, String locationLongitude){
        this.locationName=locationName;
        this.locationID=locationID;
        this.locationLatitude=locationLatitude;
        this.locationLongitude=locationLongitude;
    }

    public Location(String locationLatitude, String locationLongitude){
        this.locationLatitude=locationLatitude;
        this.locationLongitude=locationLongitude;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationName='" + locationName + '\'' +
                ", locationID='" + locationID + '\'' +
                ", locationLatitude='" + locationLatitude + '\'' +
                ", locationLongitude='" + locationLongitude + '\'' +
                '}';
    }
}

