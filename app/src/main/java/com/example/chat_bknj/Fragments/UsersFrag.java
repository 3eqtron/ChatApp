package com.example.chat_bknj.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chat_bknj.Adapter.UserAdapter;
import com.example.chat_bknj.R;
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


public class UsersFrag extends Fragment {

private RecyclerView recyclerView;
private UserAdapter userAdapter;
private List<users>muser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_users,container,false);
       recyclerView=view.findViewById(R.id.recyclerview);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       muser=new ArrayList<>();
       readUsers();
        return view;
    }
    public void readUsers(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                muser.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    users user =snapshot1.getValue(users.class);
                    assert user!=null;
                    assert firebaseUser !=null;
if (!user.getId().equals(firebaseUser.getUid())){
    muser.add(user);
                    }
                }
                userAdapter=new UserAdapter(getContext(),muser,false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
