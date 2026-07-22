package com.ironvault.auth.application;

import com.ironvault.auth.domain.enums.Role;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:#{null}}")
    private String adminEmail;

    @Value("${app.admin.password:#{null}}")
    private String adminPassword;

    public DataInitializer(UserRepositoryPort userRepositoryPort,
                           PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (adminEmail == null || adminPassword == null) {
            log.info("Admin seed skipped — ADMIN_EMAIL or ADMIN_PASSWORD not set");
            return;
        }

        if (userRepositoryPort.existsByEmail(adminEmail)) {
            log.info("Admin already exists — skipping seed email={}", adminEmail);
            return;
        }

        User admin = User.create(adminEmail, passwordEncoder.encode(adminPassword), Role.ADMIN);
        admin.setEmailConfirmed(true);
        userRepositoryPort.save(admin);

        log.info("Admin user created via seed email={}", adminEmail);
    }
}
