package com.example.notes_app.mapper.auth;

import com.example.notes_app.dto.auth.request.RegisterRequest;
import com.example.notes_app.dto.auth.response.RegisterResponse;
import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.mapper.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface AuthMapper {
    // convert register request to account entity
    @Mapping(target = "roles", ignore = true)
    AccountEntity toEntity(RegisterRequest registerRequest);
    // convert account entity to register response
}
