package com.example.hanoi_fixlt.activity;

import android.app.AlertDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.Report;
import com.example.hanoi_fixlt.viewmodel.SharedViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportDetailAdmin extends AppCompatActivity {

    private TextView txtAddress, txtDescription, txtStatus, txtCategory, txtReportId, txtSubmit, txtLastchange;
    private ImageView imgCategory;
    private Button btnXacnhan;
    private Spinner spinner;
    String selectedStatus = "";
    String reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_detail_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        txtAddress = findViewById(R.id.txtAdr);
        txtDescription = findViewById(R.id.txtDes);
        txtStatus = findViewById(R.id.txtStatus);
        txtCategory = findViewById(R.id.txtCategory);
        txtReportId = findViewById(R.id.txtReportId);
        txtSubmit = findViewById(R.id.txtSubmit);
        txtLastchange = findViewById(R.id.txtLastchange);
        imgCategory = findViewById(R.id.imageView5);
        btnXacnhan = findViewById(R.id.button);
        spinner = findViewById(R.id.spinner);

        // Danh sách trạng thái
        String[] statusList = {"Mới gửi", "Đang xử lý", "Đã xử lý", "Từ chối", "Trùng lặp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Lưu lựa chọn khi chọn spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statusList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnXacnhan.setOnClickListener(v -> {
            new AlertDialog.Builder(ReportDetailAdmin.this)
                    .setTitle("Xác nhận cập nhật")
                    .setMessage("Bạn có chắc muốn cập nhật trạng thái thành '" + selectedStatus + "'?")
                    .setPositiveButton("Có", (dialog, which) -> updateReportStatus())
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Lưu lựa chọn
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedStatus = statusList[position];
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });


        reportId = getIntent().getStringExtra("reportId");

        if (reportId != null) {
            DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("Reports").child(reportId);
            reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Report report = snapshot.getValue(Report.class);
                    if (report != null) {
                        txtCategory.setText("Danh mục: " + report.getCategoryId());
                        txtAddress.setText("Địa điểm: " + report.getAddressDetail());
                        txtDescription.setText("Nội dung: " + report.getDescription());
                        txtSubmit.setText("Thời gian báo cáo: " + report.getSubmittedAt());
                        txtStatus.setText("Trạng thái: " + report.getStatus());
                        txtCategory.setText("Danh mục: " + report.getCategoryId());
                        txtLastchange.setText("Lần thay đổi cuối: " + report.getLastUpdatedAt());
                        txtReportId.setText("ID báo cáo: " + report.getReportId());

                        DatabaseReference categoryRef = FirebaseDatabase.getInstance()
                                .getReference("IssueCategories").child(report.getCategoryId());
                        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String categoryname = snapshot.child("Name").getValue(String.class);
                                txtCategory.setText("Loại báo cáo: " + categoryname);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        DatabaseReference imageRef = FirebaseDatabase.getInstance()
                                .getReference("ReportImages").child(report.getCategoryId());
                        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot catSnap) {
                                String iconUrl = catSnap.child("ImageUrl").getValue(String.class);
                                if (iconUrl != null) {
                                    Glide.with(ReportDetailAdmin.this).load(iconUrl).into(imgCategory);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ReportDetailAdmin.this, "Không tải được báo cáo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updateReportStatus() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("Reports").child(reportId);
        reportRef.child("Status").setValue(selectedStatus);
        reportRef.child("LastUpdatedAt").setValue(currentTime);
        new ViewModelProvider(this)
                .get(SharedViewModel.class)
                .notifyDataChanged2();
        Toast.makeText(this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
    }

}