package com.example.hanoi_fixlt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.adapter.ReportAdapter;
import com.example.hanoi_fixlt.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Myreport extends AppCompatActivity {
    String userId;
    private Map<String, String> categoryIconMap = new HashMap<>();
    private List<Report> reportList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_myreport);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userId = getIntent().getStringExtra("userId");
        Log.d("DEBUG_MYREPORT", "userId nhận được: " + userId);
        recyclerView = findViewById(R.id.recyclermyreport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportAdapter = new ReportAdapter(this, reportList, categoryIconMap, report -> {
            // Mở activity mới khi bấm vào item
            Intent intent = new Intent(this, ReportDetail.class);
            intent.putExtra("reportId", report.getReportId());
            startActivity(intent);
        });
        recyclerView.setAdapter(reportAdapter);


        if (userId == null) {
            Toast.makeText(this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("DEBUG_MYREPORT", "userId nhận được: " + userId);

        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("IssueCategories");
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIconMap.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String id = snap.getKey();
                    String iconUrl = snap.child("IconUrl").getValue(String.class);
                    if (id != null && iconUrl != null) {
                        categoryIconMap.put(id, iconUrl);
                    }
                }
                loadUserReports();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Myreport.this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserReports() {
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
        reportsRef.orderByChild("UserId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reportList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Report report = snap.getValue(Report.class);
                            if (report != null) {
                                Log.d("DEBUG_MYREPORT", "reportId=" + report.getReportId());
                                reportList.add(report);
                            }
                        }
                        reportAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Myreport.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}