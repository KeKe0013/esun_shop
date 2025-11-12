package com.esun.shop.model;

import java.sql.Timestamp;

public class UserAccount {
    private String userId; // 會員編碼（字串，如 U0001）
    private String username; // 登入帳號
    private String passwordHash; // 密碼/雜湊
    private String role; // ADMIN / CUSTOMER
    private boolean active; // 是否啟用
    private Timestamp createdAt;

    public UserAccount() {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
