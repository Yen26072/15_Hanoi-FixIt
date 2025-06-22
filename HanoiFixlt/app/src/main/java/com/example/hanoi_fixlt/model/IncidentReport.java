package com.example.hanoi_fixlt.model; // Đảm bảo package này khớp với package của bạn

// Data class to represent an incident report
public class IncidentReport {
    public String id; // Unique ID for the report (Firebase key)
    public String type; // Type of incident (e.g., "Cấp nước", "Cây xanh")
    public String location; // Location of the incident
    public String time; // Time of the incident (as a string, e.g., "31/05/2025")
    public String status; // Status of the incident (e.g., "Chưa xử lý", "Đã xử lý")
    public String imageUrl; // URL of the incident image
    public String description; // Description of the incident - NEW FIELD

    public IncidentReport() {
        // Required empty public constructor for Firebase deserialization
    }

    public IncidentReport(String id, String type, String location, String time, String status, String imageUrl, String description) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.time = time;
        this.status = status;
        this.imageUrl = imageUrl;
        this.description = description; // Initialize new field
    }

    // Public getters are required for Firebase to properly read the data
    public String getId() { return id; }
    public void setId(String id) { this.id = id; } // Setter for setting ID from Firebase key

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

    // Getter and Setter for the new 'description' field
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
