package com.example.hanoi_fixlt.model;

public class IncidentReport {
    private String id;
    private String type; // Loại sự cố (ví dụ: "Giao thông", "Cấp nước")
    private String location; // Địa điểm sự cố
    private String time; // Thời gian báo cáo
    private String status; // Trạng thái: "Chưa xử lý", "Đang xử lý", "Đã xử lý"
    private String imageUrl; // URL hình ảnh sự cố
    private String description; // Mô tả chi tiết sự cố

    public IncidentReport() {
        // Constructor rỗng cần thiết cho Firebase
    }

    public IncidentReport(String id, String type, String location, String time, String status, String imageUrl, String description) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.time = time;
        this.status = status;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
