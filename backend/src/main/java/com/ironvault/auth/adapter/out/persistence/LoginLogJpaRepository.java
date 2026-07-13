package com.ironvault.auth.adapter.out.persistence;

import com.ironvault.auth.adapter.out.entity.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoginLogJpaRepository extends JpaRepository<LoginLogEntity, UUID> {
}
