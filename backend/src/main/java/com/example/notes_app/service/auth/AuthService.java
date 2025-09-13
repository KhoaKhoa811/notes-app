package com.example.notes_app.service.auth;

import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.auth.request.*;
import com.example.notes_app.dto.auth.response.LoginResponse;
import com.example.notes_app.dto.auth.response.RefreshTokenResponse;
import com.example.notes_app.dto.auth.response.RegisterResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthService {
    LoginResponse login(LoginRequest request) throws JOSEException;
    AccountResponse register(RegisterRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
