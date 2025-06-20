package com.example.hanoi_fixlt.activity;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {
    EditText edtPhone, edtPass;
    TextView txtError;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtPhone = findViewById(R.id.edtPhoneLogin);
        edtPass = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLoginLogin);
        txtError = findViewById(R.id.txtErrorLogin);

        btnLogin.setOnClickListener(v ->{
            String phone = edtPhone.getText().toString().trim();//loại bỏ khoảng trắng đầu cuoi
            String passwordInput = edtPass.getText().toString().trim();
            String passwordHash = hashPassword(passwordInput);

            if (phone.isEmpty() || passwordHash.isEmpty()) {
                txtError.setText("Vui lòng nhập đầy đủ thông tin");
                txtError.setVisibility(View.VISIBLE);
                return;
            }
            else {
                txtError.setVisibility(View.GONE);
            }

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            //Tìm các bản ghi trong "Users" mà có trường "phoneNumber" bằng số điện thoại vừa nhập.
            usersRef.orderByChild("phoneNumber").equalTo(phone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {//Lắng nghe dữ liệu một lần duy nhất để kiểm tra xem người dùng có tồn tại không.
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {//snapshot là danh sách user phù hợp
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnap : snapshot.getChildren()) {//Firebase có thể trả về nhiều user (dù thường chỉ 1), dòng này sẽ duyệt qua từng bản ghi user trong snapshot.
                                    User user = userSnap.getValue(User.class);//Ép kiểu từng bản ghi Firebase thành một đối tượng User theo model đã định nghĩa
                                    if (user != null && user.getPasswordHash().equals(passwordHash)) {//so sánh passwordHash nhập từ người dùng trùng khớp với passwordHash đã lưu trong Firebase
                                        //Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        txtError.setVisibility(View.GONE);
                                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("userPhone", phone); // nếu cần
                                        editor.apply();
                                        String userId = userSnap.getKey(); // 🔍 ID tự sinh trong Firebase
                                        SharedPreferences prefs1 = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = prefs1.edit();
                                        editor1.putString("userId", userId); // ✔ lưu đúng ID
                                        editor1.apply();
                                        Intent intent = new Intent(Login.this, MainActivity.class);

                                        startActivity(intent);
                                        finish(); // Đóng màn login
                                        return;
                                    }
                                }
                                txtError.setText("Vui lòng nhập lại mật khẩu");
                                txtError.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(Login.this, "Không tìm thấy số điện thoại", Toast.LENGTH_SHORT).show();
                                txtError.setText("Không tìm thấy số điện thoại");
                                txtError.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Lỗi kết nối Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
    private static String hashPassword(String passwordInput) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passwordInput.getBytes(StandardCharsets.UTF_8));

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
}