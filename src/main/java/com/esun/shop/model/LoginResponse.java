package com.esun.shop.model;

public class LoginResponse {
    private String role; // ADMIN / CUSTOMER / GUEST
    private String user; // 帳號（username）
    private String userId; // ★ 會員編碼（下單要用這個）

    public LoginResponse() {
    }

    public LoginResponse(String role, String user, String userId) {
        this.role = role;
        this.user = user;
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
