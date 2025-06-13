package com.example.hanoi_fixlt.model;

import java.time.LocalDateTime;

public class ReportStatusHistory {
    private int historyId; // ID lịch sử (PK)
    private int reportId; // ID báo cáo (FK)
    private String status; // Trạng thái
    private LocalDateTime statusChangedAt; // Thời gian thay đổi trạng thái
    private String userId; // ID người thay đổi (FK -> Users)
    private String notes; // Ghi chú

    public ReportStatusHistory() {
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStatusChangedAt() {
        return statusChangedAt;
    }

    public void setStatusChangedAt(LocalDateTime statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ReportStatusHistory(int historyId, int reportId, String status, LocalDateTime statusChangedAt, String userId, String notes) {
        this.historyId = historyId;
        this.reportId = reportId;
        this.status = status;
        this.statusChangedAt = statusChangedAt;
        this.userId = userId;
        this.notes = notes;
    }
}
