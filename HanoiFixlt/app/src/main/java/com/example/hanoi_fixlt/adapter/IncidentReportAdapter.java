package com.example.hanoi_fixlt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // For showing Toast messages on item click

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.example.hanoi_fixlt.IncidentDetailActivity;
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
        holder.tvIncidentDescription.setText("Nội dung báo cáo: " + report.getDescription()); // Assuming description field is added

        // Dynamically set the color of the status text based on its value
        // Note: For IncidentReport model, status field is currently available, but it's not displayed
        // in item_incident_report.xml. If you add it, you can use this logic.
        // if ("Đã xử lý".equals(report.getStatus())) {
        //     holder.tvIncidentStatus.setTextColor(Color.parseColor("#008000")); // Green for "Đã xử lý"
        // } else {
        //     holder.tvIncidentStatus.setTextColor(Color.parseColor("#FF0000")); // Red for "Chưa xử lý"
        // }

        // Load the incident image using Glide
        if (report.getImageUrl() != null && !report.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(report.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background) // Show a placeholder while loading
                    .error(R.drawable.ic_launcher_background) // Show placeholder if image loading fails
                    .into(holder.incidentImage);
        } else {
            // If no image URL, set a default placeholder
            holder.incidentImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set OnClickListener for the "Xem thêm" (View More) button
        // Phần này đã được comment lại theo yêu cầu của bạn
        /*
        holder.btnXemThem.setOnClickListener(v -> {
            Toast.makeText(context, "Đang mở chi tiết sự cố: " + report.getId(), Toast.LENGTH_SHORT).show();
            // Create an Intent to open IncidentDetailActivity
            Intent intent = new Intent(context, IncidentDetailActivity.class);
            // Pass the incident ID to the detail activity
            intent.putExtra("incident_id", report.getId());
            context.startActivity(intent);
        });
        */
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return incidentReports.size();
    }

    // ViewHolder class to hold references to the views for each item
    public static class IncidentReportViewHolder extends RecyclerView.ViewHolder {
        ImageView incidentImage;
        // Changed to tvIncidentType, tvIncidentLocation, tvIncidentTime, tvIncidentDescription, tvIncidentStatus
        TextView tvIncidentType, tvIncidentLocation, tvIncidentTime, tvIncidentDescription, tvIncidentStatus;
        Button btnXemThem;

        public IncidentReportViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link the declared views to their corresponding IDs in item_incident_report.xml
            incidentImage = itemView.findViewById(R.id.imgIncident);
            tvIncidentType = itemView.findViewById(R.id.tvIncidentType);
            tvIncidentLocation = itemView.findViewById(R.id.tvIncidentLocation);
            tvIncidentTime = itemView.findViewById(R.id.tvIncidentTime);
            tvIncidentDescription = itemView.findViewById(R.id.tvIncidentDescription); // Corrected ID
            tvIncidentStatus = itemView.findViewById(R.id.tvIncidentStatus); // Added status TextView
            btnXemThem = itemView.findViewById(R.id.btnXemThem);
        }
    }
}
