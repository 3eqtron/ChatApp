package com.example.chat_bknj.Fragments;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_bknj.Adapter.UserAdapter;
import com.example.chat_bknj.LoadingDialog;
import com.example.chat_bknj.NetworkChangeReciever;
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

import java.util.ArrayList;
import java.util.List;

public class Chatsfrag extends Fragment {

private RecyclerView recyclerView;
private UserAdapter userAdapter;
private List<users> mUsers;
FirebaseUser firebaseUser;
DatabaseReference reference;
private List<String>userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chatsfrag, container, false);
recyclerView=view.findViewById(R.id.recyclerhats);
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
userList=new ArrayList<>();

reference= FirebaseDatabase.getInstance().getReference("chats");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        userList.clear();

        for (DataSnapshot snapshot1:snapshot.getChildren()){
            Chat chat=snapshot1.getValue(Chat.class);
            if (chat.getSender().equals(firebaseUser.getUid())){
                userList.add(chat.getReciever());
            }
            if (chat.getReciever().equals(firebaseUser.getUid())){
                userList.add(chat.getSender());
            }
        }
        readchats();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        return view;
    }
  public void   readchats(){
mUsers =new ArrayList<>();
reference=FirebaseDatabase.getInstance().getReference("users");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        mUsers.clear();
  for (DataSnapshot snapshot1:snapshot.getChildren()){
      users user=snapshot1.getValue(users.class);
      
      for (String id:userList){
          if (user.getId().equals(id)){
              if (mUsers.size()!=0){
                  for (users user1:mUsers){
                      if (!user.getId().equals(user1.getId())){
                          mUsers.add(user);
                      }
                  }
              }else {
                  mUsers.add(user);
              }
          }
      }
  }
  userAdapter = new UserAdapter(getContext(),mUsers,true);
  recyclerView.setAdapter(userAdapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }

}
