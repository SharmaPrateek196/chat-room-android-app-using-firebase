package com.example.prateek_sharma;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    Context mCtx;
    ArrayList<MessagePojo> list;

    public ChatAdapter(){}

    public ChatAdapter(Context mCtx, ArrayList<MessagePojo> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf=LayoutInflater.from(mCtx);
        View v=inf.inflate(R.layout.msg_card_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final MessagePojo messagePojo=list.get(position);
        holder.tv_sender.setText(messagePojo.getSender());
        holder.tv_message.setText(messagePojo.getMessage());
        long timeInLong=messagePojo.getTime();
        String timeInStr= DateFormat.format("dd-MM-yyyy HH:mm:ss",timeInLong).toString();
        holder.tv_time.setText(timeInStr);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_sender,tv_message,tv_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message=(TextView) itemView.findViewById(R.id.tv_msg);
            tv_sender=(TextView) itemView.findViewById(R.id.tv_sender_name);
            tv_time=(TextView) itemView.findViewById(R.id.tv_time);
        }
    }

}

