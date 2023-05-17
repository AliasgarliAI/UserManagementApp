package com.company.service.inter;

import com.company.dto.AuthenticationRequest;
import com.company.dto.AuthenticationResponse;
import com.company.dto.UserDto;
import com.company.exception.domain.EmailExistsException;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.UserNameExistsException;

public interface AuthenticationService {
    AuthenticationResponse register(UserDto userDto) throws UserNameExistsException, EmailExistsException;
    AuthenticationResponse authenticate(AuthenticationRequest request) throws EmailNotFoundException;
}
