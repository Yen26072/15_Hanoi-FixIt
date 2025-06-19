package com.example.hanoi_fixlt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hanoi_fixlt.fragment.HomeFragment;
import com.example.hanoi_fixlt.fragment.MapFragment;
import com.example.hanoi_fixlt.fragment.ReportFragmentLoggedIn;
import com.example.hanoi_fixlt.fragment.ReportFragmentGuest;
import com.example.hanoi_fixlt.fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private boolean loggedIn;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, boolean loggedIn) {
        super(fragmentActivity);
        this.loggedIn = loggedIn;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (loggedIn) {
            // Fragment cho người đã đăng nhập
            switch (position) {
                case 0: return new HomeFragment();
                case 1: return new ReportFragmentLoggedIn(); // Fragment đã đăng nhập
                case 2: return new SearchFragment();
                case 3: return new MapFragment();
            }
        } else {
            // Fragment cho người chưa đăng nhập
            switch (position) {
                case 0: return new HomeFragment();
                case 1: return new ReportFragmentGuest(); // Fragment chưa đăng nhập
                case 2: return new SearchFragment();
                case 3: return new MapFragment();
            }
        }
        return new HomeFragment();
    }

    @Override
    public boolean containsItem(long itemId) {
        return loggedIn ? (itemId >= 0 && itemId < 4) : (itemId >= 100 && itemId < 104);
    }

    @Override
    public long getItemId(int position) {
        return loggedIn ? position : position + 100;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
