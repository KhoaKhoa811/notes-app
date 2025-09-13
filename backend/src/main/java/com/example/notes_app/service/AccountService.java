package com.example.notes_app.service;

import com.example.notes_app.dto.account.AccountUpdateRequest;
import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.PagedResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface AccountService {
    PagedResponse<AccountResponse> getAllAccounts(Pageable pageable);
    AccountResponse getAccountById(Integer id);
    void deleteAccountById(Integer id);
    AccountResponse updateAccountById(Integer id, AccountUpdateRequest accountUpdateRequest);
}
