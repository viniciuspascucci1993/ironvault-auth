package com.ironvault.auth.adapter.in.web.response;


import com.ironvault.auth.domain.enums.Role;
import com.ironvault.auth.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private UUID id;
    private String email;
    private Role role;
    private boolean active;
    private boolean emailConfirmed;
    private LocalDateTime createdAt;

    public static UserResponse of(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActive(user.isActive());
        response.setEmailConfirmed(user.isEmailConfirmed());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
