package com.ironvault.auth.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String email;
    private String role;

    public static AuthResponse of(String token, long expiresIn, String email, String role) {
        return new AuthResponse(token, "Bearer", expiresIn, email, role);
    }
}
