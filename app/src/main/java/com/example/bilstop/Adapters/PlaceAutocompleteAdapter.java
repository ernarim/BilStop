package com.example.bilstop.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.DataPickers.AdapterActivity;
import com.example.bilstop.MapsActivity;
import com.example.bilstop.Models.GeocodeAPI.GeocodeResponse;
import com.example.bilstop.Models.GeocodeAPI.Location;
import com.example.bilstop.Models.PlacesAPI.Prediction;
import com.example.bilstop.Models.PlacesAPI.StructuredFormatting;
import com.example.bilstop.PlacesActivity;
import com.example.bilstop.R;
import com.example.bilstop.Retrofit.JsonPlaceholder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.ViewHolder>{

    private static final String TAG = "PlcAutocompleteAdapter";
    private static final String MAPS_API_URL = "https://maps.googleapis.com/";
    private ArrayList<Prediction> placePredictions;
    private Context mContext;
    private Boolean toMaps;
    private Bundle extras;

    public PlaceAutocompleteAdapter(ArrayList<Prediction> placePredictions, Context context) {
        this.placePredictions = new ArrayList<>(placePredictions);
        this.mContext = context;
        this.toMaps = false;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public Boolean getToMaps() {
        return toMaps;
    }

    public void setToMaps(Boolean toMaps) {
        this.toMaps = toMaps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.place_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.i(TAG, "onBindViewHolder: called");

        StructuredFormatting structuredFormatting = placePredictions.get(position).getStructured_formatting();
        holder.mainText.setText(structuredFormatting.getMain_text());
        holder.secondaryText.setText(structuredFormatting.getSecondary_text());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: called");

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MAPS_API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceholder jsonPlaceholder = retrofit.create(JsonPlaceholder.class);

                Call<GeocodeResponse> geocodeResponseCall = jsonPlaceholder.getPlaceCoordinates(placePredictions.get(position).getPlace_id(), mContext.getString(R.string.api_key));

                geocodeResponseCall.enqueue(new Callback<GeocodeResponse>() {
                    @Override
                    public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {
                        if (!response.isSuccessful()){
                            Log.w(TAG, "onResponse: failed with code " + response.code());
                            return;
                        }
                        GeocodeResponse geocodeResponse = response.body();
                        Location location = geocodeResponse.getResults().get(0).getGeometry().getLocation();//there are two Location classes
                        Intent intent;
                        if(toMaps) intent = new Intent(mContext, MapsActivity.class);
                        else intent = new Intent(mContext, AdapterActivity.class);
                        intent.putExtras(extras);
                        com.example.bilstop.Classes.Location location1 = new com.example.bilstop.Classes.Location(structuredFormatting.getMain_text(),
                                placePredictions.get(position).getPlace_id(), location.getLat(), location.getLng());
                        intent.putExtra("object", location1);
                        intent.putExtra("allList", "false");
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<GeocodeResponse> call, Throwable t) {
                        Log.w(TAG, "onFailure: " + t.getMessage());
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return placePredictions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout parentLayout;
        TextView mainText, secondaryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            mainText = itemView.findViewById(R.id.main_text);
            secondaryText = itemView.findViewById(R.id.secondary_text);
        }


    }


}
