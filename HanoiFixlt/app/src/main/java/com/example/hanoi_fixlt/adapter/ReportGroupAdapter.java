package com.example.hanoi_fixlt.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_fixlt.R;
import com.example.hanoi_fixlt.activity.AllReports;
import com.example.hanoi_fixlt.adapter.ReportAdapter;
import com.example.hanoi_fixlt.model.GroupedReport;

import java.util.List;
import java.util.Map;

public class ReportGroupAdapter extends RecyclerView.Adapter<ReportGroupAdapter.GroupViewHolder> {
    private Context context;
    private List<GroupedReport> groupedReportList;
    private Map<String, String> categoryIconMap;

    public ReportGroupAdapter(Context context, List<GroupedReport> groupedReportList, Map<String, String> categoryIconMap) {
        this.context = context;
        this.groupedReportList = groupedReportList;
        this.categoryIconMap = categoryIconMap;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_report, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        GroupedReport group = groupedReportList.get(position);
        holder.txtCategory.setText(group.getCategoryName());

        // Gắn adapter con
        ReportAdapter reportAdapter = new ReportAdapter(context, group.getReports(), categoryIconMap);
        holder.recyclerReports.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerReports.setAdapter(reportAdapter);

        Log.d("DEBUG", "Gắn nhóm: " + group.getCategoryName() + ", số báo cáo: " + group.getReports().size());


        holder.txtViewMore.setOnClickListener(v -> {
            Intent intent = new Intent(context, AllReports.class);
            intent.putExtra("categoryId", group.getCategoryId());
            intent.putExtra("categoryName", group.getCategoryName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groupedReportList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtViewMore;
        RecyclerView recyclerReports;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategoryName);
            txtViewMore = itemView.findViewById(R.id.txtXemthem);
            recyclerReports = itemView.findViewById(R.id.recyclerReports);
        }
    }
}
