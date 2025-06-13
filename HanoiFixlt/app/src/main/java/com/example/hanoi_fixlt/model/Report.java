package com.example.hanoi_fixlt.model;

import java.time.LocalDateTime;

public class Report {
    private int reportId; // ID báo cáo (PK, INT, IDENTITY)
    private String userId; // ID người gửi (FK -> User)
    private String categoryId; // ID danh mục (FK -> IssueCategory)
    private String description; // Mô tả chi tiết (NOT NULL)
    private Double latitude; // Vĩ độ (NULL nếu không có)
    private Double longitude; // Kinh độ (NULL nếu không có)
    private String addressDetail; // Địa chỉ chi tiết (NULL nếu không có)
    private String district; // Quận
    private String ward; // Phường
    private String status; // Trạng thái báo cáo
    private LocalDateTime submittedAt; // Thời gian gửi báo cáo
    private LocalDateTime lastUpdatedAt; // Thời gian cập nhật cuối
    private int upvoteCount; // Số lượng upvote (DEFAULT 0)

    public Report() {
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public Report(int reportId, String userId, String categoryId, String description, Double latitude, Double longitude, String addressDetail, String district, String ward, String status, LocalDateTime submittedAt, LocalDateTime lastUpdatedAt, int upvoteCount) {
        this.reportId = reportId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressDetail = addressDetail;
        this.district = district;
        this.ward = ward;
        this.status = status;
        this.submittedAt = submittedAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.upvoteCount = upvoteCount;
    }
}
