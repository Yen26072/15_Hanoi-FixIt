package com.example.hanoi_fixlt.activity;

import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportDetail extends AppCompatActivity {
    private TextView txtAddress, txtDescription, txtStatus, txtCategory, txtReportId, txtSubmit, txtLastchange;
    private ImageView imgCategory;
    String reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_detail);
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
                                    Glide.with(ReportDetail.this).load(iconUrl).into(imgCategory);
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
                    Toast.makeText(ReportDetail.this, "Không tải được báo cáo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}