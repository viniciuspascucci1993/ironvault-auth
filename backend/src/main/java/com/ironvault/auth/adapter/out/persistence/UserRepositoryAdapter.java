package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.UserEntity;
import com.ironvault.auth.domain.model.User;
import com.ironvault.auth.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(User user) {
        UserEntity entity = toEntity(user);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return jpaRepository.findById(userId).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
