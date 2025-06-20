package com.example.hanoi_fixlt.adapter; // Replace with your package name

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // For showing Toast messages on item click

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.IncidentReport;

import java.util.List;

public class IncidentReportAdapter extends RecyclerView.Adapter<IncidentReportAdapter.IncidentReportViewHolder> {

    private Context context;
    private List<IncidentReport> incidentReports;

    public IncidentReportAdapter(Context context, List<IncidentReport> incidentReports) {
        this.context = context;
        this.incidentReports = incidentReports;
    }

    @NonNull
    @Override
    public IncidentReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (item_incident_report.xml) for each row in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_incident_report, parent, false);
        return new IncidentReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentReportViewHolder holder, int position) {
        // Get the IncidentReport object for the current position
        IncidentReport report = incidentReports.get(position);

        // Populate TextViews with incident data
        holder.tvIncidentType.setText("Loại sự cố: " + report.getType());
        holder.tvIncidentLocation.setText("Địa điểm: " + report.getLocation());
        holder.tvIncidentTime.setText("Thời gian: " + report.getTime());
        holder.tvIncidentStatus.setText("Trạng thái: " + report.getStatus());

        // Dynamically set the color of the status text based on its value
        if ("Đã xử lý".equals(report.getStatus())) {
            holder.tvIncidentStatus.setTextColor(Color.parseColor("#008000")); // Green for "Đã xử lý"
        } else {
            holder.tvIncidentStatus.setTextColor(Color.parseColor("#FF0000")); // Red for "Chưa xử lý"
        }/

        // Load the incident image using Glide
        if (report.getImageUrl() != null && !report.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(report.getImageUrl())
                    .placeholder(R.drawable.placeholder_incident_image) // Show a placeholder while loading
                    .error(R.drawable.placeholder_incident_image) // Show placeholder if image loading fails
                    .into(holder.imgIncident);
        } else {
            // If no image URL, set a default placeholder
            holder.imgIncident.setImageResource(R.drawable.placeholder_incident_image);
        }

        // Set an OnClickListener for the entire item view (optional)
        holder.itemView.setOnClickListener(v -> {
            // Example: Show a Toast message when an item is clicked
            Toast.makeText(context, "Clicked on: " + report.getLocation(), Toast.LENGTH_SHORT).show();
            // You can also start a new Activity to show incident details here
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return incidentReports.size();
    }

    // ViewHolder class to hold references to the views for each item
    public static class IncidentReportViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIncident;
        TextView tvIncidentType, tvIncidentLocation, tvIncidentTime, tvIncidentStatus;

        public IncidentReportViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link the declared views to their corresponding IDs in item_incident_report.xml
            imgIncident = itemView.findViewById(R.id.imgIncident);
            tvIncidentType = itemView.findViewById(R.id.tvIncidentType);
            tvIncidentLocation = itemView.findViewById(R.id.tvIncidentLocation);
            tvIncidentTime = itemView.findViewById(R.id.tvIncidentTime);
            tvIncidentStatus = itemView.findViewById(R.id.tvIncidentStatus);
        }
    }
}
