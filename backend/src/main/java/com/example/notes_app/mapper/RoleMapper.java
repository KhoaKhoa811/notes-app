package com.example.notes_app.mapper;

import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;
import com.example.notes_app.entity.RoleEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    // convert role request to role entity
    @Mapping(target = "permissions", ignore = true)
    RoleEntity toEntity(RoleRequest roleRequest);
    // convert role entity to role response
    RoleResponse toResponse(RoleEntity roleEntity);
    List<RoleResponse> toResponseList(List<RoleEntity> roleEntities);
    // update role entity from role request
    @Mapping(target = "permissions", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromRequest(@MappingTarget RoleEntity roleEntity, RoleRequest roleRequest);
}
