package com.example.hanoi_fixlt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button btnRegister, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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

// Khởi tạo ViewPagerAdapter và gán cho ViewPager2
        viewPager.setAdapter(viewPagerAdapter);
        boolean loggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        viewPager.setAdapter(new ViewPagerAdapter(this, loggedIn));
// Kết nối TabLayout và ViewPager2 bằng TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View customTab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabText = customTab.findViewById(R.id.tabText);

            switch (position) {
                case 0:
                    tabText.setText("Trang chủ");
                    break;
                case 1:
                    tabText.setText("Gửi báo cáo");
                    break;
                case 2:
                    tabText.setText("Tra cứu");
                    break;
                case 3:
                    tabText.setText("Bản đồ");
                    break;
            }

            tab.setCustomView(customTab);
        }).attach();
        // Đặt tab mặc định khi khởi động Activity
        int tabToOpen = getIntent().getIntExtra("tabToOpen", 0);
        viewPager.setCurrentItem(tabToOpen, false);
        // Xử lý sự kiện click cho các nút Đăng ký và Đăng nhập
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

//        viewPager.setAdapter(new ViewPagerAdapter(this, isLoggedIn()));
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//
//            tab.setText(getTabTitle(position)); // tiêu đề không đổi
//        }).attach();
    }

    private void refreshViewPager() {
        boolean loggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        viewPager.setAdapter(new ViewPagerAdapter(this, loggedIn));
        viewPager.setAdapter(viewPagerAdapter);
        // Giữ nguyên vị trí tab hiện tại
        int currentItem = viewPager.getCurrentItem();
        viewPager.setCurrentItem(currentItem, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("REFRESH_NEEDED", false)) {
                refreshViewPager();
            }
        }
    }
}