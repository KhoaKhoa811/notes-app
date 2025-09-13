package com.example.notes_app.service;

import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    List<RoleResponse> getAllRoles();
    List<RoleResponse> getRoleByAccountId(Integer accountId);
    void deleteRole(Integer id);
    RoleResponse updateRole(Integer id, RoleRequest roleRequest);
}
