package com.ironvault.auth.adapter.out.mapper;

import com.ironvault.auth.adapter.out.entity.RefreshTokenEntity;
import com.ironvault.auth.domain.model.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken toDomain(RefreshTokenEntity entity);
    RefreshTokenEntity toEntity(RefreshToken domain);
}
