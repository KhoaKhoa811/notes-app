package com.example.notes_app.controller.auth;

import com.example.notes_app.dto.auth.request.LoginRequest;
import com.example.notes_app.dto.auth.request.RegisterRequest;
import com.example.notes_app.entity.PermissionEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.repository.PermissionRepository;
import com.example.notes_app.repository.RoleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // ✅ đúng import
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @BeforeEach
    void setup() {
        if (permissionRepository.findByName("CREATE_ROLES").isEmpty()) {
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setName("CREATE_ROLES");
            permissionRepository.save(permissionEntity);
        }
        // seed role mặc định nếu chưa có
        if (roleRepository.findByName("USER").isEmpty()) {
            RoleEntity role = new RoleEntity();
            role.setName("USER");
            role.setPermissions(List.of(permissionRepository.findByName("CREATE_ROLES").get()));
            roleRepository.save(role);
        }
    }


    @Test
    void testRegisterAndLogin() throws Exception {
        // 1. Register
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test" + System.currentTimeMillis() + "@example.com"); // tránh trùng email
        registerRequest.setPassword("123456");
        registerRequest.setRoleIds(List.of(roleRepository.findByName("USER").get().getId()));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(registerRequest.getEmail()));

        // 2. Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(registerRequest.getEmail()); // login bằng email vừa tạo
        loginRequest.setPassword("123456");

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn();

        // 3. Lấy token từ response
        String responseBody = loginResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String token = responseJson.path("data").path("accessToken").asText();

        // 4. Gọi API cần xác thực
        mockMvc.perform(get("/api/v1/notes")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }
}

