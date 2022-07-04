package com.example.bilstop.Retrofit;


import android.content.res.Resources;

import com.example.bilstop.BottomNavActivity;
import com.example.bilstop.Models.GeocodeAPI.GeocodeResponse;
import com.example.bilstop.Models.PlacesAPI.PlacesInfo;
import com.example.bilstop.Models.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceholder {

    @GET("maps/api/place/autocomplete/json?components=country:tr")
    Call<PlacesInfo> getPlaces(@Query("input") String input, @Query("key") String key);

    @GET("maps/api/geocode/json?")
    Call<GeocodeResponse> getPlaceCoordinates(@Query("place_id") String place_id, @Query("key") String key);

    @GET("maps/api/geocode/json?")
    Call<Root> getPlaceNameFromLatLng(@Query("latlng") String latLng, @Query("key") String key);

}
