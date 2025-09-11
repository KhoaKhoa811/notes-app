package com.example.notes_app.mapper;

import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.entity.PermissionEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    // convert permission request to permission entity
    PermissionEntity toEntity(PermissionRequest permissionRequest);
    List<PermissionEntity> toEntityList(List<PermissionRequest> permissionRequests);
    // convert permission entity to permission response
    PermissionResponse toResponse(PermissionEntity permissionEntity);
    List<PermissionResponse> toResponseList(List<PermissionEntity> permissionEntities);
    // update permission entity from permission request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePermissionFromRequest(@MappingTarget PermissionEntity permissionEntity, PermissionRequest permissionRequest);
}
