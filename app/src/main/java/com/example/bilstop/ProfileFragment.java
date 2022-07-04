package com.example.bilstop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Adapters.FriendsRVAdapter;
import com.example.bilstop.Classes.Car;
import com.example.bilstop.Classes.Friends;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.DataPickers.AdapterActivityNotifications;
import com.example.bilstop.DataPickers.RideDataPicker;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private ArrayList<Users> friendsList;
    private ArrayList<String> uids;
    private FriendsRVAdapter friendsRVAdapter;
    private boolean getInfoDone;

    private CircleImageView profilePicture, carPP;
    private ImageButton edit, signOut, notifications, friendsAdd, carAdd, addCarButton, delete, editCar;
    private RecyclerView friends, cars;
    private TextView name, email, about;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private Car car;

    private View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        getInfo();
        action();
        return view;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void init() {
        //Views
        profilePicture = view.findViewById(R.id.civCurrentPP);
        edit = view.findViewById(R.id.imbCurrentEdit);
        friendsAdd = view.findViewById(R.id.imbCurrentFriendsAdd);
        signOut = view.findViewById(R.id.imbCurrentSignOut);
        name = view.findViewById(R.id.txtCurrentName);
        email = view.findViewById(R.id.txtCurrentEmail);
        about = view.findViewById(R.id.txtCurrentAbout);
        addCarButton = view.findViewById(R.id.add_car_button);
        delete = view.findViewById(R.id.delete);
        editCar = view.findViewById(R.id.editCar);
        notifications = view.findViewById(R.id.notificationsImageButton);
        carPP = view.findViewById(R.id.carItemPP);

        BadgeDrawable badgeDrawable =  BadgeDrawable.create(getContext());
        badgeDrawable.setNumber(3);
        badgeDrawable.setVisible(true);
        BadgeUtils.attachBadgeDrawable(badgeDrawable, notifications);

        //Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(auth.getUid());

        //RecyclerView9
        friends = view.findViewById(R.id.rcvFriends);
        friendsList = new ArrayList<>();
        uids = new ArrayList<>();
        friendsRVAdapter = new FriendsRVAdapter(friendsList, getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        friends.setAdapter(friendsRVAdapter);
        friends.setLayoutManager(layoutManager);
        getInfoDone = false;
    }

    private void getInfo() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                name.setText(currentUser.getName() + " " + currentUser.getSurname());
                email.setText(currentUser.getEmail());
                about.setText(currentUser.getAbout());
                if (!currentUser.getProfilePicture().equals("null")) {
                    Picasso.get().load(currentUser.getProfilePicture()).into(profilePicture);
                }
                reference.child("friends").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Friends friends = dataSnapshot.getValue(Friends.class);
                                uids.add(friends.getId());
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
                                            friendsList.add(user);
                                        }
                                        friendsRVAdapter.notifyDataSetChanged();

                                        //TEST
                                        for( Users users : friendsList ){
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
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


    }

    private void action() {
        friendsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TEST

            }
        });
        CardView cardView = view.findViewById(R.id.car_card);
        ArrayList<String> carValues = new ArrayList<>();
        reference.child("car").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView textView1 = view.findViewById(R.id.car_name);
                TextView textView2 = view.findViewById(R.id.car_color);
                if(snapshot.exists()){

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String value = dataSnapshot.getValue(String.class);
                            carValues.add(value);
                        }

                    if(carValues.size() == 5){
                        car = new Car(carValues.get(0), carValues.get(1), carValues.get(2), carValues.get(3), carValues.get(4));
                        textView1.setText(car.getBrand() + " " + car.getModel());
                        textView2.setText(car.getLicencePlate());
                        cardView.setVisibility(View.VISIBLE);
                        addCarButton.setVisibility(View.INVISIBLE);
                        Picasso.get().load(car.getPp()).into(carPP);
                    }
                }
                else{

                    cardView.setVisibility(View.GONE);
                    addCarButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_profileEditFragment);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addCarFragment);
            }
        });

        editCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_addCarFragment);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("car").removeValue();
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdapterActivityNotifications.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}