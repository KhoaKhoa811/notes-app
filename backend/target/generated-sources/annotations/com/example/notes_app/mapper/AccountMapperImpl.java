package com.example.notes_app.mapper;

import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.entity.AccountEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-16T21:29:54+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public AccountResponse toResponse(AccountEntity accountEntity) {
        if ( accountEntity == null ) {
            return null;
        }

        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setId( accountEntity.getId() );
        accountResponse.setEmail( accountEntity.getEmail() );
        accountResponse.setEnabled( accountEntity.getEnabled() );
        accountResponse.setRoles( roleMapper.toResponseList( accountEntity.getRoles() ) );

        return accountResponse;
    }

    @Override
    public List<AccountResponse> toResponseList(List<AccountEntity> accountEntities) {
        if ( accountEntities == null ) {
            return null;
        }

        List<AccountResponse> list = new ArrayList<AccountResponse>( accountEntities.size() );
        for ( AccountEntity accountEntity : accountEntities ) {
            list.add( toResponse( accountEntity ) );
        }

        return list;
    }
}
