package com.example.hanoi_fixlt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText edtFullName, edtPhone, edtPassword;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullName = findViewById(R.id.edtNameRegister);
        edtPhone = findViewById(R.id.edtPhoneRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegisterRegister);

        btnRegister.setOnClickListener(v -> registerUser());

        //FirebaseDatabase.getInstance("https://hanoi-fixit-36a1d-default-rtdb.firebaseio.com/").getReference("Users").setValue("123");

    }

    private void registerUser() {
        // ✅ Sử dụng URL Realtime Database đầy đủ
//        DatabaseReference usersRef= FirebaseDatabase.getInstance().getReference("Users");

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseRef.child("Users");


        String fullName = edtFullName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordHash="";
        passwordHash = Integer.toHexString(password.hashCode());
        if (passwordHash == "") {
            Log.e("DEBUG", "❌ passwordHash bị null");
        }
        String avatarUrl = "https://i.postimg.cc/1569DNqZ/avatar.png"; // Avatar mặc định


        Log.d("DEBUG", "🔄 1111Firebase");
        String key = "";
        key =usersRef.push().getKey(); // Firebase tự sinh ID
        if (key == "") {
            Log.e("DEBUG", "❌ key bị null");
        }
        Log.d("DEBUG", "🔄 22222Firebase");

        User user = new User(key, passwordHash, fullName, phone, avatarUrl);
        System.out.println("User info:");
        System.out.println("id = " + key);
        System.out.println("fullName = " + fullName);
        System.out.println("phone = " + phone);
        System.out.println("passwordHash = " + passwordHash);
        System.out.println("avatarUrl = " + avatarUrl);

        Log.d("DEBUG", "🔄 Bắt đầu ghi dữ liệu Firebase");


        usersRef.child(key).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DEBUG", "🟢 Đã ghi dữ liệu Firebase thành công");

                        Toast.makeText(Register.this, "✅ Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    } else {
                        Log.e("FIREBASE_ERROR", "❌ Exception: ", task.getException());
                        Log.e("DEBUG", "❌ Lỗi ghi dữ liệu Firebase: " + task.getException());
                        Toast.makeText(Register.this, "Lỗi khi ghi dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
