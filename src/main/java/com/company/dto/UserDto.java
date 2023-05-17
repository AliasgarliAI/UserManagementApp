package com.company.dto;

import com.company.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String profileImageUrl;
    private boolean isActive;
    private boolean isNotLocked;
    private LocalDateTime lastLoginDate;
    @JsonIgnore
    private LocalDateTime joinDate;
    Set<RoleDto> roles;

    public void setPassword(String password) {
        this.password = password;
    }
}
