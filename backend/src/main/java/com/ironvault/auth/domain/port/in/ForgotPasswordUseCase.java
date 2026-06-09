package com.ironvault.auth.domain.port.in;

public interface ForgotPasswordUseCase {
    void execute(String email);
}
