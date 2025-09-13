package com.example.notes_app.dto.role;

import com.example.notes_app.dto.permission.PermissionResponse;
import jdk.jshell.Snippet;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private Integer id;
    private String name;
    private List<PermissionResponse> permissions;

}
