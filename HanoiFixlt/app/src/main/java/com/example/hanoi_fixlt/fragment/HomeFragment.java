package com.example.hanoi_fixlt.fragment; // Đảm bảo package này khớp với package của bạn

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.activity.IncidentListActivity; // Import Activity mới
import com.example.hanoi_fixlt.model.IncidentReport; // Đảm bảo đúng import cho model
import com.example.hanoi_fixlt.adapter.IncidentReportAdapter; // Đảm bảo đúng import cho adapter
import com.example.hanoi_fixlt.R; // Đảm bảo đúng R file của bạn

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    // Các biến này có thể được chuyển thành biến cục bộ trong onCreateView
    // vì chúng được lưu trữ trong listMap và adapterMap sau đó.
    // Nếu bạn cần truy cập chúng trực tiếp bên ngoài onCreateView/onViewCreated,
    // hãy giữ chúng làm biến thành viên.
    // Ví dụ:
    // private RecyclerView incidentRecyclerViewCapNuoc;
    // private IncidentReportAdapter incidentAdapterCapNuoc;
    // private List<IncidentReport> incidentReportListCapNuoc;

    // TextViews for "Xem thêm"
    private TextView tvXemThemCapNuoc;
    private TextView tvXemThemCayXanh;
    private TextView tvXemThemChieuSang;
    private TextView tvXemThemThoatNuoc;
    private TextView tvXemThemGiaoThong; // Đây là biến thành viên cần giữ lại vì được dùng trong setOnClickListener

    // Map to hold references to lists and adapters by category type
    private Map<String, List<IncidentReport>> listMap = new HashMap<>();
    private Map<String, IncidentReportAdapter> adapterMap = new HashMap<>();

    private DatabaseReference allIncidentsRef; // Firebase reference to all incidents

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase Realtime Database reference to the root of "incidents"
        allIncidentsRef = FirebaseDatabase.getInstance().getReference("incidents");

        // --- Initialize RecyclerViews and their lists/adapters ---
        // "Cấp nước" category
        // Khai báo cục bộ để loại bỏ cảnh báo "Field can be converted to local variable"
        RecyclerView incidentRecyclerViewCapNuoc = view.findViewById(R.id.incidentRecyclerViewCapNuoc);
        List<IncidentReport> incidentReportListCapNuoc = new ArrayList<>();
        IncidentReportAdapter incidentAdapterCapNuoc = new IncidentReportAdapter(getContext(), incidentReportListCapNuoc);
        incidentRecyclerViewCapNuoc.setLayoutManager(new LinearLayoutManager(getContext()));
        incidentRecyclerViewCapNuoc.setAdapter(incidentAdapterCapNuoc);

        // "Cây xanh" category
        RecyclerView incidentRecyclerViewCayXanh = view.findViewById(R.id.incidentRecyclerViewCayXanh);
        List<IncidentReport> incidentReportListCayXanh = new ArrayList<>();
        IncidentReportAdapter incidentAdapterCayXanh = new IncidentReportAdapter(getContext(), incidentReportListCayXanh);
        incidentRecyclerViewCayXanh.setLayoutManager(new LinearLayoutManager(getContext()));
        incidentRecyclerViewCayXanh.setAdapter(incidentAdapterCayXanh);

        // "Chiếu sáng" category
        RecyclerView incidentRecyclerViewChieuSang = view.findViewById(R.id.incidentRecyclerViewChieuSang);
        List<IncidentReport> incidentReportListChieuSang = new ArrayList<>();
        IncidentReportAdapter incidentAdapterChieuSang = new IncidentReportAdapter(getContext(), incidentReportListChieuSang);
        incidentRecyclerViewChieuSang.setLayoutManager(new LinearLayoutManager(getContext()));
        incidentRecyclerViewChieuSang.setAdapter(incidentAdapterChieuSang);

        // "Thoát nước" category
        RecyclerView incidentRecyclerViewThoatNuoc = view.findViewById(R.id.incidentRecyclerViewThoatNuoc);
        List<IncidentReport> incidentReportListThoatNuoc = new ArrayList<>();
        IncidentReportAdapter incidentAdapterThoatNuoc = new IncidentReportAdapter(getContext(), incidentReportListThoatNuoc);
        incidentRecyclerViewThoatNuoc.setLayoutManager(new LinearLayoutManager(getContext()));
        incidentRecyclerViewThoatNuoc.setAdapter(incidentAdapterThoatNuoc);

        // KHỞI TẠO CHO DANH MỤC "Giao thông"
        RecyclerView incidentRecyclerViewGiaoThong = view.findViewById(R.id.incidentRecyclerViewGiaoThong);
        List<IncidentReport> incidentReportListGiaoThong = new ArrayList<>();
        IncidentReportAdapter incidentAdapterGiaoThong = new IncidentReportAdapter(getContext(), incidentReportListGiaoThong);
        incidentRecyclerViewGiaoThong.setLayoutManager(new LinearLayoutManager(getContext()));
        incidentRecyclerViewGiaoThong.setAdapter(incidentAdapterGiaoThong);


        // Store them in maps for easier management
        listMap.put("Cấp nước", incidentReportListCapNuoc);
        adapterMap.put("Cấp nước", incidentAdapterCapNuoc);

        listMap.put("Cây xanh", incidentReportListCayXanh);
        adapterMap.put("Cây xanh", incidentAdapterCayXanh);

        listMap.put("Chiếu sáng", incidentReportListChieuSang);
        adapterMap.put("Chiếu sáng", incidentAdapterChieuSang);

        listMap.put("Thoát nước", incidentReportListThoatNuoc);
        adapterMap.put("Thoát nước", incidentAdapterThoatNuoc);
        // THÊM VÀO MAP CHO DANH MỤC "Giao thông"
        listMap.put("Giao thông", incidentReportListGiaoThong);
        adapterMap.put("Giao thông", incidentAdapterGiaoThong);


        // --- Initialize "Xem thêm" TextViews and set their click listeners ---
        tvXemThemCapNuoc = view.findViewById(R.id.tvXemThemCapNuoc);
        tvXemThemCayXanh = view.findViewById(R.id.tvXemThemCayXanh);
        tvXemThemChieuSang = view.findViewById(R.id.tvXemThemChieuSang);
        tvXemThemThoatNuoc = view.findViewById(R.id.tvXemThemThoatNuoc);
        tvXemThemGiaoThong = view.findViewById(R.id.tvXemThemGiaoThong); // Giữ là biến thành viên


        tvXemThemCapNuoc.setOnClickListener(v -> navigateToIncidentList("Cấp nước"));
        tvXemThemCayXanh.setOnClickListener(v -> navigateToIncidentList("Cây xanh"));
        tvXemThemChieuSang.setOnClickListener(v -> navigateToIncidentList("Chiếu sáng"));
        tvXemThemThoatNuoc.setOnClickListener(v -> navigateToIncidentList("Thoát nước"));
        // THIẾT LẬP LISTENER CHO "Giao thông"
        tvXemThemGiaoThong.setOnClickListener(v -> navigateToIncidentList("Giao thông"));

        // --- Return the inflated view ---
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // This is called after onCreateView and ensures the view is fully created.
        // It's a good place to start fetching data.

        // --- Fetch and Filter data from Firebase Realtime Database ---
        allIncidentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear all lists before repopulating
                for (List<IncidentReport> list : listMap.values()) {
                    list.clear();
                }

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    IncidentReport report = postSnapshot.getValue(IncidentReport.class);
                    if (report != null) {
                        report.setId(postSnapshot.getKey()); // Set ID from Firebase key

                        // Filter by type and add to the correct list
                        if (listMap.containsKey(report.getType())) {
                            listMap.get(report.getType()).add(report);
                        } else {
                            // Log cảnh báo nếu có loại sự cố mới chưa được xử lý trong fragment này
                            Log.w(TAG, "Unhandled incident type: " + report.getType() + ". Consider adding a RecyclerView for this type.");
                        }
                    }
                }

                // Notify all adapters of data changes
                for (IncidentReportAdapter adapter : adapterMap.values()) {
                    adapter.notifyDataSetChanged();
                }
                Log.d(TAG, "Incident data loaded and RecyclerViews updated successfully.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(getContext(), "Lỗi đọc dữ liệu sự cố: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Lỗi đọc dữ liệu sự cố từ Firebase: " + error.getMessage(), error.toException());
            }
        });
    }

    /**
     * Helper method to navigate to IncidentListActivity with a specific category.
     * @param category The incident category to display.
     */
    private void navigateToIncidentList(String category) {
        Intent intent = new Intent(getActivity(), IncidentListActivity.class);
        intent.putExtra("incident_category", category); // Pass the category as an extra
        startActivity(intent);
        Toast.makeText(getContext(), "Đang mở danh sách: " + category, Toast.LENGTH_SHORT).show();
    }
}
