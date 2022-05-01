package com.example.bilstop.DataPickers;

import com.example.bilstop.Classes.Ride;

import java.util.ArrayList;

public class RideDataPicker {

    private static ArrayList<Ride> rides;

    public RideDataPicker(){
        rides = new ArrayList<>();
    }

    public static void setRides(ArrayList<Ride> rides) {
        RideDataPicker.rides = rides;
    }

    public static void add(Ride r){
        rides.add(r);
    }

    public static ArrayList<Ride> getRides() {
        return rides;
    }

}
