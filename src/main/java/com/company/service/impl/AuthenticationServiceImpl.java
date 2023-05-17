package com.company.service.impl;

import com.company.constant.SecurityConstants;
import com.company.dto.AuthenticationRequest;
import com.company.dto.AuthenticationResponse;
import com.company.dto.UserDto;
import com.company.entity.User;
import com.company.entity.UserDetailsImpl;
import com.company.exception.domain.EmailExistsException;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.UserNameExistsException;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.security.jwt.JwtAuthenticationProvider;
import com.company.service.inter.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.company.constant.ExceptionMessageConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse register(UserDto userDto) throws UserNameExistsException, EmailExistsException {

        validateUserName(userDto.getUserName());
        validateEmail(userDto.getEmail());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setJoinDate(LocalDateTime.now());
        log.info("user dto object -------------------->{}", userDto);

        User newUser = userMapper.dtoToUser(userDto);

        userRepository.saveAndFlush(newUser);

        String token = jwtAuthenticationProvider.generateToken(new UserDetailsImpl(newUser));

        return new AuthenticationResponse(newUser.getUserName(), newUser.getEmail(),
                userMapper.roleListToDtoList(newUser.getRoles()), token, SecurityConstants.TOKEN_PREFIX);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws EmailNotFoundException {

        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetailsImpl.getUsername(), request.getPassword(), userDetailsImpl.getAuthorities());

        Authentication authenticate = authenticationManager.authenticate(authentication);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtAuthenticationProvider.generateToken(userDetailsImpl);

        log.info("Generated token -------->>>> {}", token);

        return new AuthenticationResponse(user.getUserName(), user.getEmail(),
                userMapper.roleListToDtoList(user.getRoles()), token, SecurityConstants.TOKEN_PREFIX);
    }

    private void validateUserName(String userName) throws UserNameExistsException {
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new UserNameExistsException(USERNAME_EXISTS);
        }
    }

    private void validateEmail(String email) throws EmailExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailExistsException(EMAIL_EXISTS);
        }
    }

}
