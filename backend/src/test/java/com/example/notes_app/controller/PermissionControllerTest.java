package com.example.notes_app.controller;

import com.example.notes_app.config.PaginationProperties;
import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.permission.PermissionRequest;
import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PermissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionService permissionService;

    @Mock
    private PaginationProperties paginationProperties;

    @InjectMocks
    private PermissionController permissionController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();

        // default pagination values
        lenient().when(paginationProperties.getDefaultPage()).thenReturn(0);
        lenient().when(paginationProperties.getDefaultSize()).thenReturn(10);
        lenient().when(paginationProperties.getDefaultSortBy()).thenReturn("id");
        lenient().when(paginationProperties.getDefaultSortDir()).thenReturn("asc");
    }

    @Test
    @WithMockUser
    void testCreatePermission() throws Exception {
        PermissionRequest request = new PermissionRequest();
        request.setName("READ");
        PermissionResponse response = new PermissionResponse(1, "READ");

        Mockito.when(permissionService.createPermission(any(PermissionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.PERMISSION_CREATED.getCode()))
                .andExpect(jsonPath("$.data.name").value("READ"));
    }

    @Test
    @WithMockUser
    void testGetAllPermissions() throws Exception {
        PagedResponse<PermissionResponse> pagedResponse = new PagedResponse<>(List.of(), 0, 0, 0, 0, true);
        Mockito.when(permissionService.getAllPermission(any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/permissions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.PERMISSION_GET_ALL.getCode()));
    }

    @Test
    @WithMockUser
    void testGetPermissionByRoleId() throws Exception {
        PagedResponse<PermissionResponse> pagedResponse = new PagedResponse<>(List.of(), 0, 0, 0, 0, true);
        Mockito.when(permissionService.getPermissionByRoleId(any(Integer.class), any(Pageable.class)))
                .thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/permissions/role")
                        .param("roleId", "1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.PERMISSION_GET_ALL.getCode()));
    }

    @Test
    @WithMockUser
    void testDeletePermission() throws Exception {
        mockMvc.perform(delete("/api/v1/permissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.PERMISSION_DELETED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.PERMISSION_DELETED.getMessage()));
    }

    @Test
    @WithMockUser
    void testUpdatePermission() throws Exception {
        PermissionRequest request = new PermissionRequest();
        request.setName("WRITE");
        PermissionResponse response = new PermissionResponse(1, "WRITE");

        Mockito.when(permissionService.updatePermission(any(Integer.class), any(PermissionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/permissions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.PERMISSION_UPDATED.getCode()))
                .andExpect(jsonPath("$.data.name").value("WRITE"));
    }
}
