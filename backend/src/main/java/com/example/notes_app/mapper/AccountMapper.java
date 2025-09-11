package com.example.notes_app.mapper;

import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.entity.AccountEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface AccountMapper {
    // convert account entity to account response
    AccountResponse toResponse(AccountEntity accountEntity);
    List<AccountResponse> toResponseList(List<AccountEntity> accountEntities);
}
