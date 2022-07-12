package com.example.chat_bknj.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_bknj.MessageActivity;
import com.example.chat_bknj.R;
import com.example.chat_bknj.model.Chat;
import com.example.chat_bknj.model.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int Message_type_left=0;
    public static final int Message_type_right=1;
    private Context mcontext;
    private List<Chat> mchat;
    private String imageURL;
    FirebaseUser firebaseUser;
    public MessageAdapter(Context mcontext,List<Chat> mchat,String imageURL){
        this.mcontext=mcontext;
        this.mchat=mchat;
        this.imageURL=imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Message_type_right) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
Chat chat=mchat.get(position);
holder.showmessage.setText(chat.getMessage());
if (imageURL.equals("default")){
    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
}
else {
    Glide.with(mcontext).load(imageURL).into(holder.profile_image);
}
if (position == mchat.size()-1){
    if (chat.isIsseen()){
        holder.tx_seen.setText("seen");
    }else {
        holder.tx_seen.setText("Delivered");
    }

}else {
holder.tx_seen.setVisibility(View.GONE);
}
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showmessage;
        public ImageView profile_image;
        public TextView tx_seen;
        public ViewHolder(View itemview){
            super(itemview);
            showmessage=itemview.findViewById(R.id.showmessage);
            profile_image=itemview.findViewById(R.id.profile);
            tx_seen=itemview.findViewById(R.id.tx_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
if (mchat.get(position).getSender().equals(firebaseUser.getUid())){
    return Message_type_right;
}else {
    return Message_type_left;
}
    }
}
