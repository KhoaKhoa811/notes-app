package com.example.notes_app.service;

import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.account.AccountUpdateRequest;
import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.mapper.AccountMapper;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private RoleRepository roleRepository;

    private AccountService accountService;

    private AccountEntity accountEntity;
    private AccountResponse accountResponse;

    @BeforeEach
    void setup() {
        accountService = new AccountServiceImpl(accountRepository, accountMapper, roleRepository);

        accountEntity = new AccountEntity();
        accountEntity.setId(1);
        accountEntity.setEmail("test@example.com");
        accountEntity.setEnabled(true);

        accountResponse = new AccountResponse();
        accountResponse.setId(1);
        accountResponse.setEmail("test@example.com");
        accountResponse.setEnabled(true);
    }

    @Test
    void testGetAllAccounts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<AccountEntity> page = new PageImpl<>(List.of(accountEntity), pageable, 1);

        Mockito.when(accountRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(accountMapper.toResponse(accountEntity)).thenReturn(accountResponse);

        PagedResponse<AccountResponse> result = accountService.getAllAccounts(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testGetAccountById() {
        Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(accountEntity));
        Mockito.when(accountMapper.toResponse(accountEntity)).thenReturn(accountResponse);

        AccountResponse result = accountService.getAccountById(1);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testGetAccountById_NotFound() {
        Mockito.when(accountRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(2));
    }

    @Test
    void testDeleteAccountById() {
        Mockito.when(accountRepository.existsById(1)).thenReturn(true);
        accountService.deleteAccountById(1);
        Mockito.verify(accountRepository).deleteById(1);
    }

    @Test
    void testDeleteAccountById_NotFound() {
        Mockito.when(accountRepository.existsById(2)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccountById(2));
    }

    @Test
    void testUpdateAccountById() {
        AccountUpdateRequest request = new AccountUpdateRequest();
        request.setEnabled(false);
        request.setRoleIds(List.of(1));

        RoleEntity role = new RoleEntity();
        role.setId(1);

        Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(accountEntity));
        Mockito.when(roleRepository.findAllById(request.getRoleIds())).thenReturn(List.of(role));
        Mockito.when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
        Mockito.when(accountMapper.toResponse(accountEntity)).thenReturn(accountResponse);

        AccountResponse result = accountService.updateAccountById(1, request);

        assertThat(result.getEmail()).isEqualTo("test@example.com");
        Mockito.verify(accountRepository).save(accountEntity);
    }
}

