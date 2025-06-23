package com.example.hanoi_fixlt.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;
import android.view.inputmethod.EditorInfo; // Import EditorInfo

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.activity.ReportDetail;
import com.example.hanoi_fixlt.adapter.ReportAdapter;
import com.example.hanoi_fixlt.model.IncidentReport;
import com.example.hanoi_fixlt.adapter.IncidentReportAdapter;
//import com.example.hanoi_fixlt.IncidentListActivity; //Xem thêm

import com.example.hanoi_fixlt.model.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchFragment extends Fragment {
    private ArrayAdapter<String> districtAdapter, wardAdapter, cateAdapter;//Adapter kết nối dữ liệu danh sách với giao diện Spinner.
    private List<String> districtList = new ArrayList<>();//danh sách lưu dữ liệu lấy từ Firebase để hiển thị lên Spinner.
    private List<String> cateList = new ArrayList<>();

    private EditText edtSearchContent, toDate, fromDate;
    private Spinner spinner1, spinner2;
    private Button btn1, btn2;
    private RecyclerView recyclerView;
    private LinearLayout linear1, linear2, linear3, linear4, linear5;
    private ConstraintLayout cons1, cons2;
    private ReportAdapter reportAdapter;
    private List<Report> allReports = new ArrayList<>();
    private List<Report> filteredReports = new ArrayList<>();
    private Map<String, String> categoryIconMap = new HashMap<>();
    private DatabaseReference databaseRef;
    private DatabaseReference categoryRef;
    private Map<String, String> categoryNameToIdMap = new HashMap<>();
    private long startTimestamp = Long.MIN_VALUE;
    private long endTimestamp = Long.MAX_VALUE;
    private String selectedDistrict = "", selectedCategory = "";
    private ImageButton img1, img2;

    public SearchFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        edtSearchContent = view.findViewById(R.id.searchEditText);
        toDate = view.findViewById(R.id.toDate);
        fromDate = view.findViewById(R.id.fromDate);
        spinner1 = view.findViewById(R.id.spinnerAddressSearch);
        spinner2 = view.findViewById(R.id.spinnerCategorySearch);
        btn1 = view.findViewById(R.id.btnAdvancedSearch);
        btn2 = view.findViewById(R.id.btnSearch2);
        linear1 = view.findViewById(R.id.linear1);
        linear2 = view.findViewById(R.id.linear2);
        linear3 = view.findViewById(R.id.linear3);
        linear4 = view.findViewById(R.id.linear4);
        linear5 = view.findViewById(R.id.linear5);
        cons1 = view.findViewById(R.id.cons1);
        cons2 = view.findViewById(R.id.cons2);
        img1 = view.findViewById(R.id.imageButton);
        img2 = view.findViewById(R.id.imageButton2);

        databaseRef = FirebaseDatabase.getInstance().getReference("DiaChi");
        categoryRef = FirebaseDatabase.getInstance().getReference("IssueCategories");

        districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(districtAdapter);

        cateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cateList);
        cateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(cateAdapter);

        loadDistricts();
        loadCategory();

        districtList.add(0, "Tất cả");
        cateList.add(0, "Tất cả");

        recyclerView = view.findViewById(R.id.recyclerSearch22);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reportAdapter = new ReportAdapter(getContext(), filteredReports, categoryIconMap, report -> {
            Intent intent = new Intent(getContext(), ReportDetail.class);
            intent.putExtra("reportId", report.getReportId());
            startActivity(intent);
        });

        recyclerView.setAdapter(reportAdapter);

        setupDatePickers();
        loadCategoryIconsAndReports();

        edtSearchContent.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterReports(s.toString());
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.VISIBLE);
                linear5.setVisibility(View.VISIBLE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterReports();
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.GONE);
                linear3.setVisibility(View.GONE);
                linear4.setVisibility(View.GONE);
                linear5.setVisibility(View.GONE);
            }
        });


        return view;
    }

    private void setupDatePickers() {
        fromDate.setOnClickListener(v -> showDateDialog(fromDate, true));
        toDate.setOnClickListener(v -> showDateDialog(toDate, false));
    }

    private void showDateDialog(EditText target, boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, y, m, d) -> {
            String dateStr = d + "/" + (m + 1) + "/" + y;
            target.setText(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d, 0, 0, 0);
            if (isStart) startTimestamp = cal.getTimeInMillis();
            else endTimestamp = cal.getTimeInMillis();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void filterReports() {
        // Lấy giá trị từ Spinner với kiểm tra null
        String districtFilter = spinner1.getSelectedItemPosition() == 0 ?
                null : spinner1.getSelectedItem().toString();
        String categoryFilter = spinner2.getSelectedItemPosition() == 0 ?
                null : spinner2.getSelectedItem().toString();

        filteredReports.clear();

        for (Report report : allReports) {
            if (report == null) continue;

            boolean matchDistrict = districtFilter == null ||
                    (report.getDistrict() != null &&
                            report.getDistrict().equals(districtFilter));

            boolean matchCategory = categoryFilter == null ||
                    (report.getCategoryId() != null &&
                            report.getCategoryId().equals(categoryNameToIdMap.get(categoryFilter)));

            boolean matchDate = true;
            if (!fromDate.getText().toString().isEmpty() || !toDate.getText().toString().isEmpty()) {
                long reportTime = parseDateToMillis(report.getSubmittedAt());
                matchDate = reportTime >= startTimestamp && reportTime <= endTimestamp;
            }

            if (matchDistrict && matchCategory && matchDate) {
                filteredReports.add(report);
                Log.d("FILTER_DEBUG", "Matched report: " + report.getReportId());
            }
        }

        if (filteredReports.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy báo cáo phù hợp", Toast.LENGTH_SHORT).show();
        }

        reportAdapter.notifyDataSetChanged();
        Log.d("FILTER_RESULT", "Found " + filteredReports.size() + " reports");
    }





    private long parseDateToMillis(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(dateStr);
            return date != null ? date.getTime() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private void loadDistricts() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                districtList.clear();
                districtList.add("Tất cả"); // Luôn thêm item mặc định đầu tiên

                for (DataSnapshot districtSnap : snapshot.getChildren()) {
                    String districtName = districtSnap.getKey();
                    if (districtName != null) {
                        districtList.add(districtName);
                        Log.d("DISTRICT_DEBUG", "Loaded district: " + districtName);
                    }
                }
                spinner1.setSelection(0); // Chọn mục đầu tiên (Tất cả)
                districtAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải Quận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategory() {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cateList.clear();
                cateList.add("Tất cả"); // Thêm mục mặc định
                categoryNameToIdMap.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String name = snap.child("Name").getValue(String.class);
                    if (name != null && snap.getKey() != null) {
                        cateList.add(name);
                        categoryNameToIdMap.put(name, snap.getKey());
                    }
                }
                cateAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategoryIconsAndReports() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("IssueCategories");
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryIconMap.clear();
                for (DataSnapshot cat : snapshot.getChildren()) {
                    String id = cat.getKey();
                    String iconUrl = cat.child("IconUrl").getValue(String.class);
                    if (id != null && iconUrl != null) {
                        categoryIconMap.put(id, iconUrl);
                    }
                }
                loadAllReports();
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
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
                        Log.d("REPORT_LOAD", "Loaded report ID: " + report.getReportId() +
                                ", District: " + report.getDistrict() +
                                ", Category: " + report.getCategoryId());
                        allReports.add(report);
                    }
                }
                filteredReports.addAll(allReports);
                reportAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải báo cáo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterReports(String keyword) {
        filteredReports.clear();
        if (keyword.trim().isEmpty()) {
            filteredReports.addAll(allReports);
        } else {
            for (Report r : allReports) {
                // Kiểm tra xem mô tả có null không trước khi gọi toString()
                if (r.getDescription() != null && r.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredReports.add(r);
                }
            }
        }
        reportAdapter.notifyDataSetChanged();
    }
}
