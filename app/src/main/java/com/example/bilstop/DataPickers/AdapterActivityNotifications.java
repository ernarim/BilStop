package com.example.bilstop.DataPickers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bilstop.BottomNavActivity;
import com.example.bilstop.Classes.Notifications;
import com.example.bilstop.Classes.Ride;
import com.example.bilstop.NotificationsActivity;
import com.example.bilstop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterActivityNotifications extends AppCompatActivity {

    private ArrayList<Notifications> notifications = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("notifications").child(FirebaseAuth.getInstance().getUid());

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_screen);


        Log.d("adapterActivity","open");
        Log.d("adapterActivity", FirebaseAuth.getInstance().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Notifications nf = dataSnapshot.getValue(Notifications.class);
                        String key= dataSnapshot.getKey();
                        nf.setNotificationId(key);
                        Log.d("nfId", nf.getNotificationId());
                        notifications.add(nf);
                    }
                    RideDataPicker.setNotifications(notifications);
                    Intent intent = new Intent(AdapterActivityNotifications.this, NotificationsActivity.class);
                    startActivity(intent);
                    finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
