package com.example.hanoi_fixlt.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.activity.AdminActivity;
import com.example.hanoi_fixlt.activity.ReportDetail;
import com.example.hanoi_fixlt.model.Report;

import java.util.List;
import java.util.Map;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private Context context;
    private List<Report> reportList;
    private Map<String, String> categoryIconMap;
    private OnReportClickListener listener;

    public interface OnReportClickListener {
        void onReportClick(Report report);
    }

    public ReportAdapter(Context context, List<Report> reportList, Map<String, String> categoryIconMap) {
        this.context = context;
        this.reportList = reportList;
        this.categoryIconMap = categoryIconMap;
    }

    public ReportAdapter(Context context, List<Report> reportList, Map<String, String> categoryIconMap, OnReportClickListener listener) {
        this.context = context;
        this.reportList = reportList;
        this.categoryIconMap = categoryIconMap;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.txtLocation.setText("Địa điểm: " + report.getAddressDetail());
        holder.txtTime.setText("Thời gian: " + report.getSubmittedAt());
        holder.txtStatus.setText("Trạng thái: " + report.getStatus());
        holder.txtDescription.setText("Nội dung: " + report.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onReportClick(report);

            }
        });
        Log.d("DEBUG","Địa điểm: " + report.getAddressDetail());
        Log.d("DEBUG","Thời gian: " + report.getSubmittedAt());
        Log.d("DEBUG","Trạng thái: " + report.getStatus());
        Log.d("DEBUG","Nội dung: " + report.getDescription());

        // Load ảnh từ IconUrl tương ứng với CategoryId
        String iconUrl = categoryIconMap.get(report.getCategoryId());
        if (iconUrl != null) {
            Glide.with(context).load(iconUrl).into(holder.imgIncident);
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIncident;
        TextView  txtLocation, txtTime, txtStatus, txtDescription;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIncident = itemView.findViewById(R.id.imageView3);
            txtLocation = itemView.findViewById(R.id.txtAddress);
            txtTime = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
