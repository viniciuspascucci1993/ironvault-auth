package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.LoginLogEntity;
import com.ironvault.auth.domain.port.out.LoginLogRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LoginLogRepositoryAdapter implements LoginLogRepositoryPort {

    private final LoginLogJpaRepository loginLogJpaRepository;

    public LoginLogRepositoryAdapter(LoginLogJpaRepository loginLogJpaRepository) {
        this.loginLogJpaRepository = loginLogJpaRepository;
    }

    @Override
    public void save(String email, String ip, String userAgent, boolean success,
                     String failureReason, Instant createdAt) {

        LoginLogEntity loginLogEntity = new LoginLogEntity();

        loginLogEntity.setEmail(email);
        loginLogEntity.setIp(ip);
        loginLogEntity.setUserAgent(userAgent);
        loginLogEntity.setSuccess(success);
        loginLogEntity.setFailureReason(failureReason);
        loginLogEntity.setCreatedAt(createdAt);

        loginLogJpaRepository.save(loginLogEntity);

    }
}
