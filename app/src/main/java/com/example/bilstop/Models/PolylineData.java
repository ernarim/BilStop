package com.example.bilstop.Models;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Polyline;
import com.google.maps.model.DirectionsLeg;

import java.io.Serializable;

public class PolylineData{

    private int mData;


    private Polyline polyline;
    private DirectionsLeg leg;

    public PolylineData(Polyline polyline, DirectionsLeg leg) {
        this.polyline = polyline;
        this.leg = leg;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public DirectionsLeg getLeg() {
        return leg;
    }

    public void setLeg(DirectionsLeg leg) {
        this.leg = leg;
    }

    @Override
    public String toString() {
        return "PolylineData{" +
                "polyline=" + polyline +
                ", leg=" + leg +
                '}';
    }



}
