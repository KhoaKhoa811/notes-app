package com.example.notes_app.controller.auth;

import com.example.notes_app.dto.ApiResponse;
import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.auth.request.*;
import com.example.notes_app.dto.auth.response.LoginResponse;
import com.example.notes_app.dto.auth.response.RefreshTokenResponse;
import com.example.notes_app.dto.auth.response.RegisterResponse;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.auth.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) throws JOSEException {
        ApiResponse<LoginResponse> loginResponse =  ApiResponse.<LoginResponse>builder()
                .code(ErrorCode.LOGIN_SUCCESS.getCode())
                .data(authService.login(request))
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> register(@RequestBody RegisterRequest request) {
        ApiResponse<AccountResponse> registerResponse = ApiResponse.<AccountResponse>builder()
                .code(ErrorCode.REGISTER_SUCCESS.getCode())
                .data(authService.register(request))
                .build();
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) throws JOSEException {
        ApiResponse<RefreshTokenResponse> refreshTokenResponse = ApiResponse.<RefreshTokenResponse>builder()
                .code(ErrorCode.REFRESH_TOKEN.getCode())
                .data(authService.refreshToken(request))
                .build();
        return ResponseEntity.ok(refreshTokenResponse);
    }
}