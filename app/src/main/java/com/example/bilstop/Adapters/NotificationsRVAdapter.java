package com.example.bilstop.Adapters;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.BottomNavActivity;
import com.example.bilstop.Classes.Notifications;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.DataPickers.AdapterActivityNotifications;
import com.example.bilstop.MainActivity;
import com.example.bilstop.NotificationsActivity;
import com.example.bilstop.R;
import com.example.bilstop.RideInfoActivity;
import com.example.bilstop.TargetProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsRVAdapter extends RecyclerView.Adapter<NotificationsRVAdapter.CardNotifications>{
    private Context mContext;
    //private ArrayList<Users> requests;
    private ArrayList<Notifications> notifications;

    public NotificationsRVAdapter (Context mContext, ArrayList<Notifications> notifications)
    {
        //ArrayList<Users> requests
        this.mContext = mContext;
        //this.requests = requests;
        this.notifications = notifications;
    }


    public class CardNotifications extends RecyclerView.ViewHolder
    {

        public Button acceptButton, declineButton;
        private CircleImageView senderItemPP;
        private TextView senderName;
        private TextView senderRide;
        private TextView notificationType;
        private ImageButton imageButtonRide;
        public CardView notificationCardView;


        public CardNotifications(@NonNull View itemView) {
            super(itemView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            senderItemPP = itemView.findViewById(R.id.senderItemPP);
            senderName = itemView.findViewById(R.id.senderName);
            senderRide = itemView.findViewById(R.id.senderRide);
            notificationType = itemView.findViewById(R.id.textViewNotificationType);
            imageButtonRide = itemView.findViewById(R.id.imageButtonRide);
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

        /*Users sender = requests.get(position);

        if(!sender.getProfilePicture().equals("null")){
            Picasso.get().load(sender.getProfilePicture()).into(holder.senderItemPP);
        }

        holder.senderName.setText( sender.getName() + " " + sender.getSurname() );*/

        Notifications notification = notifications.get(position);
        holder.senderName.setText(notification.getName());
        if(notification.getRide()!=null){

            if(notification.getCurrentState().equals("accepted")){
                holder.notificationType.setText("Ride Request Accepted");
                holder.acceptButton.setVisibility(View.INVISIBLE);
                holder.declineButton.setVisibility(View.INVISIBLE);
                holder.senderName.setText("Driver: " + notification.getRide().getDriverName());
                holder.senderItemPP.setVisibility(View.INVISIBLE);

                holder.imageButtonRide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Ride ride = notification.getRide();
                        Intent intent = new Intent(mContext.getApplicationContext(), RideInfoActivity.class);
                        intent.putExtra("ride", ride);
                        mContext.startActivity(intent);
                    }
                });
            }
            else if(notification.getCurrentState().equals("declined")){
                holder.notificationType.setText("Ride Request Declined");
                holder.acceptButton.setVisibility(View.INVISIBLE);
                holder.declineButton.setVisibility(View.INVISIBLE);
                holder.senderName.setText("Driver: " + notification.getRide().getDriverName());
                holder.senderItemPP.setVisibility(View.INVISIBLE);
                holder.senderRide.setVisibility(View.INVISIBLE);
                holder.imageButtonRide.setVisibility(View.INVISIBLE);
            }

            else{
                holder.notificationType.setText("Ride request");
                holder.imageButtonRide.setVisibility(View.VISIBLE);
                holder.imageButtonRide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Ride ride = notification.getRide();
                        Intent intent = new Intent(mContext.getApplicationContext(), RideInfoActivity.class);
                        intent.putExtra("ride", ride);
                        mContext.startActivity(intent);
                    }
                });

                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Notifications newNotification = new Notifications(FirebaseAuth.getInstance().getUid(), notification.getSenderUserId(), "accepted" , FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                                , null,notification.getRide());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("notifications").child(newNotification.getTargetUid());
                        DatabaseReference myRef2 = database.getReference("notifications").child(FirebaseAuth.getInstance().getUid()).child(notification.getNotificationId());
                        myRef2.removeValue();
                        myRef.push().setValue(newNotification);
                        Intent intent = new Intent(mContext.getApplicationContext(), BottomNavActivity.class);
                        mContext.startActivity(intent);

                    }
                });

                holder.declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Notifications newNotification = new Notifications(FirebaseAuth.getInstance().getUid(), notification.getSenderUserId(), "declined" , FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                                , null,notification.getRide());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("notifications").child(newNotification.getTargetUid());
                        DatabaseReference myRef2 = database.getReference("notifications").child(FirebaseAuth.getInstance().getUid()).child(notification.getNotificationId());
                        myRef2.removeValue();
                        Intent intent = new Intent(mContext.getApplicationContext(), BottomNavActivity.class);
                        myRef.push().setValue(newNotification);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }
                });
            }



        }
        else{
            holder.notificationType.setText("Friend request");
            holder.imageButtonRide.setVisibility(View.INVISIBLE);
            holder.senderRide.setVisibility(View.INVISIBLE);

            if(notification.getCurrentState().equals("accepted")){
                holder.notificationType.setText("Friend Request Accepted");
                holder.acceptButton.setVisibility(View.INVISIBLE);
                holder.declineButton.setVisibility(View.INVISIBLE);
                holder.senderName.setText("Friend: " + notification.getName());
                holder.senderItemPP.setVisibility(View.INVISIBLE);


            }
            else if(notification.getCurrentState().equals("declined")){
                holder.notificationType.setText("Friend Request Declined");
                holder.acceptButton.setVisibility(View.INVISIBLE);
                holder.declineButton.setVisibility(View.INVISIBLE);
                holder.senderName.setText(notification.getName());
                holder.senderItemPP.setVisibility(View.INVISIBLE);
                holder.senderRide.setVisibility(View.INVISIBLE);
                holder.imageButtonRide.setVisibility(View.INVISIBLE);
            }

            else{
                holder.notificationType.setText("Friend request");
                holder.senderName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String uid = notification.getSenderUserId();
                        Intent intent = new Intent(mContext.getApplicationContext(),TargetProfileActivity.class);
                        intent.putExtra("uid",uid);
                        mContext.startActivity(intent);
                    }
                });

                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Notifications newNotification = new Notifications(FirebaseAuth.getInstance().getUid(), notification.getSenderUserId(), "accepted" , FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                                , null,null);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("notifications").child(newNotification.getTargetUid());
                        DatabaseReference myRef2 = database.getReference("notifications").child(FirebaseAuth.getInstance().getUid()).child(notification.getNotificationId());
                        myRef2.removeValue();
                        myRef.push().setValue(newNotification);
                        Intent intent = new Intent(mContext.getApplicationContext(), BottomNavActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();

                    }
                });

                holder.declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Notifications newNotification = new Notifications(FirebaseAuth.getInstance().getUid(), notification.getSenderUserId(), "declined" , FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                                , null,null);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("notifications").child(newNotification.getTargetUid());
                        DatabaseReference myRef2 = database.getReference("notifications").child(FirebaseAuth.getInstance().getUid()).child(notification.getNotificationId());
                        myRef2.removeValue();
                        Intent intent = new Intent(mContext.getApplicationContext(), BottomNavActivity.class);
                        myRef.push().setValue(newNotification);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }
                });
            }
        }

    }

    public void setList( ArrayList<Notifications> notifications ){
        this.notifications = notifications;
        //this.requests = requests;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


}
