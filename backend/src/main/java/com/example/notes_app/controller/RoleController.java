package com.example.notes_app.controller;

import com.example.notes_app.dto.ApiResponse;
import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleResponse> roleResponse = ApiResponse.<RoleResponse>builder()
                .code(ErrorCode.ROLE_CREATED.getCode())
                .data(roleService.createRole(roleRequest))
                .build();
        return ResponseEntity.ok(roleResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        ApiResponse<List<RoleResponse>> roleResponse = ApiResponse.<List<RoleResponse>>builder()
                .code(ErrorCode.ROLE_GET_ALL.getCode())
                .data(roleService.getAllRoles())
                .build();
        return ResponseEntity.ok(roleResponse);
    }

    @GetMapping("/account")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRolesByAccountId(@RequestParam("accountId") Integer accountId) {
        ApiResponse<List<RoleResponse>> roleResponse = ApiResponse.<List<RoleResponse>>builder()
                .code(ErrorCode.ROLE_GET_ALL.getCode())
                .data(roleService.getRoleByAccountId(accountId))
                .build();
        return ResponseEntity.ok(roleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
        ApiResponse<?> roleResponse = ApiResponse.builder()
                .code(ErrorCode.ROLE_DELETED.getCode())
                .message(ErrorCode.ROLE_DELETED.getMessage())
                .build();
        return ResponseEntity.ok(roleResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable("id") Integer id,
            @RequestBody RoleRequest roleRequest
    ) {
        ApiResponse<RoleResponse> roleResponse = ApiResponse.<RoleResponse>builder()
                .code(ErrorCode.ROLE_UPDATED.getCode())
                .data(roleService.updateRole(id, roleRequest))
                .build();
        return ResponseEntity.ok(roleResponse);
    }
}
