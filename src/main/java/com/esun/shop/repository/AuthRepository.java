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

    /** 依 username 取得帳號（若無回傳 null） */
    public UserAccount findByUsername(String username) {
        try {
            return jdbc.queryForObject("""
                    SELECT TOP 1 [UserID], [Username], [PasswordHash], [Role], [IsActive], [CreatedAt]
                    FROM [dbo].[Users]
                    WHERE [Username] = ?
                    """, (rs, i) -> {
                UserAccount u = new UserAccount();
                u.setUserId(rs.getString("UserID")); // ← 字串
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setRole(rs.getString("Role"));
                u.setActive(rs.getBoolean("IsActive"));
                u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return u;
            }, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /** 依 userId 取得帳號（若無回傳 null） */
    public UserAccount findByUserId(String userId) {
        try {
            return jdbc.queryForObject("""
                    SELECT [UserID], [Username], [PasswordHash], [Role], [IsActive], [CreatedAt]
                    FROM [dbo].[Users]
                    WHERE [UserID] = ?
                    """, (rs, i) -> {
                UserAccount u = new UserAccount();
                u.setUserId(rs.getString("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setRole(rs.getString("Role"));
                u.setActive(rs.getBoolean("IsActive"));
                u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return u;
            }, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /** 使用者總數（給初始化 seed 用） */
    public int countUsers() {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM [dbo].[Users]", Integer.class);
        return c == null ? 0 : c;
    }

    /** 4 參數：明確給定 userId */
    public void createUser(String userId, String username, String passwordHash, String role) {
        jdbc.update("""
                INSERT INTO [dbo].[Users]([UserID], [Username], [PasswordHash], [Role], [IsActive])
                VALUES(?, ?, ?, ?, 1)
                """, userId, username, passwordHash, role);
    }

    /** 3 參數 Overload：自動產生下一個 UserID（讓舊的 DataInitializer 可用） */
    public void createUser(String username, String passwordHash, String role) {
        String newId = nextUserId(); // 例如 U0001
        createUser(newId, username, passwordHash, role);
    }

    /** 產生下一個 UserID：以 MAX([UserID]) 的數字部分 + 1 */
    private String nextUserId() {
        String maxId = jdbc.queryForObject(
                "SELECT ISNULL(MAX([UserID]), 'U0000') FROM [dbo].[Users]",
                String.class);
        // maxId 形式預期為 Uxxxx（U 開頭 + 4 位數）
        int num = 0;
        try {
            num = Integer.parseInt(maxId.substring(1));
        } catch (Exception ignore) {
        }
        num += 1;
        return "U" + String.format("%04d", num);
    }

    /** 密碼比對 */
    public boolean passwordMatches(String raw, String stored) {
        if (stored == null)
            return false;
        // 判斷像 BCrypt 的格式：$2a$、$2b$、$2y$
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(raw, stored);
            } catch (Exception e) {
                return false;
            }
        }
        // 退而求其次：明文比對（僅限開發測試）
        return java.util.Objects.equals(raw, stored);
    }
}
