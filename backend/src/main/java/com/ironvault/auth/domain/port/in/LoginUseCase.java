package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.adapter.in.web.response.AuthResponse;

public interface LoginUseCase {

    AuthResponse execute(String email, String password, String ip, String userAgent);
}
