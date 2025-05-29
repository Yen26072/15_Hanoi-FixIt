package com.example.hanoi_fixlt.model;

public class ReportVote {
    private String userId;    // ID người dùng (FK)
    private int reportId;     // ID báo cáo (FK)

    public ReportVote() {
    }

    public ReportVote(String userId, int reportId) {
        this.userId = userId;
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}
