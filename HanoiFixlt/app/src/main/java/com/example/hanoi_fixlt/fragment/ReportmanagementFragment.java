package com.example.hanoi_fixlt.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hanoi_fixlt.R;
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
    private List<Report> reportList = new ArrayList<>();
    private Map<String, String> categoryIconMap = new HashMap<>();
    private DatabaseReference reportsRef, categoriesRef;

    public ReportmanagementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reportmanagement, container, false);

        recyclerView = view.findViewById(R.id.recyclerAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportAdapter = new ReportAdapter(getContext(), reportList, categoryIconMap);
        recyclerView.setAdapter(reportAdapter);

        reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
        categoriesRef = FirebaseDatabase.getInstance().getReference("IssueCategories");

        loadCategoryIcons(); // Cần icon để hiển thị

        return view;
    }

    private void loadCategoryIcons() {
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
                loadAllReports();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllReports() {
        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Report report = snap.getValue(Report.class);
                    if (report != null) {
                        reportList.add(report);
                    }
                }

                // Sắp xếp theo thời gian giảm dần
                Collections.sort(reportList, (r1, r2) -> r2.getSubmittedAt().compareTo(r1.getSubmittedAt()));
                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải báo cáo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}