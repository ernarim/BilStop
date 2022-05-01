package com.example.bilstop.Classes;

import com.example.bilstop.Models.PolylineData;

import java.io.Serializable;
import java.util.Calendar;

public class Ride implements Serializable {
    private Location origin;
    private Location destination;
    private Calendar rideDate;
    private int numberOfPassenger;
    private String rideId;

    public Ride(){

    }

    public Ride(String rideId, Location origin, Location destination, Calendar rideDate, int numberOfPassenger){
        this.rideId=rideId;
        this.origin=origin;
        this.destination=destination;
        this.rideDate=rideDate;
        this.numberOfPassenger=numberOfPassenger;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setRideDate(Calendar rideDate) {
        this.rideDate = rideDate;
    }

    public void setNumberOfPassenger(int numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }

    public String getRideId() {
        return rideId;
    }

    public Location getOrigin() {
        return origin;
    }

    public Location getDestination() {
        return destination;
    }

    public Calendar getRideDate() {
        return rideDate;
    }

    public int getNumberOfPassenger() {
        return numberOfPassenger;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", rideDate=" + rideDate +
                ", numberOfPassenger=" + numberOfPassenger +
                ", rideId='" + rideId + '\'' +
                '}';
    }
}
