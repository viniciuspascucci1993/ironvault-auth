package com.ironvault.auth.adapter.out.mapper;

import com.ironvault.auth.adapter.out.entity.RefreshTokenEntity;
import com.ironvault.auth.domain.model.RefreshToken;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-23T14:08:09-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class RefreshTokenMapperImpl implements RefreshTokenMapper {

    @Override
    public RefreshToken toDomain(RefreshTokenEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setId( entity.getId() );
        refreshToken.setUserId( entity.getUserId() );
        refreshToken.setToken( entity.getToken() );
        refreshToken.setExpiresAt( entity.getExpiresAt() );
        refreshToken.setCreatedAt( entity.getCreatedAt() );
        refreshToken.setRevoked( entity.isRevoked() );

        return refreshToken;
    }

    @Override
    public RefreshTokenEntity toEntity(RefreshToken domain) {
        if ( domain == null ) {
            return null;
        }

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();

        refreshTokenEntity.setId( domain.getId() );
        refreshTokenEntity.setUserId( domain.getUserId() );
        refreshTokenEntity.setToken( domain.getToken() );
        refreshTokenEntity.setExpiresAt( domain.getExpiresAt() );
        refreshTokenEntity.setCreatedAt( domain.getCreatedAt() );
        refreshTokenEntity.setRevoked( domain.isRevoked() );

        return refreshTokenEntity;
    }
}
