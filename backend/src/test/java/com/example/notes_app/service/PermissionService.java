package com.example.notes_app.service;

import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.entity.PermissionEntity;
import com.example.notes_app.exception.impl.ResourceAlreadyExistsException;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.mapper.PermissionMapper;
import com.example.notes_app.repository.PermissionRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Test
    void testCreatePermission_Success() {
        PermissionRequest request = new PermissionRequest("READ_NOTES");
        PermissionEntity entity = new PermissionEntity();
        entity.setId(1);
        entity.setName("READ_NOTES");
        PermissionResponse response = new PermissionResponse(1, "READ_NOTES");

        Mockito.when(permissionRepository.existsByName("READ_NOTES")).thenReturn(false);
        Mockito.when(permissionMapper.toEntity(request)).thenReturn(entity);
        Mockito.when(permissionRepository.save(entity)).thenReturn(entity);
        Mockito.when(permissionMapper.toResponse(entity)).thenReturn(response);

        PermissionResponse result = permissionService.createPermission(request);

        assertThat(result.getName()).isEqualTo("READ_NOTES");
    }

    @Test
    void testCreatePermission_AlreadyExists() {
        PermissionRequest request = new PermissionRequest("READ_NOTES");

        Mockito.when(permissionRepository.existsByName("READ_NOTES")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> permissionService.createPermission(request));
    }

    @Test
    void testGetAllPermission() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        PermissionEntity entity = new PermissionEntity();
        entity.setId(1);
        entity.setName("WRITE_NOTES");
        PermissionResponse response = new PermissionResponse(1, "WRITE_NOTES");

        Page<PermissionEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        Mockito.when(permissionRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(permissionMapper.toResponseList(List.of(entity))).thenReturn(List.of(response));

        PagedResponse<PermissionResponse> result = permissionService.getAllPermission(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getName()).isEqualTo("WRITE_NOTES");
    }

    @Test
    void testGetPermissionByRoleId_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        PermissionEntity entity = new PermissionEntity();
        entity.setId(1);
        entity.setName("DELETE_NOTES");
        PermissionResponse response = new PermissionResponse(1, "DELETE_NOTES");

        Page<PermissionEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        Mockito.when(roleRepository.existsById(1)).thenReturn(true);
        Mockito.when(permissionRepository.findAllByRoleId(1, pageable)).thenReturn(page);
        Mockito.when(permissionMapper.toResponseList(List.of(entity))).thenReturn(List.of(response));

        PagedResponse<PermissionResponse> result = permissionService.getPermissionByRoleId(1, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getName()).isEqualTo("DELETE_NOTES");
    }

    @Test
    void testGetPermissionByRoleId_RoleNotFound() {
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(roleRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> permissionService.getPermissionByRoleId(2, pageable));
    }

    @Test
    void testDeletePermission_Success() {
        Mockito.when(permissionRepository.existsById(1)).thenReturn(true);

        permissionService.deletePermission(1);

        Mockito.verify(permissionRepository).deleteById(1);
    }

    @Test
    void testDeletePermission_NotFound() {
        Mockito.when(permissionRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> permissionService.deletePermission(2));
    }

    @Test
    void testUpdatePermission_Success() {
        PermissionRequest request = new PermissionRequest("UPDATE_NOTES");
        PermissionEntity entity = new PermissionEntity();
        entity.setId(1);
        entity.setName("OLD_NAME");
        PermissionResponse response = new PermissionResponse(1, "UPDATE_NOTES");

        Mockito.when(permissionRepository.existsByName("UPDATE_NOTES")).thenReturn(false);
        Mockito.when(permissionRepository.findById(1)).thenReturn(Optional.of(entity));
        Mockito.doAnswer(invocation -> {
            PermissionEntity argEntity = invocation.getArgument(0);
            argEntity.setName("UPDATE_NOTES");
            return null;
        }).when(permissionMapper).updatePermissionFromRequest(entity, request);
        Mockito.when(permissionRepository.save(entity)).thenReturn(entity);
        Mockito.when(permissionMapper.toResponse(entity)).thenReturn(response);

        PermissionResponse result = permissionService.updatePermission(1, request);

        assertThat(result.getName()).isEqualTo("UPDATE_NOTES");
    }

    @Test
    void testUpdatePermission_AlreadyExists() {
        PermissionRequest request = new PermissionRequest("UPDATE_NOTES");

        Mockito.when(permissionRepository.existsByName("UPDATE_NOTES")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> permissionService.updatePermission(1, request));
    }

    @Test
    void testUpdatePermission_NotFound() {
        PermissionRequest request = new PermissionRequest("UPDATE_NOTES");

        Mockito.when(permissionRepository.existsByName("UPDATE_NOTES")).thenReturn(false);
        Mockito.when(permissionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> permissionService.updatePermission(1, request));
    }
}

