package com.example.hanoi_fixlt.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.IsueCategory;
import com.example.hanoi_fixlt.viewmodel.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragmentLoggedIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragmentLoggedIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner spinnerDistrict, spinnerWard, spinnerCategory;
    private ArrayAdapter<String> districtAdapter, wardAdapter, cateAdapter;//Adapter kết nối dữ liệu danh sách với giao diện Spinner.
    private List<String> districtList = new ArrayList<>();//danh sách lưu dữ liệu lấy từ Firebase để hiển thị lên Spinner.
    private List<String> wardList = new ArrayList<>();
    private List<String> cateList = new ArrayList<>();

    private DatabaseReference databaseRef;
    private DatabaseReference categoryRef;
    private DatabaseReference reportRef;
    private DatabaseReference imageRef;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imagePreview;
    private Uri imageUri;
    private Button btnSelectImage, btnReport;
    private EditText edtDescription, edtAddressDetail;
    private ScrollView scrollView;

    private StorageReference storageRef;
    private Map<String, String> categoryNameToIdMap = new HashMap<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportFragmentLoggedIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragmentLoggedIn.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragmentLoggedIn newInstance(String param1, String param2) {
        ReportFragmentLoggedIn fragment = new ReportFragmentLoggedIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_loggedin, container, false);


        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        spinnerWard = view.findViewById(R.id.spinnerWard);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        imagePreview = view.findViewById(R.id.imgReport);
        btnReport = view.findViewById(R.id.btnReport);
        edtDescription = view.findViewById(R.id.edtDes);
        edtAddressDetail = view.findViewById(R.id.edtAddress);
        scrollView = view.findViewById(R.id.scrollViewReport);

        databaseRef = FirebaseDatabase.getInstance().getReference("DiaChi");
        categoryRef = FirebaseDatabase.getInstance().getReference("IssueCategories");
        reportRef = FirebaseDatabase.getInstance().getReference("Reports");
        imageRef = FirebaseDatabase.getInstance().getReference("ReportImages");
        storageRef = FirebaseStorage.getInstance().getReference("report_images");


        districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districtList);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);

        wardAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, wardList);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWard.setAdapter(wardAdapter);

        cateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cateList);
        cateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(cateAdapter);

        loadDistricts();
        loadCategory();


        btnSelectImage.setOnClickListener(v -> openGallery());
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(getContext(), "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                submitReportWithFakeImage(imageUri);
            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = districtList.get(position);
                loadWards(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
            imagePreview.setVisibility(View.VISIBLE);
            btnSelectImage.setVisibility(View.GONE);
        }
    }

    //Lắng nghe Firebase, lấy danh sách Quận, thêm vào districtList, rồi cập nhật adapter.
    private void loadDistricts() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                districtList.clear();
                for (DataSnapshot districtSnap : snapshot.getChildren()) {
                    districtList.add(districtSnap.getKey());
                }
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
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                cateList.clear();
                categoryNameToIdMap.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String name = snap.child("Name").getValue(String.class);
                    if (name != null) {
                        cateList.add(name);
                        categoryNameToIdMap.put(name, snap.getKey());
                    }

                }
                cateAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadWards(String districtName) {
        databaseRef.child(districtName).child("Phường")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        wardList.clear();
                        for (DataSnapshot wardSnap : snapshot.getChildren()) {
                            wardList.add(wardSnap.getKey());
                        }
                        wardAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Lỗi tải Phường", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitReportWithFakeImage(Uri imageUri) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        if (userId == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
            Log.d("DEBUG_USER_ID", "userId from SharedPreferences: " + userId);
            return;
        }

        String reportId = reportRef.push().getKey();
        String imageId = imageRef.push().getKey();

        String district = spinnerDistrict.getSelectedItem().toString();
        String ward = spinnerWard.getSelectedItem().toString();
        String categoryName = spinnerCategory.getSelectedItem().toString();
        String categoryId = categoryNameToIdMap.get(categoryName);
        String addressDetail = edtAddressDetail.getText().toString();
        String description = edtDescription.getText().toString();

        String fakeImageUrl = "https://placehold.co/400x300?text=Fake+Image";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
        String formattedDate = sdf.format(new Date());

        Map<String, Object> imageData = new HashMap<>();
        imageData.put("ReportId", reportId);
        imageData.put("ImageUrl", fakeImageUrl);
        imageData.put("UploadedAt", formattedDate);
        imageRef.child(imageId).setValue(imageData);

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("ReportId", reportId);
        reportData.put("CategoryId", categoryId);
        reportData.put("District", district);
        reportData.put("Ward", ward);
        reportData.put("Latitude", 0.0);
        reportData.put("Longitude", 0.0);
        reportData.put("UpvoteCount", 0);
        reportData.put("Description", description);
        reportData.put("AddressDetail", addressDetail);
        reportData.put("UserId", userId);
        reportData.put("Status", "Submitted");
        reportData.put("SubmittedAt", formattedDate);
        reportData.put("LastUpdatedAt", formattedDate);
        reportRef.child(reportId).setValue(reportData);

        edtAddressDetail.setText("");
        edtDescription.setText("");
        spinnerDistrict.setSelection(0);
        spinnerWard.setSelection(0);
        spinnerCategory.setSelection(0);
        btnSelectImage.setVisibility(View.VISIBLE);
        imagePreview.setVisibility(View.GONE);
        scrollView.fullScroll(ScrollView.FOCUS_UP);


        Toast.makeText(getContext(), "Đã gửi báo cáo với ảnh giả lập", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isAdded() || requireActivity().isFinishing()) {
                Log.w("Dialog", "Fragment/Activity không còn hiển thị, không show dialog.");
                return;
            }

            Dialog dialog = new Dialog(requireActivity());
            dialog.setContentView(R.layout.dialog_custom);
            dialog.setCanceledOnTouchOutside(false);

            // Làm mờ nền phía sau
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                WindowManager.LayoutParams params = window.getAttributes();
                params.dimAmount = 0.5f; // độ mờ nền
                window.setAttributes(params);
            }

            Button btnClose = dialog.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(v -> {
                dialog.dismiss();

                // Chỉ thông báo cập nhật dữ liệu sau khi dialog đóng
                new ViewModelProvider(requireActivity())
                        .get(SharedViewModel.class)
                        .notifyDataChanged();
            });

            Log.d("Dialog", "Hiển thị dialog thành công");
            dialog.show();

        }, 300); // Delay 300ms để UI ổn định
    }

}