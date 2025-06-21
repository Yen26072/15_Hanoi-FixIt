package com.example.hanoi_fixlt.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hanoi_fixlt.fragment.HomeFragment;
import com.example.hanoi_fixlt.fragment.ListmanagementFragment;
import com.example.hanoi_fixlt.fragment.MapFragment;
import com.example.hanoi_fixlt.fragment.QuantityFragment;
import com.example.hanoi_fixlt.fragment.ReportFragmentLoggedIn;
import com.example.hanoi_fixlt.fragment.ReportmanagementFragment;
import com.example.hanoi_fixlt.fragment.SearchFragment;

public class ViewPagerAdapter2 extends FragmentStateAdapter {

    public ViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ReportmanagementFragment();
            case 1: return new ListmanagementFragment(); // Fragment đã đăng nhập
            case 2: return new QuantityFragment();
        }
        return new ReportmanagementFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
