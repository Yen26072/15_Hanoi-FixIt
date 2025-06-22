package com.example.hanoi_fixlt.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.adapter.ViewPagerAdapter;
import com.example.hanoi_fixlt.adapter.ViewPagerAdapter2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button btnRegister, btnLogin;
    ImageButton imageNotifications, imageAvatar;
    LinearLayout layoutButton, layoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        imageNotifications = findViewById(R.id.imageNotifications);
        imageAvatar = findViewById(R.id.imageAvatar);
        layoutButton = findViewById(R.id.layoutButton);
        layoutImage = findViewById(R.id.layoutImage);

        layoutButton.setVisibility(GONE);
        btnRegister.setVisibility(GONE);
        btnLogin.setVisibility(GONE);
        layoutImage.setVisibility(VISIBLE);
        imageNotifications.setVisibility(VISIBLE);
        imageAvatar.setVisibility(VISIBLE);

        viewPager.setAdapter(new ViewPagerAdapter2(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Quản lý báo cáo");
                    break;
                case 1:
                    tab.setText("Quản lý danh mục");
                    break;
                case 2:
                    tab.setText("Thống kê");
                    break;
            }
        }).attach();
    }
}