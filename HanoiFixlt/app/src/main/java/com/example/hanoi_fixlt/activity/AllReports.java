package com.example.hanoi_fixlt.activity;

import android.os.Bundle;
import android.widget.TextView;
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

public class AllReports extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<Report> reportList = new ArrayList<>();
    private DatabaseReference reportsRef;
    private String categoryId;
    private String categoryName;
    TextView txtCategoryName;
    private Map<String, String> categoryIconMap = new HashMap<>();
    private DatabaseReference categoriesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerAllReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtCategoryName = findViewById(R.id.txtCategoryNameAll);

        // Nhận categoryId từ intent
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        txtCategoryName.setText(categoryName);

        categoriesRef = FirebaseDatabase.getInstance().getReference("IssueCategories");
        loadCategoryImage();

        // Load dữ liệu
        reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
    }

    private void loadCategoryImage() {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIconMap.clear();
                for (DataSnapshot catSnap : snapshot.getChildren()) {
                    String catId = catSnap.getKey();
                    String iconUrl = catSnap.child("IconUrl").getValue(String.class);
                    if (catId != null && iconUrl != null) {
                        categoryIconMap.put(catId, iconUrl);
                    }
                }

                loadReports(); // Chỉ load báo cáo sau khi có icon
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllReports.this, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadReports() {
        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Report report = snap.getValue(Report.class);
                    if (report != null && report.getCategoryId().equals(categoryId)) {
                        reportList.add(report);
                    }
                }

                adapter = new ReportAdapter(AllReports.this, reportList, categoryIconMap);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllReports.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

    }
}