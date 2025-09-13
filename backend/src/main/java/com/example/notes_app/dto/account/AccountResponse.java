package com.example.notes_app.dto.account;

import com.example.notes_app.dto.role.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Integer id;
    private String email;
    private Boolean enabled;
    private List<RoleResponse> roles;
}
