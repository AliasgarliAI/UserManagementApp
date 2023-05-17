package com.company.dto;

import com.company.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable {
    private String role_id;
    private String roleName;

    private Set<AuthorityDto> authorities;

}
