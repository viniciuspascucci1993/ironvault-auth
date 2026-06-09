package com.ironvault.auth.adapter.out.mapper;

import com.ironvault.auth.adapter.out.entity.PasswordResetTokenEntity;
import com.ironvault.auth.domain.model.PasswordResetToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenMapper {
    PasswordResetToken toDomain(PasswordResetTokenEntity entity);
    PasswordResetTokenEntity toEntity(PasswordResetToken domain);
}
