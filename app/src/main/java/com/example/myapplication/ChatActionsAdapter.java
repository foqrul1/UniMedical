package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatActionsAdapter extends RecyclerView.Adapter<ChatActionsAdapter.PhotoViewHolder> {

    private List<String> chatActions;
    private Context context;


    private ItemClickListener itemClickListener;

    public ChatActionsAdapter(List<String> photosList, Context context) {
        this.chatActions = photosList;
        this.context = context;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_actions, parent, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.mButton.setText(chatActions.get(position));
    }

    @Override
    public int getItemCount() {
        return chatActions.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button mButton;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mButton=itemView.findViewById(R.id.btn_action_chat);
            itemView.setOnClickListener(this);
            mButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
