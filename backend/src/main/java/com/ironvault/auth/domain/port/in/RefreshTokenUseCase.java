package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.adapter.in.web.response.AuthResponse;

public interface RefreshTokenUseCase {

    AuthResponse refresh(String refreshToken);
}
