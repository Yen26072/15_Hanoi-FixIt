package com.example.hanoi_fixlt.model;

public class Report {
    private String ReportId; // ID báo cáo (PK, INT, IDENTITY)
    private String UserId; // ID người gửi (FK -> User)
    private String CategoryId; // ID danh mục (FK -> IssueCategory)
    private String Description; // Mô tả chi tiết (NOT NULL)
    private Double Latitude; // Vĩ độ (NULL nếu không có)
    private Double Longitude; // Kinh độ (NULL nếu không có)
    private String AddressDetail; // Địa chỉ chi tiết (NULL nếu không có)
    private String District; // Quận
    private String Ward; // Phường
    private String Status; // Trạng thái báo cáo
    private String SubmittedAt; // Thời gian gửi báo cáo
    private String LastUpdatedAt; // Thời gian cập nhật cuối
    private Integer UpvoteCount; // Số lượng upvote (DEFAULT 0)

    public Report() {
    }

    public String getReportId() {
        return ReportId;
    }

    public void setReportId(String reportId) {
        ReportId = reportId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getAddressDetail() {
        return AddressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        AddressDetail = addressDetail;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSubmittedAt() {
        return SubmittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        SubmittedAt = submittedAt;
    }

    public String getLastUpdatedAt() {
        return LastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        LastUpdatedAt = lastUpdatedAt;
    }

    public Integer getUpvoteCount() {
        return UpvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        UpvoteCount = upvoteCount;
    }

    public Report(String reportId, String userId, String categoryId, String description, Double latitude, Double longitude, String addressDetail, String district, String ward, String status, String submittedAt, String lastUpdatedAt, Integer upvoteCount) {
        ReportId = reportId;
        UserId = userId;
        CategoryId = categoryId;
        Description = description;
        Latitude = latitude;
        Longitude = longitude;
        AddressDetail = addressDetail;
        District = district;
        Ward = ward;
        Status = status;
        SubmittedAt = submittedAt;
        LastUpdatedAt = lastUpdatedAt;
        UpvoteCount = upvoteCount;
    }
}
