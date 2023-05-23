package com.company.service.impl;

import com.company.dto.UserDto;
import com.company.entity.User;
import com.company.entity.UserDetailsImpl;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.UserNotFoundException;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.service.LoginAttemptService;
import com.company.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.company.constant.ExceptionMessageConstant.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final LoginAttemptService loginAttemptService;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.userListToDtoList(userList);
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        if (!ObjectUtils.isNotEmpty(user)) {
            log.info("user not found with this user: {} ", userName);
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND);
        }
        validateLoginAttempt(user);
        user.setLastLoginDate(LocalDateTime.now());
        user.setLastLoginDateDisplay(LocalDateTime.now());

        return new UserDetailsImpl(user);
    }

    @Override
    public UserDto getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with specified id"));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.save(userMapper.dtoToUser(userDto));

        user.setJoinDate(LocalDateTime.now());
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.saveAndFlush(userMapper.dtoToUser(userDto));
        return userMapper.userToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.isExceededMaxAttempts(user.getUserName())) {
                user.setNotLocked(false);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUserName());
        }
    }
}
