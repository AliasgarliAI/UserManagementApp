package com.company.dto;

import com.company.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    @Getter
    private Long id;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String email;
    @Getter
    private String password;
    @Getter
    @Setter
    private String profileImageUrl;
    @Setter
    private boolean isActive;
    @Setter
    private boolean isNotLocked;
    @Setter
    private LocalDateTime lastLoginDate;
    @Setter
    private LocalDateTime joinDate;
    @Getter
    @Setter
    Set<RoleDto> roles;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isActive() {
        return isActive;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isNotLocked() {
        return isNotLocked;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime getJoinDate() {
        return joinDate;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setPassword(String password) {
        this.password = password;
    }
}
