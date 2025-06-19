package com.example.hanoi_fixlt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hanoi_fixlt.fragment.HomeFragment;
import com.example.hanoi_fixlt.fragment.LookupFragment;
import com.example.hanoi_fixlt.fragment.MapFragment;
import com.example.hanoi_fixlt.fragment.ReportFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new HomeFragment();
            case 1: return new ReportFragment();
            case 2: return new LookupFragment();
            case 3: return new MapFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
