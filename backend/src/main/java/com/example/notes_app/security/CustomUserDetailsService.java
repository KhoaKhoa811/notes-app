package com.example.notes_app.security;

import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // get account by email
        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_EMAIL_NOT_FOUND));
        // return a CustomUserDetails
        return new CustomUserDetails(accountEntity, getAuthorities(accountEntity));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AccountEntity accountEntity) {
        // a new list GrantAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity role : accountEntity.getRoles()) {
            // get role and attach a prefix ROLE_
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())); // a new SimpleGranteAuthority
            // get permission based on role
            role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        }
        return authorities;
    }
}
