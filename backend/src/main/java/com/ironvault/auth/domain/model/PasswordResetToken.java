package com.ironvault.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetToken {

    private UUID id;
    private UUID userId;
    private String token;
    private boolean used;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public PasswordResetToken() {}

    public PasswordResetToken(UUID id, UUID userId, String token,
                              boolean used, LocalDateTime expiresAt,
                              LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.used = used;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public static PasswordResetToken create(UUID userId) {
        return new PasswordResetToken(
                UUID.randomUUID(),
                userId,
                UUID.randomUUID().toString(),
                false,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now()
        );
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    // getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
