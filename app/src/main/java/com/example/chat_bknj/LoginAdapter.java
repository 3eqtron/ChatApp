package com.example.chat_bknj;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
private Context context;
int totalTabs;
public LoginAdapter(FragmentManager fm, Context context, int totalTabs){
    super(fm);
    this.context=context;
    this.totalTabs=totalTabs;
}

    @NonNull
    @Override
    public Fragment getItem(int position) {
    switch (position){
        case 0:LoginTabfrag loginTabfrag=new LoginTabfrag();
        return loginTabfrag;
        case 1:SignuotabFrag signuotabFrag =new SignuotabFrag();
            return signuotabFrag;
        default:
        return null;
    }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
