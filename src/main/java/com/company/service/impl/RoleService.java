package com.company.service.impl;

import com.company.dto.RoleDto;
import com.company.entity.Role;
import com.company.mapper.UserMapper;
import com.company.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public Set<RoleDto> getAllRoles() {
        return userMapper.roleListToDtoList(roleRepository.findAll());
    }

    public Set<RoleDto> getRolesByAuthority(Long authorityId) {
        Set<Role> roles = roleRepository.findRoleByAuthority(authorityId).orElse(null);
        return userMapper.roleListToDtoList(roles);
    }

    public RoleDto createRole(RoleDto newRole) {
        Role role = roleRepository.saveAndFlush(userMapper.dtoToRole(newRole));
        return userMapper.roleToDto(role);
    }

    public RoleDto updateRole(RoleDto newRole){
        Role role = roleRepository.saveAndFlush(userMapper.dtoToRole(newRole));
        return userMapper.roleToDto(role);
    }
    public boolean deleteRole(Long id){
        roleRepository.deleteById(id);
        return true;
    }
}
