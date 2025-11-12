package com.esun.shop.model;

import java.time.LocalDateTime;

public class User {
    private String userId; // 會員編碼（你要拿來當 memberId）
    private String username; // 登入帳號
    private String passwordHash; // 密碼（範例用明文/或hash，視你資料庫而定）
    private String role; // ADMIN / CUSTOMER
    private Boolean isActive; // 是否啟用
    private LocalDateTime createdAt;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
