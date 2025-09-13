package com.example.notes_app.service.impl;

import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.account.AccountUpdateRequest;
import com.example.notes_app.entity.AccountEntity;
import com.example.notes_app.entity.RoleEntity;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.impl.ResourceNotFoundException;
import com.example.notes_app.mapper.AccountMapper;
import com.example.notes_app.repository.AccountRepository;
import com.example.notes_app.repository.RoleRepository;
import com.example.notes_app.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final RoleRepository roleRepository;

    @Override
    public PagedResponse<AccountResponse> getAllAccounts(Pageable pageable) {
        Page<AccountEntity> accountPage = accountRepository.findAll(pageable);
        // get a list of accounts
        List<AccountResponse> content = accountPage.getContent()
                .stream()
                .map(accountMapper::toResponse)
                .toList();
        // mapping to paged response
        return PagedResponse.<AccountResponse>builder()
                .content(content)
                .page(accountPage.getNumber())
                .size(accountPage.getSize())
                .totalElements(accountPage.getTotalElements())
                .totalPages(accountPage.getTotalPages())
                .last(accountPage.isLast())
                .build();
    }

    @Override
    public AccountResponse getAccountById(Integer id) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        return accountMapper.toResponse(accountEntity);
    }

    @Override
    public void deleteAccountById(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AccountResponse updateAccountById(Integer id, AccountUpdateRequest accountUpdateRequest) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        if (accountUpdateRequest.getEnabled() != null) {
            accountEntity.setEnabled(accountUpdateRequest.getEnabled());
        }
        if (accountUpdateRequest.getRoleIds() != null) {
            List<RoleEntity> roleEntities = roleRepository.findAllById(accountUpdateRequest.getRoleIds());
            accountEntity.setRoles(roleEntities);
        }
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return accountMapper.toResponse(savedAccount);
    }
}
