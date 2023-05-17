package com.company.mapper;

import com.company.dto.AuthorityDto;
import com.company.dto.RoleDto;
import com.company.dto.UserDto;
import com.company.entity.Authority;
import com.company.entity.Role;
import com.company.entity.User;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface  UserMapper {
    // user mapping
    UserDto userToDto(User user);
    User dtoToUser(UserDto dto);
    List<UserDto> userListToDtoList(List<User> userList);
    List<User> dtoListToUserList(List<UserDto> dtoList);


    // role mapping
    RoleDto roleToDto(Role role);
    Role dtoToRole(RoleDto dto);
    Set<RoleDto> roleListToDtoList(Collection<Role> roleList);
    Set<Role> dtoListToRoleList(List<RoleDto> dtoList);


    // authority mapping
    AuthorityDto authorityToDto(Authority authority);
    Authority dtoToAuthority(AuthorityDto dto);
    Set<AuthorityDto> authorityListToDtoList(List<Authority> authorityList);
    Set<Authority> dtoListToAuthorityList(List<AuthorityDto> dtoList);
}
