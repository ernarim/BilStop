package com.example.bilstop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.ChatActivity;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsRVAdapter extends RecyclerView.Adapter<ChatsRVAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context context;

    public ChatsRVAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsRVAdapter.ViewHolder holder, int position) {
        Users user = list.get(position);
        if (!user.getProfilePicture().equals("null")) {
            Picasso.get().load(user.getProfilePicture()).into(holder.pp);
        }
        holder.name.setText(user.getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext() , ChatActivity.class);
                intent.putExtra("uid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<Users> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView pp;
        TextView name;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.civChatsPP);
            name = itemView.findViewById(R.id.txtChatsName);
            card = itemView.findViewById(R.id.cdvChats);
        }
    }

}
