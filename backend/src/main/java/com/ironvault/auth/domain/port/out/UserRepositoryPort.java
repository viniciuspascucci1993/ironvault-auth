package com.ironvault.auth.domain.port.out;

import com.ironvault.auth.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    void save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
