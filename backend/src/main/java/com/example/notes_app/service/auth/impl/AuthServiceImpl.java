package com.example.notes_app.service.auth.impl;

import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.auth.request.*;
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
import com.example.notes_app.service.auth.AuthService;
import com.nimbusds.jose.JOSEException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final AuthMapper authMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AccountMapper accountMapper;

    @Override
    public LoginResponse login(LoginRequest request) throws JOSEException {
        // authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        // save authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // generate token for the user
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AccountResponse register(RegisterRequest request) {
        // validate the request
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException(ErrorCode.ACCOUNT_EMAIL_ALREADY_EXIST);
        }
        // convert the request to entity
        AccountEntity accountEntity = authMapper.toEntity(request);
        // get roles by ids
        List<RoleEntity> roleEntities = roleRepository.findAllById(request.getRoleIds());
        accountEntity.setRoles(roleEntities);
        accountEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        accountEntity.setEnabled(true);
        // save the entity
        AccountEntity savedEntity = accountRepository.save(accountEntity);
        // convert the entity to response
        return accountMapper.toResponse(savedEntity);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {
            String email = jwtUtils.getEmail(request.getRefreshToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // Tạo Access Token mới
            String newAccessToken = jwtUtils.generateAccessToken(authentication);
            return new RefreshTokenResponse(newAccessToken);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
