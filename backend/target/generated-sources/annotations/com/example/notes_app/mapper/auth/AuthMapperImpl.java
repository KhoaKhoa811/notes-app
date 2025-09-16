package com.example.notes_app.mapper.auth;

import com.example.notes_app.dto.auth.request.RegisterRequest;
import com.example.notes_app.entity.AccountEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-16T21:29:54+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public AccountEntity toEntity(RegisterRequest registerRequest) {
        if ( registerRequest == null ) {
            return null;
        }

        AccountEntity.AccountEntityBuilder accountEntity = AccountEntity.builder();

        accountEntity.email( registerRequest.getEmail() );
        accountEntity.password( registerRequest.getPassword() );

        return accountEntity.build();
    }
}
