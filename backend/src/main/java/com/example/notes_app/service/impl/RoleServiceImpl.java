package com.example.notes_app.service.impl;

import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;
import com.example.notes_app.entity.PermissionEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.impl.ResourceAlreadyExistsException;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.mapper.RoleMapper;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.PermissionRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw  new ResourceAlreadyExistsException(ErrorCode.ROLE_ALREADY_EXIST);
        }
        // Convert RoleRequest to RoleEntity
        RoleEntity roleEntity = roleMapper.toEntity(roleRequest);
        // find permissions by ids
        List<PermissionEntity> permissionEntities = permissionRepository.findAllById(roleRequest.getPermissionIds());
        // set permissions to role entity
        roleEntity.setPermissions(permissionEntities);
        // save role entity to db
        RoleEntity savedEntity = roleRepository.save(roleEntity);
        return roleMapper.toResponse(savedEntity);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleMapper.toResponseList(roleEntities);
    }

    @Override
    public List<RoleResponse> getRoleByAccountId(Integer accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        List<RoleEntity> roleEntities = roleRepository.findByAccountId(accountId);
        return roleMapper.toResponseList(roleEntities);
    }

    @Override
    public void deleteRole(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public RoleResponse updateRole(Integer id, RoleRequest roleRequest) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND);
        }
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND));
        // update role entity
        roleMapper.updateRoleFromRequest(roleEntity, roleRequest);
        // find permissions by ids
        if (roleRequest.getPermissionIds() != null) {
            List<PermissionEntity> permissionEntities = permissionRepository.findAllById(roleRequest.getPermissionIds());
            // set permissions to role entity
            roleEntity.setPermissions(permissionEntities);
        }
        // save role entity to db
        RoleEntity updatedEntity = roleRepository.save(roleEntity);
        return roleMapper.toResponse(updatedEntity);
    }
}