package com.example.notes_app.controller.auth;

import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.auth.request.LoginRequest;
import com.example.notes_app.dto.auth.request.RegisterRequest;
import com.example.notes_app.dto.auth.request.RefreshTokenRequest;
import com.example.notes_app.dto.auth.response.LoginResponse;
import com.example.notes_app.dto.auth.response.RefreshTokenResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser // giả lập user đã login, tránh 401
    void testLogin() throws Exception {
        LoginResponse mockResponse = new LoginResponse("access123", "refresh123");
        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        LoginRequest request = new LoginRequest("test@example.com", "password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.accessToken").value("access123"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh123"));
    }

    @Test
    @WithMockUser
    void testRegister() throws Exception {
        AccountResponse mockAccount = new AccountResponse(1, "test@example.com", true, null);
        Mockito.when(authService.register(any(RegisterRequest.class))).thenReturn(mockAccount);

        RegisterRequest request = new RegisterRequest("test@example.com", "password", null);

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.REGISTER_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    @WithMockUser
    void testRefreshToken() throws Exception {
        RefreshTokenResponse mockResponse = new RefreshTokenResponse("newAccess123");
        Mockito.when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(mockResponse);

        RefreshTokenRequest request = new RefreshTokenRequest("refresh123");

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.REFRESH_TOKEN.getCode()))
                .andExpect(jsonPath("$.data.newAccessToken").value("newAccess123"));
    }
}


