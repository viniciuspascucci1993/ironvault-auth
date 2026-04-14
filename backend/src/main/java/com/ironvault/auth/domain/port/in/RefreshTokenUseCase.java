package com.ironvault.auth.domain.port.in;

public interface RefreshTokenUseCase {

    String execute(String refreshToken);
}
