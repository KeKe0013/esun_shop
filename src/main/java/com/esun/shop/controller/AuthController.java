package com.esun.shop.controller;

import com.esun.shop.model.UserAccount;
import com.esun.shop.repository.AuthRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthRepository authRepository;

    public AuthController(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    // =======================
    // DTO 區：Login / Register / Response
    // =======================

    /** 登入用請求物件：只需要帳號 + 密碼 */
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /** 註冊用請求物件：帳號 + 密碼 + 身分（角色） */
    public static class RegisterRequest {
        private String username;
        private String password;
        private String role; // ADMIN / CUSTOMER

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    /** 回傳給前端的登入狀態（給 /login 和 /me 用） */
    public static class LoginResponse {
        private String role; // ADMIN / CUSTOMER / GUEST
        private String user; // username
        private String userId; // 會員編碼（UserID）

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

    // =======================
    // API：登入
    // =======================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpSession session) {
        // 基本欄位檢查
        if (req.getUsername() == null || req.getPassword() == null ||
                req.getUsername().isBlank() || req.getPassword().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("帳號與密碼不可為空");
        }

        UserAccount u = authRepository.findByUsername(req.getUsername());
        if (u == null || !authRepository.passwordMatches(req.getPassword(), u.getPasswordHash())) {
            // 回應 401 + 簡單文字，前端 fetch(res.text()) 就可以拿到「帳號或密碼錯誤」
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("帳號或密碼錯誤");
        }
        if (!u.isActive()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("帳號未啟用");
        }

        // 登入成功 → 設定 session
        session.setAttribute("ROLE", u.getRole()); // ADMIN / CUSTOMER
        session.setAttribute("USER", u.getUsername()); // username
        session.setAttribute("USER_ID", u.getUserId()); // e.g. U0001

        return ResponseEntity.ok(
                new LoginResponse(u.getRole(), u.getUsername(), u.getUserId()));
    }

    // =======================
    // API：註冊
    // =======================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        // 1) 基本欄位檢查
        if (req.getUsername() == null || req.getPassword() == null || req.getRole() == null ||
                req.getUsername().isBlank() || req.getPassword().isBlank() || req.getRole().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("帳號、密碼與角色皆不可為空");
        }

        // 2) 檢查帳號是否已存在
        if (authRepository.findByUsername(req.getUsername()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("帳號已存在");
        }

        // 3) 檢查角色是否合法（只允許 ADMIN / CUSTOMER）
        String roleUpper = req.getRole().toUpperCase();
        if (!"ADMIN".equals(roleUpper) && !"CUSTOMER".equals(roleUpper)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("角色必須是 ADMIN 或 CUSTOMER");
        }

        // 4) 產生密碼雜湊並寫入資料庫
        try {
            String hash = authRepository.hashPassword(req.getPassword());
            authRepository.createUser(req.getUsername(), hash, req.getRole(), true);
            return ResponseEntity.ok("註冊成功");
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("註冊失敗: " + e.getMessage());
        }
    }

    // =======================
    // API：登出
    // =======================

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    // =======================
    // API：查詢目前登入狀態（給前端 guard 使用）
    // =======================

    @GetMapping("/me")
    public LoginResponse me(HttpSession session) {
        String role = (String) session.getAttribute("ROLE");
        String user = (String) session.getAttribute("USER");
        String userId = (String) session.getAttribute("USER_ID");

        if (role == null)
            role = "GUEST";
        if (user == null)
            user = "";
        // userId 可以為 null，前端拿到就是 null / undefined，再自己處理
        return new LoginResponse(role, user, userId);
    }
}
