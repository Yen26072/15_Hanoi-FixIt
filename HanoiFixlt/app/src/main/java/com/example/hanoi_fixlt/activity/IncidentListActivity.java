package com.example.hanoi_fixlt.activity; // Đảm bảo package này khớp với package của bạn

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView; // Import ImageView
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.adapter.IncidentReportAdapter;
import com.example.hanoi_fixlt.model.IncidentReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IncidentListActivity extends AppCompatActivity {

    private static final String TAG = "IncidentListActivity";
    private TextView tvCategoryTitle;
    private RecyclerView incidentRecyclerView;
    private IncidentReportAdapter incidentAdapter;
    private List<IncidentReport> incidentReportList;
    private DatabaseReference incidentsRef;
    private ImageView btnBack; // Declare back button

    private String incidentCategory; // To store the category passed from HomeFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_list); // Set the layout for this Activity

        // Get the incident category passed from the Intent
        if (getIntent().hasExtra("incident_category")) {
            incidentCategory = getIntent().getStringExtra("incident_category");
        } else {
            // Handle case where no category is passed (e.g., set a default or show error)
            incidentCategory = "Tất cả sự cố"; // Default
            Toast.makeText(this, "Không có loại sự cố được chỉ định.", Toast.LENGTH_SHORT).show();
        }

        // Initialize UI elements
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        incidentRecyclerView = findViewById(R.id.incidentListRecyclerView);
        btnBack = findViewById(R.id.btnBack); // Initialize back button

        // Set the title based on the received category
        tvCategoryTitle.setText(incidentCategory);

        // Set OnClickListener for the back button
        btnBack.setOnClickListener(v -> finish()); // Go back to the previous activity

        // Initialize Firebase Realtime Database reference
        incidentsRef = FirebaseDatabase.getInstance().getReference("incidents");

        // Initialize RecyclerView and its list
        incidentReportList = new ArrayList<>();
        incidentAdapter = new IncidentReportAdapter(this, incidentReportList);

        incidentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        incidentRecyclerView.setAdapter(incidentAdapter);

        // --- Fetch data from Firebase Realtime Database filtered by category ---
        incidentsRef.orderByChild("type").equalTo(incidentCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incidentReportList.clear(); // Clear previous data
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    IncidentReport report = postSnapshot.getValue(IncidentReport.class);
                    if (report != null) {
                        report.setId(postSnapshot.getKey()); // Set ID from Firebase key
                        incidentReportList.add(report);
                    }
                }
                incidentAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                Log.d(TAG, "Dữ liệu sự cố cho loại '" + incidentCategory + "' đã được tải và cập nhật.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(IncidentListActivity.this, "Lỗi đọc dữ liệu: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Lỗi đọc dữ liệu cho loại '" + incidentCategory + "' từ Firebase: " + error.getMessage(), error.toException());
            }
        });
    }
}
