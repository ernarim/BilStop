package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bilstop.Adapters.FriendsRVAdapter;
import com.example.bilstop.Adapters.NotificationsRVAdapter;
import com.example.bilstop.Adapters.RidesRVAdapter;
import com.example.bilstop.ChatActivity;
import com.example.bilstop.Classes.Car;
import com.example.bilstop.Classes.Notifications;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.DataPickers.RideDataPicker;
import com.example.bilstop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
@SuppressWarnings("all")
public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Notifications> notifications = new ArrayList<Notifications>();
    private NotificationsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        init();
        getInfo();
        action();

    }

    private void init(){
        if(notifications!=null){
            notifications = new ArrayList<Notifications>(RideDataPicker.getNotifications());
        }
        adapter = new NotificationsRVAdapter(NotificationsActivity.this, notifications);

        Log.d("ridesActivity", notifications.toString());

        recyclerView = (RecyclerView) findViewById(R.id.rvNotifications);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    private void action(){

    }


    private void getInfo(){





    }



}