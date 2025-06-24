package com.example.hanoi_fixlt.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageButton imageNotifications, imageAvatar;
    LinearLayout layoutButton, layoutImage;
    String userId;

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Xoá toàn bộ dữ liệu
        editor.commit(); // dùng commit() thay vì apply() để chắc chắn ghi ngay
    }

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
        imageNotifications = findViewById(R.id.imageNotifications);
        imageAvatar = findViewById(R.id.imageAvatar);
        layoutButton = findViewById(R.id.layoutButton);
        layoutImage = findViewById(R.id.layoutImage);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("isLoggedIn", false);
        String role = prefs.getString("rolename", "");

        SharedPreferences prefs1 = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs1.getString("userId", "");

        if (loggedIn) {
            if ("Admin".equals(role)) {
                // Chuyển sang trang dành cho Admin
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        }

        Log.d("DEBUG_LOGIN", "MainActivity isLoggedIn = " + loggedIn + ", Role = " + role);

        viewPager.setAdapter(new ViewPagerAdapter(this, loggedIn));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Trang chủ");
                    break;
                case 1:
                    tab.setText("Gửi báo cáo");
                    break;
                case 2:
                    tab.setText("Tra cứu");
                    break;
                case 3:
                    tab.setText("Bản đồ");
                    break;
            }
        }).attach();

        if(loggedIn){
            layoutButton.setVisibility(GONE);
            btnRegister.setVisibility(GONE);
            btnLogin.setVisibility(GONE);
            layoutImage.setVisibility(VISIBLE);
            imageNotifications.setVisibility(VISIBLE);
            imageAvatar.setVisibility(VISIBLE);
        }
        else{
            layoutButton.setVisibility(VISIBLE);
            btnRegister.setVisibility(VISIBLE);
            btnLogin.setVisibility(VISIBLE);
            layoutImage.setVisibility(GONE);
            imageNotifications.setVisibility(GONE);
            imageAvatar.setVisibility(GONE);
        }

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

        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_dropdown, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();

                    if (id == R.id.item1) {
                        Intent intent = new Intent(MainActivity.this, Accountinformation.class);
                        intent.putExtra("userId", userId); // truyền userId
                        startActivity(intent);
                        return true;
                    } else if (id == R.id.item2) {
                        Intent intent = new Intent(MainActivity.this, Myreport.class);
                        intent.putExtra("userId", userId); // truyền userId
                        startActivity(intent);
                        return true;
                    } else if (id == R.id.item3) {
                        Intent intent = new Intent(MainActivity.this, Changepassword.class);
                        startActivity(intent);
                        return true;
                    } else if (id == R.id.item4) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear(); // hoặc remove("isLoggedIn") nếu chỉ muốn xóa 1 mục
                        editor.apply();
                        layoutButton.setVisibility(VISIBLE);
                        btnRegister.setVisibility(VISIBLE);
                        btnLogin.setVisibility(VISIBLE);
                        layoutImage.setVisibility(GONE);
                        imageNotifications.setVisibility(GONE);
                        imageAvatar.setVisibility(GONE);
                        viewPager.setCurrentItem(0, false);
                        // Reset adapter (nếu tab Gửi báo cáo dùng adapter khác nhau)
                        viewPager.setAdapter(new ViewPagerAdapter(MainActivity.this, false));

                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });
    }
}