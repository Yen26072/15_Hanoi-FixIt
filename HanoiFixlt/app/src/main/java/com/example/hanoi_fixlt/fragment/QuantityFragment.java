package com.example.hanoi_fixlt.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class QuantityFragment extends Fragment {
    private LinearLayout statisticsContainer;


    public QuantityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quantity, container, false);

        statisticsContainer = view.findViewById(R.id.linear);
        loadStatistics();
        return view;
    }

    private void loadStatistics() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("IssueCategories");
        DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("Reports");

        Map<String, String> iconMap = new HashMap<>();
        Map<String, String> nameMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cat : snapshot.getChildren()) {
                    String id = cat.getKey();
                    String name = cat.child("Name").getValue(String.class);
                    String iconUrl = cat.child("IconUrl").getValue(String.class);
                    if (id != null && name != null) {
                        nameMap.put(id, name);
                        iconMap.put(id, iconUrl);
                        countMap.put(id, 0); // khởi tạo số lượng
                    }

                }

                reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Report report = snap.getValue(Report.class);
                            if (report != null && report.getCategoryId() != null) {
                                String catId = report.getCategoryId();
                                if (countMap.containsKey(catId)) {
                                    countMap.put(catId, countMap.get(catId) + 1);
                                }
                            }
                        }

                        statisticsContainer.removeAllViews();
                        for (String catId : nameMap.keySet()) {
                            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_quantity, statisticsContainer, false);

                            TextView txtName = itemView.findViewById(R.id.txtCategoryName);
                            TextView txtCount = itemView.findViewById(R.id.txtReportCount);
                            ImageView imgIcon = itemView.findViewById(R.id.imgCategory);

                            txtName.setText(nameMap.get(catId));
                            txtCount.setText(String.valueOf(countMap.get(catId)));

                            String iconUrl = iconMap.get(catId);
                            if (iconUrl != null) {
                                Glide.with(getContext())
                                        .load(iconUrl)
                                        .circleCrop() // Biến ảnh thành hình tròn
                                        .into(imgIcon);

                            }

                            statisticsContainer.addView(itemView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Lỗi tải báo cáo", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
