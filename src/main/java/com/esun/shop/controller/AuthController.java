package com.esun.shop.controller;

import com.esun.shop.model.UserAccount;
import com.esun.shop.repository.AuthRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthRepository authRepository;

    public AuthController(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

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

    public static class LoginResponse {
        private String role;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpSession session) {
        UserAccount u = authRepository.findByUsername(req.getUsername());
        if (u == null || !authRepository.passwordMatches(req.getPassword(), u.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
        }
        if (!u.isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("帳號未啟用");
        }
        session.setAttribute("ROLE", u.getRole());
        session.setAttribute("USER", u.getUsername());
        session.setAttribute("USER_ID", u.getUserId());
        return ResponseEntity.ok(new LoginResponse(u.getRole(), u.getUsername(), u.getUserId()));
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/me")
    public LoginResponse me(HttpSession session) {
        String role = (String) session.getAttribute("ROLE");
        String user = (String) session.getAttribute("USER");
        String userId = (String) session.getAttribute("USER_ID");
        if (role == null)
            role = "GUEST";
        if (user == null)
            user = "";
        return new LoginResponse(role, user, userId);
    }
}
