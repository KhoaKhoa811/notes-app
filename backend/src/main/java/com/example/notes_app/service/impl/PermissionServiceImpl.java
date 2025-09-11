package com.example.notes_app.service.impl;

import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.entity.PermissionEntity;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.impl.ResourceAlreadyExistsException;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.mapper.PermissionMapper;
import com.example.notes_app.repository.PermissionRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        if (permissionRepository.existsByName((permissionRequest.getName()))) {
            throw new ResourceAlreadyExistsException(ErrorCode.PERMISSION_ALREADY_EXIST);
        }
        PermissionEntity permissionEntity = permissionMapper.toEntity(permissionRequest);
        PermissionEntity savedEntity = permissionRepository.save(permissionEntity);
        return permissionMapper.toResponse(savedEntity);
    }

    @Override
    public PagedResponse<PermissionResponse> getAllPermission(Pageable pageable) {
        // find all permissions
        Page<PermissionEntity> permissionEntities = permissionRepository.findAll(pageable);
        // get a list of permissions
        List<PermissionResponse> permissionResponses = permissionMapper.toResponseList(permissionEntities.getContent());
        // mapping to paged response
        return PagedResponse.<PermissionResponse>builder()
                .content(permissionResponses)
                .page(permissionEntities.getNumber())
                .size(permissionEntities.getSize())
                .totalElements(permissionEntities.getTotalElements())
                .totalPages(permissionEntities.getTotalPages())
                .last(permissionEntities.isLast())
                .build();
    }

    @Override
    public PagedResponse<PermissionResponse> getPermissionByRoleId(Integer roleId, Pageable pageable) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND);
        }
        // find permissions by role id
        Page<PermissionEntity> permissionEntities = permissionRepository.findAllByRoleId(roleId, pageable);
        // get a list of permissions
        List<PermissionResponse> permissionResponses = permissionMapper.toResponseList(permissionEntities.getContent());
        // mapping to paged response
        return PagedResponse.<PermissionResponse>builder()
                .content(permissionResponses)
                .page(permissionEntities.getNumber())
                .size(permissionEntities.getSize())
                .totalElements(permissionEntities.getTotalElements())
                .totalPages(permissionEntities.getTotalPages())
                .last(permissionEntities.isLast())
                .build();
    }

    @Override
    public void deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionResponse updatePermission(Integer id, PermissionRequest permissionRequest) {
        if (permissionRepository.existsByName((permissionRequest.getName()))) {
            throw new ResourceAlreadyExistsException(ErrorCode.PERMISSION_ALREADY_EXIST);
        }
        PermissionEntity permissionEntity = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionMapper.updatePermissionFromRequest(permissionEntity, permissionRequest);
        PermissionEntity savedEntity = permissionRepository.save(permissionEntity);
        return permissionMapper.toResponse(savedEntity);
    }
}
