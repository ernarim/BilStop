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
    private String senderUserId, targetUid, currentState;
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
        this.currentState = "not_friends";
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
                if (currentState.equals("not_friends"))
                {
                    sendFriendRequestToSomeone();
                }
                if (currentState.equals("request_sent"))
                {
                    cancelFriendRequest();
                }
            }
        });
    }

    private void sendFriendRequestToSomeone() {
        friendRequestReference.child(senderUserId).child(targetUid)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            friendRequestReference.child(targetUid).child(senderUserId)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                addFriend.setEnabled(true);
                                                currentState = "request_sent";
                                                addFriend.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                                                //arkaplanın siyah olması gerek
                                                Toast.makeText(getApplicationContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void cancelFriendRequest() {

        friendRequestReference.child(senderUserId).child(targetUid)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            friendRequestReference.child(targetUid).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                addFriend.setEnabled(true);
                                                currentState = "not_friends";
                                                addFriend.setBackgroundResource(R.drawable.ic_baseline_person_add_24);
                                                //arkaplanın siyah olması gerek
                                                Toast.makeText(getApplicationContext(), "Friend request cancelled", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

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

                maintananceOfButtons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void maintananceOfButtons() {
        friendRequestReference.child(senderUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(targetUid))
                        {
                            String request_type = snapshot.child(targetUid).child("request_type").getValue().toString();

                            if (request_type.equals("sent"))
                            {
                                currentState = "request_sent";
                                addFriend.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
                                //arkaplanın siyah olması gerek
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}