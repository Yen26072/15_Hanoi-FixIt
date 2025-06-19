package com.example.hanoi_fixlt.activity;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button btnRegister, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Register.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabText = customTab.findViewById(R.id.tabText);
            switch (position) {
                case 0: tabText.setText("Trang chủ"); break;
                case 1: tabText.setText("Gửi báo cáo"); break;
                case 2: tabText.setText("Tra cứu"); break;
                case 3: tabText.setText("Bản đồ"); break;
            }
            tab.setCustomView(customTab);
                }).attach();
        //Xóa padding của các tab trong TabLayout nhưng vẫn chưa được
        tabLayout.post(() -> {
            ViewGroup tabStrip = (ViewGroup) tabLayout.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                tabView.setPadding(0, 0, 0, 0);
                ViewGroup.LayoutParams params = tabView.getLayoutParams();
                params.height = dpToPx(48); // Ép chiều cao chuẩn
                tabView.setLayoutParams(params);

                // Ép tiếp custom view bên trong
                View customView = ((ViewGroup) tabView).getChildAt(0);
                if (customView instanceof TextView) {
                    customView.setPadding(0, 0, 0, 0);
                    ViewGroup.LayoutParams cp = customView.getLayoutParams();
                    cp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    customView.setLayoutParams(cp);
                }
            }
        });

    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}