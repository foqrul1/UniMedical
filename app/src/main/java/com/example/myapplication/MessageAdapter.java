package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        //  Toast.makeText(mContext, "MessageAdapter", Toast.LENGTH_SHORT).show();
        Chat chat = mChat.get(position);
        String image = chat.getMessage();
        if(chat.getType().equals("image")){

            holder.imageMessage.setVisibility(View.VISIBLE);
            holder.show_message.setVisibility(View.GONE);



            Glide.with(mContext).load(image).placeholder(R.drawable.profile_pic).into(holder.imageMessage);

        }
        else if(chat.getType().equals("docs")){

            //ic_docs

            holder.show_message.setVisibility(View.GONE);
            holder.imageMessage.setVisibility(View.VISIBLE);
            holder.imageMessage.setImageDrawable(mContext.getDrawable(R.drawable.ic_doc__1_));

            holder.imageMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Open for downloading...

                    System.out.println("Log. ok Downloading Link,,, " + chat.getMessage());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(chat.getMessage()));
                    mContext.startActivity(browserIntent);

                }
            });



        }
        else {


            holder.imageMessage.setVisibility(View.GONE);
            holder.show_message.setVisibility(View.VISIBLE);
            holder.show_message.setText(chat.getMessage());
        }

        if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.profile_pic);
        } else {

            Glide.with(mContext)
                    .load(imageurl)
                    .placeholder(R.drawable.profile_pic)
                    .into(holder.profile_image);
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Sent");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }



    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image,imageMessage;
        public TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            imageMessage = itemView.findViewById(R.id.imageMsg);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }
    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}