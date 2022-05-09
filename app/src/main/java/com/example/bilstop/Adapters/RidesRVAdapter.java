package com.example.bilstop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Classes.Ride;
import com.example.bilstop.R;
import com.example.bilstop.RideInfoActivity;

import java.util.List;

public class RidesRVAdapter extends RecyclerView.Adapter<RidesRVAdapter.ObjectsHolder>{
    private Context mContex;
    private List<Ride> rides;

    public RidesRVAdapter(Context mContex, List<Ride> rides){
        this.mContex=mContex;
        this.rides=rides;
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public class ObjectsHolder extends RecyclerView.ViewHolder{
        public CardView cardViewRow;
        public CardView cardViewImage;
        public TextView textViewName;
        public TextView textViewNumOfPas;
        public TextView textViewDate;
        public TextView textViewTime;
        public ImageView imageViewRides;

        public ObjectsHolder(View view){
            super(view);
            cardViewRow = (CardView) view.findViewById(R.id.cardViewRow);
            cardViewImage = (CardView) view.findViewById(R.id.cardViewImage);
            textViewName= (TextView) view.findViewById(R.id.textViewName);
            textViewNumOfPas= (TextView) view.findViewById(R.id.textViewNumOfPas);
            textViewDate= (TextView) view.findViewById(R.id.textViewDate);
            textViewTime= (TextView) view.findViewById(R.id.textViewTime);
            imageViewRides = (ImageView) view.findViewById(R.id.imageViewRides);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectsHolder holder, int position) {
        final Ride ride = rides.get(position);

        holder.textViewName.setText("Driver Name: " + ride.getDriverName());
        holder.textViewNumOfPas.setText("Number of Passengers: " + ride.getNumberOfPassenger());
        holder.textViewDate.setText("Date: " + ride.getRideDate());
        holder.textViewTime.setText("Time: " + ride.getRideHour());

        holder.cardViewRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContex.getApplicationContext(), RideInfoActivity.class);
                intent.putExtra("ride", ride);
                intent.putExtra("uid", ride.getDriverUid());
                mContex.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ObjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rides_cardview, parent, false);


        return new ObjectsHolder(itemView);
    }
}
