package com.example.hanoi_fixlt.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.activity.ReportDetailAdmin;
import com.example.hanoi_fixlt.adapter.ReportAdapter;
import com.example.hanoi_fixlt.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportmanagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private List<Report> allReports = new ArrayList<>();
    private List<Report> filteredReports = new ArrayList<>();
    private Map<String, String> categoryIconMap = new HashMap<>();
    private SearchView searchView;
    private List<Report> reportList = new ArrayList<>();

    public ReportmanagementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reportmanagement, container, false);

        recyclerView = view.findViewById(R.id.recyclerAdmin);
        searchView = view.findViewById(R.id.searchAdmin);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportAdapter = new ReportAdapter(getContext(), filteredReports, categoryIconMap, report -> {
            // Mở activity mới khi bấm vào item
            Intent intent = new Intent(getContext(), ReportDetailAdmin.class);
            intent.putExtra("reportId", report.getReportId());
            startActivity(intent);
        });
        recyclerView.setAdapter(reportAdapter);

        loadCategoryIconsAndReports();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterByReportId(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    // Nếu rỗng thì hiển thị lại toàn bộ
                    filteredReports.clear();
                    filteredReports.addAll(allReports);
                    reportAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        return view;
    }

    private void loadCategoryIconsAndReports() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("IssueCategories");
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIconMap.clear();
                for (DataSnapshot catSnap : snapshot.getChildren()) {
                    String categoryId = catSnap.getKey();
                    String iconUrl = catSnap.child("IconUrl").getValue(String.class);
                    if (categoryId != null && iconUrl != null) {
                        categoryIconMap.put(categoryId, iconUrl);
                    }
                }
                loadAllReports(); // Sau khi có icon thì load report
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllReports() {
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allReports.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Report report = snap.getValue(Report.class);
                    if (report != null) {
                        allReports.add(report);
                    }
                }

                filteredReports.clear();
                filteredReports.addAll(allReports);

                // Sắp xếp theo thời gian giảm dần
                Collections.sort(allReports, (r1, r2) -> r2.getSubmittedAt().compareTo(r1.getSubmittedAt()));
                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải báo cáo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByReportId(String query) {
        Log.d("DEBUG", "Query: " + query);
        Log.d("DEBUG", "Danh sách report:");
        for (Report r : allReports) {
            Log.d("DEBUG", "reportId: " + r.getReportId());
        }
        try {
            filteredReports.clear();
            String queryTrimmed = query.trim();
            for (Report r : allReports) {
                if (r.getReportId() != null && r.getReportId().equals(queryTrimmed)) {
                    filteredReports.add(r);
                    break;
                }
            }
            reportAdapter.notifyDataSetChanged();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Mã báo cáo không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}