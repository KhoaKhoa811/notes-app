package com.example.notes_app.service;

import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;
import com.example.notes_app.entity.PermissionEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.exception.impl.ResourceAlreadyExistsException;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.mapper.RoleMapper;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.PermissionRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testCreateRole_Success() {
        RoleRequest request = new RoleRequest("ROLE_USER", List.of(1));
        RoleEntity entity = new RoleEntity();
        entity.setId(1);
        entity.setName("ROLE_USER");

        RoleResponse response = new RoleResponse(1, "ROLE_USER", List.of());

        Mockito.when(roleRepository.existsByName("ROLE_USER")).thenReturn(false);
        Mockito.when(roleMapper.toEntity(request)).thenReturn(entity);
        Mockito.when(permissionRepository.findAllById(request.getPermissionIds())).thenReturn(List.of());
        Mockito.when(roleRepository.save(entity)).thenReturn(entity);
        Mockito.when(roleMapper.toResponse(entity)).thenReturn(response);

        RoleResponse result = roleService.createRole(request);

        assertThat(result.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    void testCreateRole_AlreadyExists() {
        RoleRequest request = new RoleRequest("ROLE_USER", List.of(1));

        Mockito.when(roleRepository.existsByName("ROLE_USER")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> roleService.createRole(request));
    }

    @Test
    void testGetAllRoles() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1);
        entity.setName("ROLE_ADMIN");

        RoleResponse response = new RoleResponse(1, "ROLE_ADMIN", List.of());

        Mockito.when(roleRepository.findAll()).thenReturn(List.of(entity));
        Mockito.when(roleMapper.toResponseList(List.of(entity))).thenReturn(List.of(response));

        List<RoleResponse> results = roleService.getAllRoles();

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testGetRoleByAccountId_Success() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1);
        entity.setName("ROLE_ADMIN");

        RoleResponse response = new RoleResponse(1, "ROLE_ADMIN", List.of());

        Mockito.when(accountRepository.existsById(1)).thenReturn(true);
        Mockito.when(roleRepository.findByAccountId(1)).thenReturn(List.of(entity));
        Mockito.when(roleMapper.toResponseList(List.of(entity))).thenReturn(List.of(response));

        List<RoleResponse> results = roleService.getRoleByAccountId(1);

        assertThat(results).hasSize(1);
    }

    @Test
    void testGetRoleByAccountId_NotFound() {
        Mockito.when(accountRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleByAccountId(2));
    }

    @Test
    void testDeleteRole_Success() {
        Mockito.when(roleRepository.existsById(1)).thenReturn(true);

        roleService.deleteRole(1);

        Mockito.verify(roleRepository).deleteById(1);
    }

    @Test
    void testDeleteRole_NotFound() {
        Mockito.when(roleRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roleService.deleteRole(2));
    }

    @Test
    void testUpdateRole_Success() {
        RoleRequest request = new RoleRequest("ROLE_ADMIN", List.of(1));
        RoleEntity entity = new RoleEntity();
        entity.setId(1);
        entity.setName("ROLE_USER");

        RoleResponse response = new RoleResponse(1, "ROLE_ADMIN", List.of());

        Mockito.when(roleRepository.existsById(1)).thenReturn(true);
        Mockito.when(roleRepository.findById(1)).thenReturn(Optional.of(entity));
        Mockito.when(permissionRepository.findAllById(request.getPermissionIds())).thenReturn(List.of(new PermissionEntity()));
        Mockito.when(roleRepository.save(entity)).thenReturn(entity);
        Mockito.when(roleMapper.toResponse(entity)).thenReturn(response);

        RoleResponse result = roleService.updateRole(1, request);

        assertThat(result.getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testUpdateRole_NotFound() {
        RoleRequest request = new RoleRequest("ROLE_ADMIN", List.of());

        Mockito.when(roleRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roleService.updateRole(2, request));
    }
}
