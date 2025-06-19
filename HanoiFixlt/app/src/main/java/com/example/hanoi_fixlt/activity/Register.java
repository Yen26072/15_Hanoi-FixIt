package com.example.hanoi_fixlt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText edtFullName, edtPhone, edtPassword, edtPassword2;
    Button btnRegister;
    TextView txtError;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = databaseRef.child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullName = findViewById(R.id.edtNameRegister);
        edtPhone = findViewById(R.id.edtPhoneRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        btnRegister = findViewById(R.id.btnRegisterRegister);
        edtPassword2 = findViewById(R.id.edtPasswordRegister2);
        txtError = findViewById(R.id.txtError);

        btnRegister.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String password2 = edtPassword2.getText().toString().trim();

            if (fullName.isEmpty() || password.isEmpty() || phone.isEmpty() || password2.isEmpty()) {
                txtError.setText("Vui lòng nhập đầy đủ thông tin");
                txtError.setVisibility(View.VISIBLE);
                return;
            }
            else {
                if(!password.equals(password2)){
                    txtError.setText("Vui lòng kiểm tra lại mật khẩu");
                    txtError.setVisibility(View.VISIBLE);
                    return;
                }
                else {
                    txtError.setVisibility(View.GONE);
                }
            }

            if (!isValidPhoneNumber(phone)) {
                txtError.setText("Số điện thoại không đúng định dạng");
                txtError.setVisibility(View.VISIBLE);
                return;
            }

            String passwordHash = hashPassword(password);
            if (passwordHash.isEmpty()) {
                Log.e("DEBUG", "❌ passwordHash bị null");
            }
            String avatarUrl = "https://i.postimg.cc/1569DNqZ/avatar.png"; // Avatar mặc định


            Log.d("DEBUG", "🔄 1111Firebase");
            String key =usersRef.push().getKey(); // Firebase tự sinh ID
            if (key.isEmpty()) {
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
            checkPhoneExistsAndRegister(user);
        });
    }

    private void checkPhoneExistsAndRegister(User user){
            // Đảm bảo usersRef đã được khởi tạo
            if (usersRef == null) {
                usersRef = FirebaseDatabase.getInstance().getReference("Users");
            }
            // Kiểm tra xem user và số điện thoại có hợp lệ không
            if (user != null && user.getPhoneNumber() != null) {
                usersRef.orderByChild("phoneNumber")
                        .equalTo(user.getPhoneNumber())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    txtError.setText("Số điện thoại đã tồn tại");
                                    txtError.setVisibility(View.VISIBLE);
                                } else {
                                    txtError.setVisibility(View.GONE);
                                    createNewUser (user);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Register.this, "Lỗi kết nối Firebase", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Dữ liệu người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            }
    }

    private void createNewUser(User user) {
        usersRef.child(user.getUserId()).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Lỗi ghi dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^0\\d{9}$");//bắt đầu là số 0, theo sau là 9 chữ số
    }
}
