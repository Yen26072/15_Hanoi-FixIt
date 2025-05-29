package com.example.hanoi_fixlt.model;

import java.time.LocalDateTime;

public class ReportImage {
    private String imageId; // ID hình ảnh (PK)
    private int reportId; // ID báo cáo (FK -> Report)
    private String imageUrl; // Đường dẫn đến hình ảnh
    private LocalDateTime uploadedAt; // Thời gian tải lên

    public ReportImage() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public ReportImage(String imageId, int reportId, String imageUrl, LocalDateTime uploadedAt) {
        this.imageId = imageId;
        this.reportId = reportId;
        this.imageUrl = imageUrl;
        this.uploadedAt = uploadedAt;
    }
}
