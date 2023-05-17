package com.company.controller;

import com.company.dto.AuthenticationRequest;
import com.company.dto.AuthenticationResponse;
import com.company.dto.UserDto;
import com.company.exception.domain.EmailExistsException;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.UserNameExistsException;
import com.company.service.inter.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody UserDto userDto) throws UserNameExistsException, EmailExistsException {
        AuthenticationResponse authenticationResponse = authenticationService.register(userDto);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) throws EmailNotFoundException {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        return new ResponseEntity<>(authenticationResponse,HttpStatus.OK);
    }
}
