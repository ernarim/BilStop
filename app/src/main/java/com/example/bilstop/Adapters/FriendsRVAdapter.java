package com.example.bilstop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.ChatActivity;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.R;
import com.example.bilstop.TargetProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsRVAdapter extends RecyclerView.Adapter<FriendsRVAdapter.ViewHolder> {
    public ArrayList<Users> list;
    public Context context;

    public FriendsRVAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = list.get(position);
        if(!user.getProfilePicture().equals("null")){
            Picasso.get().load(user.getProfilePicture()).into(holder.pp);
        }
        holder.name.setText(user.getName() + " " + user.getSurname());

        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uid",user.getId());
                context.startActivity(intent);

            }
        });

        holder.seeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TargetProfileActivity.class);
                intent.putExtra("uid",user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList( ArrayList<Users> list ){
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView pp;
        TextView name;
        ImageButton seeProfile, sendMessage;

        public ViewHolder(View itemView){
            super(itemView);
            pp = itemView.findViewById(R.id.senderItemPP);
            name = itemView.findViewById(R.id.senderName);
            seeProfile = itemView.findViewById(R.id.friendsItemLook);
            sendMessage = itemView.findViewById(R.id.friendsItemMessage);
        }
    }

}
