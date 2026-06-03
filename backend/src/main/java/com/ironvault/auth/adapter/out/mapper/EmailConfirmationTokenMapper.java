package com.ironvault.auth.adapter.out.mapper;

import com.ironvault.auth.adapter.out.entity.EmailConfirmationTokenEntity;
import com.ironvault.auth.domain.model.EmailConfirmationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailConfirmationTokenMapper {

    EmailConfirmationToken toDomain(EmailConfirmationTokenEntity entity);
    EmailConfirmationTokenEntity toEntity(EmailConfirmationToken domain);
}
