package com.studysphere.backend.dto;

import com.studysphere.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithRole {
    private String username;
    private Role role;
}