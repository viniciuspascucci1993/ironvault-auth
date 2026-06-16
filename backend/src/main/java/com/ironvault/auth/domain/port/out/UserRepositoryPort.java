package com.ironvault.auth.domain.port.out;

import com.ironvault.auth.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    void save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID userId);
    boolean existsByEmail(String email);
    List<User> findAll();
}
