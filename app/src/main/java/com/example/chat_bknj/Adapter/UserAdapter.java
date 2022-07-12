package com.example.chat_bknj.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_bknj.MainActivity;
import com.example.chat_bknj.MessageActivity;
import com.example.chat_bknj.R;
import com.example.chat_bknj.model.Chat;
import com.example.chat_bknj.model.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mcontext;
    private List<users> muser;
    private Boolean ischat;
    String thelastmessage;
    public UserAdapter(Context mcontext,List<users> muser,Boolean ischat){
        this.mcontext=mcontext;
        this.muser=muser;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
final users user=muser.get(position);
holder.name.setText(user.getName());
if (user.getImageURL().equals("default")){
    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
}else {
    Glide.with(mcontext).load(user.getImageURL()).into(holder.profile_image);
}
if (ischat){
    lastmessage(user.getId(),holder.last_msg);
}
else {
    holder.last_msg.setVisibility(View.GONE);
}
if (ischat){
    if (user.getStatus().equals("online")){
        holder.img_on.setVisibility(View.VISIBLE);
        holder.img_off.setVisibility((View.GONE));
    }else {
        holder.img_off.setVisibility(View.VISIBLE);
        holder.img_on.setVisibility((View.GONE));
    }

}else {
    holder.img_off.setVisibility(View.GONE);
    holder.img_on.setVisibility((View.GONE));
}

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(mcontext, MessageActivity.class);
        String userid=user.getId();
        intent.putExtra("userid",userid);
       mcontext.startActivity(intent);


    }
});
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        public ViewHolder(View itemview){
            super(itemview);
            name=itemview.findViewById(R.id.name);
            profile_image=itemview.findViewById(R.id.profile_image);
            img_on=itemview.findViewById(R.id.img_on);
            img_off=itemview.findViewById(R.id.img_off);
            last_msg=itemview.findViewById(R.id.last_msg);
        }
    }
    private void lastmessage(String userid,TextView last_msg){
thelastmessage="default";
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat =snapshot1.getValue(Chat.class);
                    if (chat.getReciever().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)||
                     chat.getReciever().equals(userid)&& chat.getSender().equals(firebaseUser.getUid())){
thelastmessage=chat.getMessage();
                    }
                }
                switch (thelastmessage){
                    case "default" :last_msg.setText("No message");
                    break;
                    default:
                        last_msg.setText(thelastmessage);
                        break;
                }
                thelastmessage ="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
