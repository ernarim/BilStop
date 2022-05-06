package com.example.bilstop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bilstop.Classes.Message;
import com.example.bilstop.Classes.Users;
import com.example.bilstop.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesRVAdapter extends RecyclerView.Adapter<MessagesRVAdapter.ViewHolder> {
    ArrayList<Message> messageList;
    String currentUid, targetUid;
    final int SENT = 1;
    final int RECIEVED = 2;
    boolean state;

    public MessagesRVAdapter(){
        currentUid = FirebaseAuth.getInstance().getUid();
        state = false;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if( viewType == SENT ){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent, parent , false);
            return new ViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recieved, parent , false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.message.setText(messageList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setList( ArrayList<Message> messageList ){
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getFrom().equals(currentUid)){
            state = true;
            return SENT;
        }
        else{
            state = false;
            return RECIEVED;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message;

        public ViewHolder(View itemView){
            super(itemView);
            if (state){
                message = itemView.findViewById(R.id.txtMessageSent);
            }
            else{
                message = itemView.findViewById(R.id.txtMessageReceived);
            }
        }
    }
}
