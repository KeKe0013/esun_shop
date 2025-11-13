package com.esun.shop.repository;

import com.esun.shop.model.UserAccount;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Objects;

@Repository
public class AuthRepository {

    private final JdbcTemplate jdbc;

    public AuthRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** 依 username 取得帳號（若無回傳 null） → 使用 SP：usp_FindUserByUsername */
    public UserAccount findByUsername(String username) {
        try {
            return jdbc.queryForObject(
                    "EXEC [dbo].[usp_FindUserByUsername] ?",
                    (rs, i) -> {
                        UserAccount u = new UserAccount();
                        u.setUserId(rs.getString("UserID")); // 不管 DB 是 int 或 varchar，都用字串接
                        u.setUsername(rs.getString("Username"));
                        u.setPasswordHash(rs.getString("PasswordHash"));
                        u.setRole(rs.getString("Role"));
                        u.setActive(rs.getBoolean("IsActive"));
                        u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                        return u;
                    },
                    username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /** 依 userId 取得帳號（若無回傳 null） → 使用 SP：usp_FindUserByUserId */
    public UserAccount findByUserId(String userId) {
        try {
            return jdbc.queryForObject(
                    "EXEC [dbo].[usp_FindUserByUserId] ?",
                    (rs, i) -> {
                        UserAccount u = new UserAccount();
                        u.setUserId(rs.getString("UserID"));
                        u.setUsername(rs.getString("Username"));
                        u.setPasswordHash(rs.getString("PasswordHash"));
                        u.setRole(rs.getString("Role"));
                        u.setActive(rs.getBoolean("IsActive"));
                        u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                        return u;
                    },
                    userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /** 使用者總數 → 使用 SP：usp_CountUsers */
    public int countUsers() {
        Integer c = jdbc.queryForObject(
                "EXEC [dbo].[usp_CountUsers]",
                Integer.class);
        return c == null ? 0 : c;
    }

    /** 建立使用者（讓 DB 自己產生 UserID，比如 IDENTITY） → 使用 SP：usp_CreateUser */
    public void createUser(String username, String passwordHash, String role, boolean isActive) {
        jdbc.update(
                "EXEC [dbo].[usp_CreateUser] ?, ?, ?, ?",
                username,
                passwordHash,
                role,
                isActive ? 1 : 0 // MSSQL BIT
        );
    }

    /** 密碼比對：支援 BCrypt，否則退回明文（僅開發測試） */
    public boolean passwordMatches(String raw, String stored) {
        if (raw == null || stored == null)
            return false;
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(raw, stored);
            } catch (Exception e) {
                return false;
            }
        }
        return Objects.equals(raw, stored);
    }

    /** 將明文密碼做 BCrypt 雜湊（註冊用） */
    public String hashPassword(String raw) {
        if (raw == null)
            return null;
        return BCrypt.hashpw(raw, BCrypt.gensalt());
    }
}
