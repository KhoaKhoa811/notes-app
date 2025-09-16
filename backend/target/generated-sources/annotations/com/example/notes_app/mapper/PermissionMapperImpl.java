package com.example.notes_app.mapper;

import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.entity.PermissionEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-16T21:29:54+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public PermissionEntity toEntity(PermissionRequest permissionRequest) {
        if ( permissionRequest == null ) {
            return null;
        }

        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setName( permissionRequest.getName() );

        return permissionEntity;
    }

    @Override
    public List<PermissionEntity> toEntityList(List<PermissionRequest> permissionRequests) {
        if ( permissionRequests == null ) {
            return null;
        }

        List<PermissionEntity> list = new ArrayList<PermissionEntity>( permissionRequests.size() );
        for ( PermissionRequest permissionRequest : permissionRequests ) {
            list.add( toEntity( permissionRequest ) );
        }

        return list;
    }

    @Override
    public PermissionResponse toResponse(PermissionEntity permissionEntity) {
        if ( permissionEntity == null ) {
            return null;
        }

        PermissionResponse permissionResponse = new PermissionResponse();

        permissionResponse.setId( permissionEntity.getId() );
        permissionResponse.setName( permissionEntity.getName() );

        return permissionResponse;
    }

    @Override
    public List<PermissionResponse> toResponseList(List<PermissionEntity> permissionEntities) {
        if ( permissionEntities == null ) {
            return null;
        }

        List<PermissionResponse> list = new ArrayList<PermissionResponse>( permissionEntities.size() );
        for ( PermissionEntity permissionEntity : permissionEntities ) {
            list.add( toResponse( permissionEntity ) );
        }

        return list;
    }

    @Override
    public void updatePermissionFromRequest(PermissionEntity permissionEntity, PermissionRequest permissionRequest) {
        if ( permissionRequest == null ) {
            return;
        }

        if ( permissionRequest.getName() != null ) {
            permissionEntity.setName( permissionRequest.getName() );
        }
    }
}
