package com.example.bilstop.DataPickers;

import com.example.bilstop.Classes.Car;
import com.example.bilstop.Classes.Notifications;
import com.example.bilstop.Classes.Ride;

import java.util.ArrayList;
import java.util.Collections;

public class RideDataPicker  {

    private static ArrayList<Ride> ridesFromBilkent;
    private static ArrayList<Ride> ridestoBilkent;
    private static ArrayList<Ride> myRides;
    private static ArrayList<Notifications> notifications;

    public RideDataPicker(){
        ridesFromBilkent = new ArrayList<>();
        ridestoBilkent = new ArrayList<>();
        myRides = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public static void setRidesFromBilkent(ArrayList<Ride> ridesFromBilkent) {
        Collections.sort(ridesFromBilkent);
        RideDataPicker.ridesFromBilkent =  ridesFromBilkent;
    }

    public static void setRidestoBilkent(ArrayList<Ride> ridestoBilkent) {
        Collections.sort(ridestoBilkent);
        RideDataPicker.ridestoBilkent = ridestoBilkent;
    }

    public static void setMyRides(ArrayList<Ride> myRides) {
        RideDataPicker.myRides = myRides;
    }

    public static void setNotifications(ArrayList<Notifications> notifications) {
        RideDataPicker.notifications = notifications;
    }

    public static ArrayList<Ride> getRidesFromBilkent() {
        return ridesFromBilkent;
    }

    public static ArrayList<Ride> getRidestoBilkent() {
        return ridestoBilkent;
    }

    public static ArrayList<Ride> getMyRides() {
        return myRides;
    }

    public static ArrayList<Notifications> getNotifications() {
        return notifications;
    }
}
