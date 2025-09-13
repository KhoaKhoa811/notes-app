package com.example.notes_app.controller;

import com.example.notes_app.config.PaginationProperties;
import com.example.notes_app.dto.ApiResponse;
import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.PaginationRequest;
import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.PermissionService;
import com.example.notes_app.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;
    private final PaginationProperties paginationProperties;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(@RequestBody PermissionRequest permissionRequest) {
        ApiResponse<PermissionResponse> permissionResponse = ApiResponse.<PermissionResponse>builder()
                .code(ErrorCode.PERMISSION_CREATED.getCode())
                .data(permissionService.createPermission(permissionRequest))
                .build();
        return ResponseEntity.ok(permissionResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<PermissionResponse>>> getAllPermissions(
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        Pageable pageable = PaginationUtils.createPageable(paginationRequest, paginationProperties);
        ApiResponse<PagedResponse<PermissionResponse>> permissionResponse = ApiResponse.<PagedResponse<PermissionResponse>>builder()
                .code(ErrorCode.PERMISSION_GET_ALL.getCode())
                .data(permissionService.getAllPermission(pageable))
                .build();
        return ResponseEntity.ok(permissionResponse);
    }

    @GetMapping("/role")
    public ResponseEntity<ApiResponse<PagedResponse<PermissionResponse>>> getPermissionByRoleId(
            @RequestParam("roleId") Integer roleId,
            @ModelAttribute PaginationRequest paginationRequest
    ) {
        Pageable pageable = PaginationUtils.createPageable(paginationRequest, paginationProperties);
        ApiResponse<PagedResponse<PermissionResponse>> permissionResponse = ApiResponse.<PagedResponse<PermissionResponse>>builder()
                .code(ErrorCode.PERMISSION_GET_ALL.getCode())
                .data(permissionService.getPermissionByRoleId(roleId, pageable))
                .build();
        return ResponseEntity.ok(permissionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deletePermission(@PathVariable("id") Integer id) {
        permissionService.deletePermission(id);
        ApiResponse<?> permissionResponse = ApiResponse.builder()
                .code(ErrorCode.PERMISSION_DELETED.getCode())
                .message(ErrorCode.PERMISSION_DELETED.getMessage())
                .build();
        return ResponseEntity.ok(permissionResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(
            @PathVariable("id") Integer id,
            @RequestBody PermissionRequest permissionRequest) {
        ApiResponse<PermissionResponse> permissionResponse = ApiResponse.<PermissionResponse>builder()
                .code(ErrorCode.PERMISSION_UPDATED.getCode())
                .data(permissionService.updatePermission(id, permissionRequest))
                .build();
        return ResponseEntity.ok(permissionResponse);
    }
}
