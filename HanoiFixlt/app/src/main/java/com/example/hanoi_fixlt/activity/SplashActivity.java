package com.example.hanoi_fixlt.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.hanoi_fixlt.MainActivity;
import com.example.hanoi_fixlt.R;

public class SplashActivity extends AppCompatActivity {

    // Thời gian hiển thị màn hình splash (ví dụ: 2 giây)
    private static final int SPLASH_TIME_OUT = 2000; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Đặt layout cho Activity này

        // Sử dụng Handler để trì hoãn việc chuyển sang MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Tạo Intent để chuyển sang MainActivity
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i); // Bắt đầu MainActivity

            // Kết thúc SplashActivity để người dùng không thể quay lại
            finish();
        }, SPLASH_TIME_OUT);
    }
}
