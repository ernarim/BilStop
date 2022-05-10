package com.example.bilstop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Classes.Users;
import com.example.bilstop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsRVAdapter extends RecyclerView.Adapter<NotificationsRVAdapter.CardNotifications>{
    private Context mContext;
    private ArrayList<Users> requests;

    public NotificationsRVAdapter (Context mContext, ArrayList<Users> requests)
    {
        this.mContext = mContext;
        this.requests = requests;
    }


    public class CardNotifications extends RecyclerView.ViewHolder
    {

        public Button acceptButton, declineButton;
        private CircleImageView senderItemPP;
        private TextView senderName;
        public CardView notificationCardView;

        public CardNotifications(@NonNull View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            senderItemPP = itemView.findViewById(R.id.senderItemPP);
            senderName = itemView.findViewById(R.id.senderName);
            notificationCardView = itemView.findViewById(R.id.notificationCardView);
        }
    }

    @NonNull
    @Override
    public CardNotifications onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notifications, parent, false);
        return new CardNotifications(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNotifications holder, int position) {

        Users sender = requests.get(position);

        if(!sender.getProfilePicture().equals("null")){
            Picasso.get().load(sender.getProfilePicture()).into(holder.senderItemPP);
        }

        holder.senderName.setText( sender.getName() + " " + sender.getSurname() );


    }

    public void setList( ArrayList<Users> requests ){
        this.requests = requests;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }


}
