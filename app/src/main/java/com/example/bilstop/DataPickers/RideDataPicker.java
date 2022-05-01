package com.example.bilstop.DataPickers;

import com.example.bilstop.Classes.Ride;

import java.util.ArrayList;

public class RideDataPicker {

    private static ArrayList<Ride> ridesFromBilkent;
    private static ArrayList<Ride> ridestoBilkent;

    public RideDataPicker(){
        ridesFromBilkent = new ArrayList<>();
        ridestoBilkent = new ArrayList<>();
    }

    public static void setRidesFromBilkent(ArrayList<Ride> ridesFromBilkent) {
        RideDataPicker.ridesFromBilkent = ridesFromBilkent;
    }

    public static void setRidestoBilkent(ArrayList<Ride> ridestoBilkent) {
        RideDataPicker.ridestoBilkent = ridestoBilkent;
    }

    public static ArrayList<Ride> getRidesFromBilkent() {
        return ridesFromBilkent;
    }

    public static ArrayList<Ride> getRidestoBilkent() {
        return ridestoBilkent;
    }
}
