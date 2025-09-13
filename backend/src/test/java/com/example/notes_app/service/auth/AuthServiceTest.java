package com.example.notes_app.service.auth;

import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.auth.request.LoginRequest;
import com.example.notes_app.dto.auth.request.RefreshTokenRequest;
import com.example.notes_app.dto.auth.request.RegisterRequest;
import com.example.notes_app.dto.auth.response.LoginResponse;
import com.example.notes_app.dto.auth.response.RefreshTokenResponse;
import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.impl.ResourceAlreadyExistsException;
import com.example.notes_app.mapper.AccountMapper;
import com.example.notes_app.mapper.auth.AuthMapper;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.security.JwtUtils;
import com.example.notes_app.service.auth.impl.AuthServiceImpl;
import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AuthMapper authMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    // --- TEST LOGIN ---
    @Test
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateAccessToken(authentication)).thenReturn("access-token");
        when(jwtUtils.generateRefreshToken(authentication)).thenReturn("refresh-token");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    // --- TEST REGISTER ---
    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", List.of(1));

        when(accountRepository.existsByEmail("test@example.com")).thenReturn(false);
        AccountEntity accountEntity = new AccountEntity();
        when(authMapper.toEntity(request)).thenReturn(accountEntity);
        when(roleRepository.findAllById(request.getRoleIds())).thenReturn(Collections.singletonList(new RoleEntity()));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        AccountEntity savedEntity = new AccountEntity();
        when(accountRepository.save(accountEntity)).thenReturn(savedEntity);
        AccountResponse expectedResponse = new AccountResponse();
        when(accountMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        AccountResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(accountRepository).save(accountEntity);
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", List.of(1));

        when(accountRepository.existsByEmail("test@example.com")).thenReturn(true);

        ResourceAlreadyExistsException exception =
                assertThrows(ResourceAlreadyExistsException.class, () -> authService.register(request));

        assertEquals(ErrorCode.ACCOUNT_EMAIL_ALREADY_EXIST, exception.getErrorCode());
        verify(accountRepository, never()).save(any());
    }

    // --- TEST REFRESH TOKEN ---
    @Test
    void testRefreshToken_Success() throws JOSEException {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh-token");

        when(jwtUtils.getEmail("refresh-token")).thenReturn("test@example.com");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(jwtUtils.generateAccessToken(any(Authentication.class))).thenReturn("new-access-token");

        RefreshTokenResponse response = authService.refreshToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.getNewAccessToken());
    }
}

