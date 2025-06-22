package com.example.hanoi_fixlt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.util.Log;
import android.view.inputmethod.EditorInfo; // Import EditorInfo

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.IncidentReport; // Đảm bảo đúng import cho model
import com.example.hanoi_fixlt.adapter.IncidentReportAdapter; // Đảm bảo đúng import cho adapter
//import com.example.hanoi_fixlt.IncidentListActivity; // Nếu bạn có activity này cho "Xem thêm"

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment"; // TAG for Logcat

    private EditText searchEditText;
    private ImageView searchIcon;
    private Button btnAdvancedSearch;
    private LinearLayout searchResultsPlaceholder;
    private RecyclerView searchResultsRecyclerView;

    private IncidentReportAdapter searchResultsAdapter;
    private List<IncidentReport> allIncidentReports; // List of all reports
    private List<IncidentReport> filteredIncidentReports; // List of filtered reports

    private DatabaseReference incidentsRef; // Firebase reference to the "incidents" node

    public SearchFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Starting to create View for SearchFragment.");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Log.d(TAG, "onCreateView: Layout fragment_search has been inflated.");

        // Initialize Firebase Database Reference
        incidentsRef = FirebaseDatabase.getInstance().getReference("incidents");
        Log.d(TAG, "onCreateView: Initializing FirebaseDatabase reference to 'incidents'.");

        // Map Views from layout
        searchEditText = view.findViewById(R.id.searchEditText);
        searchIcon = view.findViewById(R.id.searchIcon);
        btnAdvancedSearch = view.findViewById(R.id.btnAdvancedSearch);
        searchResultsPlaceholder = view.findViewById(R.id.searchResultsPlaceholder);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);

        // Initialize lists and adapter for search results RecyclerView
        allIncidentReports = new ArrayList<>();
        filteredIncidentReports = new ArrayList<>();
        searchResultsAdapter = new IncidentReportAdapter(getContext(), filteredIncidentReports);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        Log.d(TAG, "onCreateView: Initialized RecyclerView and Adapter.");

        // Load all reports once when the Fragment is created
        loadAllIncidents();

        Log.d(TAG, "onCreateView: Completed creating View for SearchFragment.");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Started.");

        // Handle click event for the search icon
        searchIcon.setOnClickListener(v -> performSearch());

        // Handle Enter key press on the keyboard in EditText
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event == null || event.getAction() == android.view.KeyEvent.ACTION_DOWN && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER) {
                performSearch();
                return true;
            }
            return false;
        });

        // Handle click event for "Advanced Search" button
        btnAdvancedSearch.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Advanced search functionality will be developed later.", Toast.LENGTH_SHORT).show();
            // Navigate to advanced search Activity/Fragment if available
        });

        // Initially, show the placeholder
        showPlaceholder();
        Log.d(TAG, "onViewCreated: Completed.");
    }

    /**
     * Loads all incident reports from Firebase Realtime Database.
     * This data will be used for client-side filtering.
     */
    private void loadAllIncidents() {
        Log.d(TAG, "loadAllIncidents: Loading all reports from Firebase.");
        incidentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allIncidentReports.clear(); // Clear old data
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        IncidentReport report = postSnapshot.getValue(IncidentReport.class);
                        if (report != null) {
                            report.setId(postSnapshot.getKey()); // Get ID from Firebase key
                            allIncidentReports.add(report);
                            Log.d(TAG, "loadAllIncidents: Loaded report: " + report.getId() + " - " + report.getType());
                        }
                    }
                    Log.d(TAG, "loadAllIncidents: Loaded a total of " + allIncidentReports.size() + " reports.");
                    // After loading, perform search if there's a keyword in EditText
                    if (!searchEditText.getText().toString().trim().isEmpty()) {
                        performSearch();
                    }
                } else {
                    Log.d(TAG, "loadAllIncidents: No data in 'incidents' node.");
                    Toast.makeText(getContext(), "No incident data to search.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading data: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "loadAllIncidents: Error loading data from Firebase: " + error.getMessage(), error.toException());
            }
        });
    }

    /**
     * Performs a search based on the entered keyword.
     * Filters data from the `allIncidentReports` list.
     */
    private void performSearch() {
        String queryText = searchEditText.getText().toString().trim().toLowerCase();
        Log.d(TAG, "performSearch: Performing search with keyword: '" + queryText + "'");

        filteredIncidentReports.clear(); // Clear old results

        if (queryText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a search keyword.", Toast.LENGTH_SHORT).show();
            showPlaceholder(); // Show placeholder again if no keyword
            searchResultsAdapter.notifyDataSetChanged(); // Update RecyclerView (empty it)
            return;
        }

        for (IncidentReport report : allIncidentReports) {
            String reportDescription = report.getDescription() != null ? report.getDescription().toLowerCase() : "";
            String reportType = report.getType() != null ? report.getType().toLowerCase() : "";
            String reportLocation = report.getLocation() != null ? report.getLocation().toLowerCase() : "";

            // Filter by title or content or location
            if (reportDescription.contains(queryText) || reportType.contains(queryText) || reportLocation.contains(queryText)) {
                filteredIncidentReports.add(report);
            }
        }

        if (filteredIncidentReports.isEmpty()) {
            Toast.makeText(getContext(), "No results found for '" + queryText + "'", Toast.LENGTH_SHORT).show();
            showPlaceholder(); // Show placeholder if no results
        } else {
            showResults(); // Show RecyclerView with results
        }
        searchResultsAdapter.notifyDataSetChanged(); // Update RecyclerView
        Log.d(TAG, "performSearch: Search completed. Found " + filteredIncidentReports.size() + " results.");
    }

    /**
     * Shows the placeholder and hides the RecyclerView.
     */
    private void showPlaceholder() {
        searchResultsPlaceholder.setVisibility(View.VISIBLE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        Log.d(TAG, "showPlaceholder: Showing placeholder.");
    }

    /**
     * Shows the RecyclerView and hides the placeholder.
     */
    private void showResults() {
        searchResultsPlaceholder.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        Log.d(TAG, "showResults: Showing RecyclerView results.");
    }
}
