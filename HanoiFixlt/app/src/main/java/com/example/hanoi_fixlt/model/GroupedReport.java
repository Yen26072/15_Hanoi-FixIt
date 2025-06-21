package com.example.hanoi_fixlt.model;

import java.util.List;

public class GroupedReport {
    private String categoryId;
    private String categoryName;
    private String iconUrl;
    private List<Report> reports;

    public GroupedReport() {
    }

    public GroupedReport(String categoryId, String categoryName, String iconUrl, List<Report> reports) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.iconUrl = iconUrl;
        this.reports = reports;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
