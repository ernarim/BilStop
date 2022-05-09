package com.example.bilstop.Classes;

import com.example.bilstop.Models.PolylineData;

import java.io.Serializable;
import java.util.Calendar;

public class Ride implements Serializable, Comparable<Ride> {
    private Location origin;
    private Location destination;
    private String rideDate;
    private String rideHour;
    private int numberOfPassenger;
    private String rideId;
    private String driverName;
    private int polylineIndex;
    private double distanceFromLocation;

    private String driverUid;

    public Ride(){

    }

    public Ride(String rideId, String driverName , Location origin, Location destination, String rideDate, String rideHour ,int numberOfPassenger, int polylineIndex, String driverUid){
        this.rideId=rideId;
        this.driverName=driverName;
        this.origin=origin;
        this.destination=destination;
        this.rideDate=rideDate;
        this.rideHour=rideHour;
        this.numberOfPassenger=numberOfPassenger;
        this.polylineIndex= polylineIndex;

        this.driverUid = driverUid;
    }

    @Override
    public int compareTo(Ride ride) {
        if(this.distanceFromLocation > ride.distanceFromLocation){
            return 1;
        }
        else if(this.distanceFromLocation < ride.distanceFromLocation){
            return -1;
        }
        else{
            return 0;
        }

    }
    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public void setRideHour(String rideHour) {
        this.rideHour = rideHour;
    }

    public void setNumberOfPassenger(int numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }

    public void setPolylineIndex(int polylineIndex) {
        this.polylineIndex = polylineIndex;
    }

    public void setDistanceFromLocation(double distanceFromLocation) {
        this.distanceFromLocation = distanceFromLocation;
    }

    public String getRideId() {
        return rideId;
    }

    public String getDriverName() {
        return driverName;
    }

    public Location getOrigin() {
        return origin;
    }

    public Location getDestination() {
        return destination;
    }

    public String getRideDate() {
        return rideDate;
    }

    public String getRideHour() {
        return rideHour;
    }

    public int getNumberOfPassenger() {
        return numberOfPassenger;
    }

    public int getPolylineIndex() {
        return polylineIndex;
    }

    public double getDistanceFromLocation() {
        return distanceFromLocation;
    }

    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(String driverUid) {
        this.driverUid = driverUid;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", rideDate='" + rideDate + '\'' +
                ", rideHour='" + rideHour + '\'' +
                ", numberOfPassenger=" + numberOfPassenger +
                ", rideId='" + rideId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", polylineIndex=" + polylineIndex +
                '}';
    }
}
