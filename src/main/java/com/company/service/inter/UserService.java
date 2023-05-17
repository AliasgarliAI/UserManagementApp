package com.company.service.inter;

import com.company.dto.UserDto;
import com.company.entity.User;
import com.company.exception.domain.EmailExistsException;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.UserNotFoundException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto getUserByUserName(String userName);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id) throws UserNotFoundException;
    UserDto getUserByEmail(String email) throws EmailNotFoundException;
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long id);

}
