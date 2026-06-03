package com.ironvault.auth.domain.port.in;

public interface ConfirmationEmailUseCase {
    void confirm(String token);
}
