package com.example.hanoi_fixlt.model;

public class User {
    private String userId; // ID người dùng
    private String passwordHash; // Mật khẩu đã băm
    private String fullName; // Họ và tên
    private String phoneNumber; // Số điện thoại
    private String avatarurl;

    public User(String userId, String passwordHash, String fullName, String phoneNumber, String avatarurl) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.avatarurl = avatarurl;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

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
}
