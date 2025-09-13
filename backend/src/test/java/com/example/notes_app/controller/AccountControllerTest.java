package com.example.notes_app.controller;

import com.example.notes_app.config.PaginationProperties;
import com.example.notes_app.dto.PagedResponse;
import com.example.notes_app.dto.account.AccountResponse;
import com.example.notes_app.dto.account.AccountUpdateRequest;
import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @Mock
    private PaginationProperties paginationProperties;

    @InjectMocks
    private AccountController accountController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @BeforeEach
    void setUp() {
        lenient().when(paginationProperties.getDefaultPage()).thenReturn(0);
        lenient().when(paginationProperties.getDefaultSize()).thenReturn(10);
        lenient().when(paginationProperties.getDefaultSortBy()).thenReturn("id");
        lenient().when(paginationProperties.getDefaultSortDir()).thenReturn("asc"); // rất quan trọng
    }

    @Test
    @WithMockUser
    void testGetAllAccounts() throws Exception {
        PagedResponse<AccountResponse> mockPagedResponse = new PagedResponse<>(List.of(), 0, 0, 0, 0, true);
        Mockito.when(accountService.getAllAccounts(any(Pageable.class)))
                .thenReturn(mockPagedResponse);

        mockMvc.perform(get("/api/v1/accounts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetAccountById() throws Exception {
        AccountResponse accountResponse = new AccountResponse(1, "test@example.com", true, null);

        Mockito.when(accountService.getAccountById(1)).thenReturn(accountResponse);

        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_GET_ALL.getCode()))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void testDeleteAccountById() throws Exception {
        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_DELETED.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ACCOUNT_DELETED.getMessage()));
    }

    @Test
    @WithMockUser
    void testUpdateAccountById() throws Exception {
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setEnabled(true);
        AccountResponse updatedResponse = new AccountResponse(1, "updated@example.com", true, null);

        Mockito.when(accountService.updateAccountById(any(Integer.class), any(AccountUpdateRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(patch("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ErrorCode.ACCOUNT_UPDATED.getCode()))
                .andExpect(jsonPath("$.data.email").value("updated@example.com"));
    }
}

