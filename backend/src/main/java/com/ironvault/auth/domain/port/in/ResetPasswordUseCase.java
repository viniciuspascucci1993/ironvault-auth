package com.ironvault.auth.domain.port.in;

public interface ResetPasswordUseCase {
    void execute(String token, String newPassword);
}
