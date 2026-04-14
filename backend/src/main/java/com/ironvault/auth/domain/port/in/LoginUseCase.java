package com.ironvault.auth.domain.port.in;

public interface LoginUseCase {

    String execute(String email, String password);
}
