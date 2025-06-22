package com.example.hanoi_fixlt.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hanoi_fixlt.R;

public class Accountinformation extends AppCompatActivity {
    Button btnRegister, btnLogin;
    ImageButton imageNotifications, imageAvatar;
    LinearLayout layoutButton, layoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accountinformation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnRegister = findViewById(R.id.btnReport);
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
    }
}