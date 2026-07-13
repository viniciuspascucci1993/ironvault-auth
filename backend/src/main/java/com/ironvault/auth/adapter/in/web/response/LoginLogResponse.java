package com.ironvault.auth.adapter.in.web.response;

import com.ironvault.auth.adapter.out.entity.LoginLogEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogResponse {

    private UUID id;
    private String email;
    private String ip;
    private String userAgent;
    private boolean success;
    private String failureReason;
    private Instant createdAt;

    public static LoginLogResponse of(LoginLogEntity entity) {
        LoginLogResponse response = new LoginLogResponse();
        response.setId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setIp(entity.getIp());
        response.setUserAgent(entity.getUserAgent());
        response.setSuccess(entity.isSuccess());
        response.setFailureReason(entity.getFailureReason());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
