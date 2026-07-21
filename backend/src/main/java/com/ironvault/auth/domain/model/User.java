package com.ironvault.auth.domain.model;

import com.ironvault.auth.domain.enums.ApprovalStatus;
import com.ironvault.auth.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String email;
    private String password;
    private Role role;
    private boolean active;
    private boolean emailConfirmed;
    private ApprovalStatus approvalStatus;
    private LocalDateTime createdAt;

    public User() { }

    public User(UUID id, String email,
                String password, Role role,
                boolean active,
                boolean emailConfirmed,
                ApprovalStatus approvalStatus,
                LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.emailConfirmed = emailConfirmed;
        this.approvalStatus = approvalStatus;
        this.createdAt = createdAt;
    }

    public static User create(String email, String encodedPassword, Role role) {
        return new User(
                UUID.randomUUID(),
                email,
                encodedPassword,
                role,
                true, // sempre ativo quando cadastrado pelo ADMIN
                false,
                ApprovalStatus.APPROVED, // sempre aprovado quando cadastrado pelo ADMIN
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
