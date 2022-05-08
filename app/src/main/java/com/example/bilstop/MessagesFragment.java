package com.example.bilstop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Adapters.ChatsRVAdapter;
import com.example.bilstop.Classes.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {
    private RecyclerView chatsRec;
    private ChatsRVAdapter chatsRVAdapter;
    private ArrayList<Users> chatsList;
    private ArrayList<String> uids;

    private boolean getInfoDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        init(view);
        getInfo();
        return view;
    }

    private void init(View view) {
        //RecyclerView
        chatsRec = view.findViewById(R.id.rcvChats);
        chatsList = new ArrayList<>();
        uids = new ArrayList<>();
        chatsRVAdapter = new ChatsRVAdapter(chatsList,getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        chatsRec.setAdapter(chatsRVAdapter);
        chatsRec.setLayoutManager(layoutManager);
        getInfoDone = false;
    }

    private void getInfo(){
        String currentUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() ){
                    for( DataSnapshot dataSnapshot : snapshot.getChildren() ){
                        String uid = dataSnapshot.getKey();
                        uids.add(uid);
                    }
                    getInfoDone = true;
                }
                if( getInfoDone && uids.size() > 0 ){
                    for( String uid : uids ){
                        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("id").equalTo(uid);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.exists()){
                                    Users user = snapshot.getValue(Users.class);
                                    chatsList.add(user);
                                }
                                chatsRVAdapter.notifyDataSetChanged();

                                //TEST
                                for( Users users : chatsList ){
                                    System.out.println(users.getId());
                                }
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}