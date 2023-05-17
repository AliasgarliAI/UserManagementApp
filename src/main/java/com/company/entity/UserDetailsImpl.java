package com.company.entity;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails{
    private final User user;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = this.user.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return getAuthoritiesByRoles(roles)
                .stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }

    public List<String> getRoles(){
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }
    private Set<Authority> getAuthoritiesByRoles(Set<Role> roles) {
        Set<Authority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.addAll(role.getAuthorities());
        }
        return authorities;
    }

}
