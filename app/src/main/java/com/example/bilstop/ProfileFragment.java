package com.example.bilstop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Adapters.FriendsRVAdapter;
import com.example.bilstop.Classes.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private ArrayList<Users> friendsList;
    private FriendsRVAdapter friendsRVAdapter;

    private CircleImageView profilePicture;
    private ImageButton edit, notifications, friendsAdd, carAdd;
    private RecyclerView friends, cars;
    private TextView name, email, about;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        getInfo();
        action();






        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_profileEditFragment);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void init(View view){
        friends = view.findViewById(R.id.rcvFriends);
        friendsList = new ArrayList<>();
        friendsRVAdapter = new FriendsRVAdapter();
        friendsRVAdapter.setList(friendsList);
        friends.setAdapter(friendsRVAdapter);
        friends.setLayoutManager(new LinearLayoutManager(getContext()));

        profilePicture = view.findViewById(R.id.civCurrentPP);
        edit = view.findViewById(R.id.imbCurrentEdit);
        friendsAdd = view.findViewById(R.id.imbCurrentFriendsAdd);
        friends = view.findViewById(R.id.rcvFriends);
        name = view.findViewById(R.id.txtCurrentName);
        email = view.findViewById(R.id.txtCurrentEmail);
        about = view.findViewById(R.id.txtCurrentAbout);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(auth.getUid());
    }

    private void getInfo(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                name.setText(currentUser.getName() + " " + currentUser.getSurname());
                email.setText(currentUser.getEmail());
                about.setText(currentUser.getAbout());
                if( !currentUser.getProfilePicture().equals("null") ){
                    Picasso.get().load(currentUser.getProfilePicture()).into(profilePicture);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });

        friendsList.clear();
        ArrayList<String> uids = new ArrayList<>();
        reference.child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for( DataSnapshot dataSnapshot : snapshot.getChildren() ){
                        String uid = dataSnapshot.child("id").getValue().toString();
                        Log.i("CATCAT",uid);
                        uids.add(uid);
                    }
                }

                for( String uid : uids ){
                    Log.i("CATCAT","hey");//test
                    Query query = database.getReference().child("users").orderByChild("id").equalTo(uid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for( DataSnapshot dataSnapshot : snapshot.getChildren() ){
                                Users us = dataSnapshot.getValue(Users.class);
                                Log.i("CATCAT",us.getName());//test
                                friendsList.add(us);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
        friendsRVAdapter.setList(friendsList);
    }

    private void action(){
        friendsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("friends").push().child("id").setValue("mK0hIFaN7pWvvNH2T3fTM8BNLOs1");
                reference.child("friends").push().child("id").setValue("r0eRpd3A5YMb3F2MkffIxfXUen83");
                getInfo();
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOGIN","login");
                auth.signOut();
                Intent intent = new Intent( getContext() , MainActivity.class );
                startActivity(intent);
                getActivity().finish();
            }
        });

        Button btn2 = view.findViewById(R.id.test2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String burakdemirel49 = "mK0hIFaN7pWvvNH2T3fTM8BNLOs1";
                String mehmet = "r0eRpd3A5YMb3F2MkffIxfXUen83";
                String mikail = "WoPIDUuVVobtNiR3xp1qg7xnGM82";
                Intent intent = new Intent(getActivity(), TargetProfileActivity.class);
                intent.putExtra("uid",mikail);
                startActivity(intent);
            }
        });
    }
}