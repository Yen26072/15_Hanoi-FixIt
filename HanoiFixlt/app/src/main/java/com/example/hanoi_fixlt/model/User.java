package com.example.hanoi_fixlt.model;

public class User {
    private String userId; // ID người dùng
    private String passwordHash; // Mật khẩu đã băm
    private String fullName; // Họ và tên
    private String phoneNumber; // Số điện thoại
    private String defaultDistrict; // Quận mặc định
    private String defaultWard; // Phường mặc định

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDefaultDistrict() {
        return defaultDistrict;
    }

    public void setDefaultDistrict(String defaultDistrict) {
        this.defaultDistrict = defaultDistrict;
    }

    public String getDefaultWard() {
        return defaultWard;
    }

    public void setDefaultWard(String defaultWard) {
        this.defaultWard = defaultWard;
    }

    public User(String userId, String passwordHash, String fullName, String phoneNumber, String defaultDistrict, String defaultWard) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.defaultDistrict = defaultDistrict;
        this.defaultWard = defaultWard;
    }
}
