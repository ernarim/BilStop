package com.example.bilstop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bilstop.Adapters.MessagesRVAdapter;
import com.example.bilstop.Classes.Message;
import com.example.bilstop.Classes.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    private EditText messageText;
    private FloatingActionButton send;
    private ImageButton back;
    private TextView name;
    private FirebaseDatabase database;
    private DatabaseReference usersReference, messagesReference;
    private String currentUid, targetUid;
    private ArrayList<Message> messages;
    private RecyclerView messagesRecyclerView;
    private MessagesRVAdapter messagesRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        getInfo();
        getMessages();
        action();
    }

    private void init(){
        messageText = findViewById(R.id.edtChatSendMessage);
        send = findViewById(R.id.fabChatSendMessage);
        back = findViewById(R.id.imbChatBack);
        name = findViewById(R.id.txtChatName);
        Bundle extras = getIntent().getExtras();
        if( extras != null ){
            targetUid = extras.getString("uid");
        }
        currentUid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("users");
        messagesReference= database.getReference().child("messages");
        messages = new ArrayList<>();
        messagesRecyclerView = findViewById(R.id.rcvMessages);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext() , 1);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRVAdapter = new MessagesRVAdapter();
        messagesRVAdapter.setList(messages);
        messagesRecyclerView.setAdapter(messagesRVAdapter);
    }

    private void getInfo(){
        usersReference.child(targetUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users targetUser = snapshot.getValue(Users.class);
                name.setText(targetUser.getName().toString() + " " + targetUser.getSurname());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    private void action(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = messageText.getText().toString();
                if( !text.isEmpty() ){
                    sendMessage(text,currentUid,targetUid,"text");
                    messageText.setText("");
                }
            }
        });
    }

    private void sendMessage(String text, String currentUid, String targetUid, String type ){
        String messageID = messagesReference.child(currentUid).child(targetUid).push().getKey();
        HashMap<String,Object> messageMap = new HashMap<>();
        messageMap.put("type",type);
        messageMap.put("text",text);
        messageMap.put("from",currentUid);

        messagesReference.child(currentUid).child(targetUid).child(messageID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                messagesReference.child(targetUid).child(currentUid).child(messageID).setValue(messageMap);
            }
        });
    }

    private void getMessages(){
        messagesReference.child(currentUid).child(targetUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                messages.add(message);
                messagesRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}