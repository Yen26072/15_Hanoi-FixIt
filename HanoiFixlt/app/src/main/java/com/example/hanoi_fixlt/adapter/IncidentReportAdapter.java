package com.example.hanoi_fixlt.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.model.IncidentReport; // Đảm bảo model này tồn tại

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
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_incident_report, parent, false);
        return new IncidentReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentReportViewHolder holder, int position) {
        IncidentReport report = incidentReports.get(position);

        // Set incident details to TextViews
        holder.incidentTypeTextView.setText("Loại sự cố: " + report.getType());
        holder.incidentLocationTextView.setText("Địa điểm: " + report.getLocation());
        holder.incidentTimeTextView.setText("Thời gian: " + report.getTime());
        holder.incidentDescriptionTextView.setText("Nội dung báo cáo: " + report.getDescription());

        // Load incident image using Glide
        if (report.getImageUrl() != null && !report.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(report.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
                    .error(R.drawable.ic_launcher_background) // Error image if loading fails
                    .into(holder.incidentImage);
        } else {
            // Set a default image if no URL is provided
            holder.incidentImage.setImageResource(R.drawable.ic_launcher_background);
        }

//        // Set OnClickListener for the "Xem thêm" (View More) button
//        holder.btnXemThem.setOnClickListener(v -> {
//            Toast.makeText(context, "Đang mở chi tiết sự cố: " + report.getId(), Toast.LENGTH_SHORT).show();
//            // Create an Intent to open IncidentDetailActivity
//            Intent intent = new Intent(context, IncidentDetailActivity.class);
//            // Pass the incident ID to the detail activity
//            intent.putExtra("incident_id", report.getId());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return incidentReports.size();
    }

    // ViewHolder class to hold references to the views in each item_incident_report layout
    public static class IncidentReportViewHolder extends RecyclerView.ViewHolder {
        ImageView incidentImage;
        TextView incidentTypeTextView, incidentLocationTextView, incidentTimeTextView, incidentDescriptionTextView;
        Button btnXemThem;

        public IncidentReportViewHolder(@NonNull View itemView) {
            super(itemView);
            // Map the views from item_incident_report.xml to the ViewHolder variables
            incidentImage = itemView.findViewById(R.id.incidentImage);
            incidentTypeTextView = itemView.findViewById(R.id.incidentTypeTextView);
            incidentLocationTextView = itemView.findViewById(R.id.incidentLocationTextView);
            incidentTimeTextView = itemView.findViewById(R.id.incidentTimeTextView);
            incidentDescriptionTextView = itemView.findViewById(R.id.incidentDescriptionTextView);
            btnXemThem = itemView.findViewById(R.id.btnXemThem);
        }
    }
}
