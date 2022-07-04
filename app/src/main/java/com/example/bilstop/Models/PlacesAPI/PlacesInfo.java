package com.example.bilstop.Models.PlacesAPI;

import java.util.ArrayList;

public class PlacesInfo {
    private ArrayList<Prediction> predictions;

    public ArrayList<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<Prediction> predictions) {
        this.predictions = predictions;
    }
}
