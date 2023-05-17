package com.company.dto;

import com.company.constant.SecurityConstants;
import com.company.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {
    String username;
    String email;
    Set<RoleDto> roles;
    String token;
    String tokenType;
}
