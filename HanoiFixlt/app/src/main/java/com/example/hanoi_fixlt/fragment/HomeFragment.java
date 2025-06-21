package com.example.hanoi_fixlt.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.adapter.ReportGroupAdapter;
import com.example.hanoi_fixlt.model.GroupedReport;
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

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReportGroupAdapter adapter;
    private List<GroupedReport> groupedReportList = new ArrayList<>();

    private DatabaseReference reportsRef, categoriesRef;
    private Map<String, String> categoryNameMap = new HashMap<>();
    private Map<String, String> categoryIconMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewGroups);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportGroupAdapter(getContext(), groupedReportList, categoryIconMap);
        recyclerView.setAdapter(adapter);

        reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
        categoriesRef = FirebaseDatabase.getInstance().getReference("IssueCategories");

        loadCategoriesAndReports();

        return view;
    }

    private void loadCategoriesAndReports() {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryNameMap.clear();
                categoryIconMap.clear();
                Log.d("DEBUG", "MainActivity onCreate chạy");

                for (DataSnapshot catSnap : snapshot.getChildren()) {
                    String categoryId = catSnap.getKey();
                    String name = catSnap.child("Name").getValue(String.class);
                    String iconUrl = catSnap.child("IconUrl").getValue(String.class);

                    if (categoryId != null && name != null) {
                        categoryNameMap.put(categoryId, name);
                        categoryIconMap.put(categoryId, iconUrl);
                    }
                }

                loadReports(); // Sau khi có map danh mục
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReports() {
        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DEBUG", "Số lượng báo cáo: " + snapshot.getChildrenCount());
                Map<String, List<Report>> groupedMap = new HashMap<>();
                groupedReportList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Report report = snap.getValue(Report.class);
                    if (report != null && report.getCategoryId() != null) {
                        String categoryId = report.getCategoryId();

                        if (!groupedMap.containsKey(categoryId)) {
                            groupedMap.put(categoryId, new ArrayList<>());
                        }

                        groupedMap.get(categoryId).add(report);
                    }
                    else {
                        Log.d("DEBUG", "report bị null");
                    }
                }

                for (Map.Entry<String, List<Report>> entry : groupedMap.entrySet()) {
                    List<Report> allReports = entry.getValue();

                    // Sort theo thời gian giảm dần
                    Collections.sort(allReports, (r1, r2) ->
                            r2.getSubmittedAt().compareTo(r1.getSubmittedAt()));

                    List<Report> top3 = allReports.subList(0, Math.min(3, allReports.size()));
                    String categoryId = entry.getKey();
                    String categoryName = categoryNameMap.get(categoryId);
                    String iconUrl = categoryIconMap.get(categoryId);

                    GroupedReport group = new GroupedReport(categoryId, categoryName, iconUrl, top3);
                    groupedReportList.add(group);

                    Log.d("DEBUG", "Tạo nhóm " + categoryName + " với " + top3.size() + " báo cáo");
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải báo cáo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
