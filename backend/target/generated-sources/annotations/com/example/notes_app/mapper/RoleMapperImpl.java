package com.example.notes_app.mapper;

import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;
import com.example.notes_app.entity.RoleEntity;
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
public class RoleMapperImpl implements RoleMapper {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public RoleEntity toEntity(RoleRequest roleRequest) {
        if ( roleRequest == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setName( roleRequest.getName() );

        return roleEntity;
    }

    @Override
    public RoleResponse toResponse(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.id( roleEntity.getId() );
        roleResponse.name( roleEntity.getName() );
        roleResponse.permissions( permissionMapper.toResponseList( roleEntity.getPermissions() ) );

        return roleResponse.build();
    }

    @Override
    public List<RoleResponse> toResponseList(List<RoleEntity> roleEntities) {
        if ( roleEntities == null ) {
            return null;
        }

        List<RoleResponse> list = new ArrayList<RoleResponse>( roleEntities.size() );
        for ( RoleEntity roleEntity : roleEntities ) {
            list.add( toResponse( roleEntity ) );
        }

        return list;
    }

    @Override
    public void updateRoleFromRequest(RoleEntity roleEntity, RoleRequest roleRequest) {
        if ( roleRequest == null ) {
            return;
        }

        if ( roleRequest.getName() != null ) {
            roleEntity.setName( roleRequest.getName() );
        }
    }
}
