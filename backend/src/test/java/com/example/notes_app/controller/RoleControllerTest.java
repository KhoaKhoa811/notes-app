package com.example.notes_app.controller;

import com.example.notes_app.dto.permission.PermissionResponse;
import com.example.notes_app.dto.role.RoleRequest;
import com.example.notes_app.dto.role.RoleResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private ObjectMapper objectMapper;

    private RoleResponse roleResponse;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();

        roleResponse = RoleResponse.builder()
                .id(1)
                .name("ADMIN")
                .build();
    }

    @Test
    @WithMockUser
    void testCreateRole() throws Exception {
        RoleRequest request = new RoleRequest();
        request.setName("ADMIN");

        Mockito.when(roleService.createRole(any(RoleRequest.class))).thenReturn(roleResponse);

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ROLE_CREATED.getCode()))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("ADMIN"));
    }

    @Test
    @WithMockUser
    void testGetAllRoles() throws Exception {
        Mockito.when(roleService.getAllRoles()).thenReturn(List.of(roleResponse));

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ROLE_GET_ALL.getCode()))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("ADMIN"));
    }

    @Test
    @WithMockUser
    void testGetRolesByAccountId() throws Exception {
        Mockito.when(roleService.getRoleByAccountId(1)).thenReturn(List.of(roleResponse));

        mockMvc.perform(get("/api/v1/roles/account")
                        .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ROLE_GET_ALL.getCode()))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("ADMIN"));
    }

    @Test
    @WithMockUser
    void testDeleteRole() throws Exception {
        mockMvc.perform(delete("/api/v1/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ROLE_DELETED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ROLE_DELETED.getMessage()));
    }

    @Test
    @WithMockUser
    void testUpdateRole() throws Exception {
        RoleRequest request = new RoleRequest();
        request.setName("USER");

        RoleResponse updatedRole = new RoleResponse(1, "USER", List.of(new PermissionResponse()));

        // FIX strict stubbing bằng any(RoleRequest.class)
        Mockito.when(roleService.updateRole(eq(1), any(RoleRequest.class)))
                .thenReturn(updatedRole);

        mockMvc.perform(patch("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ROLE_UPDATED.getCode()))
                .andExpect(jsonPath("$.data.name").value("USER"));
    }
}

