package com.ironvault.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class RefreshToken {

    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private boolean revoked;

    public RefreshToken() { }

    public RefreshToken(UUID id, UUID userId, String token, LocalDateTime expiresAt,
                        LocalDateTime createdAt, boolean revoked) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.revoked = revoked;
    }

    public static RefreshToken create(UUID userId, String token, long expirationMs) {
        return new RefreshToken(
                UUID.randomUUID(),
                userId,
                token,
                LocalDateTime.now().plusNanos(expirationMs * 1_000_000),
                LocalDateTime.now(),
                false
        );
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
