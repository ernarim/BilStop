package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bilstop.Classes.Notifications;
import com.example.bilstop.Classes.Users;
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

import de.hdodenhof.circleimageview.CircleImageView;
@SuppressWarnings("all")
public class TargetProfileActivity extends AppCompatActivity {
    private CircleImageView profilePicture;
    private ImageButton addFriend, sendMessage;
    private TextView name, email, about;
    private String senderUserId, targetUid;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, friendRequestReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_profile);
        init();
        getInfo();
        action();
    }

    private void init(){
        this.profilePicture = findViewById(R.id.civTargetPP);
        this.addFriend = findViewById(R.id.btnTargetAddFriend);
        this.sendMessage = findViewById(R.id.btnTargetSendMessage);
        this.name = findViewById(R.id.txtTargetName);
        this.email = findViewById(R.id.txtTargetEmail);
        this.about = findViewById(R.id.txtTargetAbout);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            this.targetUid = extras.getString("uid");
        }
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference().child("users").child(targetUid);
        friendRequestReference = database.getReference().child("FriendRequests");

        mAuth = FirebaseAuth.getInstance();
        senderUserId = mAuth.getCurrentUser().getUid();
        //this.currentState = "not_friends";
    }

    private void action(){
        this.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("uid",targetUid);
                startActivity(intent);

            }
        });

        this.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFriend.setEnabled(false);
                addFriend.setVisibility(View.INVISIBLE);
                sendFriendRequestToSomeone();

            }
        });
    }

    private void sendFriendRequestToSomeone() {
        Notifications notification = new Notifications(FirebaseAuth.getInstance().getUid(), targetUid, "currentState" , FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                , null,null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("notifications").child(targetUid);
        reference.push().setValue(notification);


        //this.currentState = "request_sent";
    }



    private void getInfo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users targetUser = snapshot.getValue(Users.class);
                name.setText(targetUser.getName() + " " + targetUser.getSurname());
                email.setText(targetUser.getEmail());
                about.setText(targetUser.getAbout());
                if( !targetUser.getProfilePicture().equals("null") ){
                    Picasso.get().load(targetUser.getProfilePicture()).into(profilePicture);
                }

                //maintananceOfButtons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}