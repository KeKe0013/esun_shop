package com.esun.shop.bootstrap;

import com.esun.shop.repository.AuthRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Configuration
public class DataInitializer {

    @Bean
    public Object seedDefaultUsers(AuthRepository authRepository) {
        if (authRepository.countUsers() == 0) {
            // 預設密碼：admin123 / cust123
            String adminHash = BCrypt.hashpw("admin123", BCrypt.gensalt(10));
            String custHash = BCrypt.hashpw("cust123", BCrypt.gensalt(10));
            authRepository.createUser("admin", adminHash, "ADMIN");
            authRepository.createUser("customer", custHash, "CUSTOMER");
            System.out.println("[INIT] Seeded default users: admin / customer");
        }
        return new Object();
    }
}
