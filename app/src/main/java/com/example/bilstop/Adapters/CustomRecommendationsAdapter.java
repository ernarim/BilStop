package com.example.bilstop.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.MainActivity;
import com.example.bilstop.PlacesActivity;
import com.example.bilstop.R;

import java.util.ArrayList;

public class CustomRecommendationsAdapter extends RecyclerView.Adapter<CustomRecommendationsAdapter.ViewHolder>{

    private static final String TAG = "CustomRecommendationsAd";
    private static final String MAPS_API_URL = "https://maps.googleapis.com/";

    private final Context mContext;
    private String intentPage;

    public CustomRecommendationsAdapter(Context mContext, String intentPage) {
        this.mContext = mContext;
        this.intentPage = intentPage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cl_and_past_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder:ran");
        if (position == 0){
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PlacesActivity) mContext).getCurrentLocation();
                }
            });
        }
        else if(position == 1 && intentPage.equals("home")){
            holder.imageView.setImageResource(R.drawable.ic_baseline_menu_book_24);
            String toOrFrom = "";
            toOrFrom = ((PlacesActivity) mContext).getButtonType();
            holder.textView.setText("List all rides " + toOrFrom + " Bilkent");
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PlacesActivity) mContext).goToAdapterActivity();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1 + (intentPage.equals("home") ? 1 : 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout constraintLayout;
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.parent_layout_cl_and_past);
            imageView = itemView.findViewById(R.id.icon_image_view);
            textView = itemView.findViewById(R.id.text_next_to_icon);
        }
    }

}
