package com.ironvault.auth.domain.port.out;

import java.time.Instant;

public interface LoginLogRepositoryPort {
    void save(String email, String ip, String userAgent, boolean success, String failureReason, Instant createdAt);
}
