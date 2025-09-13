package com.example.notes_app.service;

import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    PagedResponse<PermissionResponse> getAllPermission(Pageable pageable);
    PagedResponse<PermissionResponse> getPermissionByRoleId(Integer roleId, Pageable pageable);
    void deletePermission(Integer id);
    PermissionResponse updatePermission(Integer id, PermissionRequest permissionRequest);
}
