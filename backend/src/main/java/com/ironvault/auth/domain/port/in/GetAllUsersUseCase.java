package com.ironvault.auth.domain.port.in;

import com.ironvault.auth.domain.model.User;

import java.util.List;

public interface GetAllUsersUseCase {
    List<User> execute();
}
