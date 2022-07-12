package com.example.chat_bknj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chat_bknj.Fragments.Chatsfrag;
import com.example.chat_bknj.Fragments.ProfileFragment;
import com.example.chat_bknj.Fragments.UsersFrag;
import com.example.chat_bknj.model.users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
CircleImageView circleImageView;
TextView username;
FirebaseUser firebaseUser;
    NetworkChangeReciever nerworkChangeReciever=new NetworkChangeReciever();
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        circleImageView=findViewById(R.id.circleimage);
        username=findViewById(R.id.username);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        users user=snapshot.getValue(users.class);
        username.setText(user.getName());
        if (user.getImageURL().equals("default")){
            circleImageView.setImageResource(R.mipmap.ic_launcher);

        }else {
            Glide.with(getApplicationContext()).load(user.getImageURL()).into(circleImageView);

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        TabLayout tabLayout=findViewById(R.id.tablayout1);
        ViewPager viewPager=findViewById(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Users"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Authentification.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                return true;
        }
        return false;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private Context context;
        int totalTabs;
        public ViewPagerAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
  this.context=context;
    this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:Chatsfrag chatsfrag=new Chatsfrag();
                return chatsfrag;
            case 1:UsersFrag usersFrag =new UsersFrag();
                return usersFrag;
            case 2:
                ProfileFragment profileFragment=new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
private void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
    HashMap <String ,Object> hashMap=new HashMap<>();
    hashMap.put("status",status);
    reference.updateChildren(hashMap);
}

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Offline");
    }
    @Override
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(nerworkChangeReciever,filter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(nerworkChangeReciever);
        super.onStop();
    }
}
