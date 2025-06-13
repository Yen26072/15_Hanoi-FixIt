package com.example.hanoi_fixlt.model;

public class IsueCategory {
    private String categoryId; // ID danh mục
    private String name; // Tên danh mục (UNIQUE)
    private String imgCategory; // Đường dẫn đến hình ảnh

    public IsueCategory() {
    }

    public IsueCategory(String categoryId, String name, String imgCategory) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgCategory = imgCategory;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(String imgCategory) {
        this.imgCategory = imgCategory;
    }
}
