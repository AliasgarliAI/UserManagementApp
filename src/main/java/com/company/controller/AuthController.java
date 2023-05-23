package com.company.controller;

import com.company.dto.*;
import com.company.exception.domain.*;
import com.company.service.inter.AuthService;
import liquibase.pro.packaged.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody UserDto userDto) throws UserNameExistsException, EmailExistsException {
        AuthenticationResponse authenticationResponse = authService.register(userDto);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest) throws EmailNotFoundException {
        AuthenticationResponse authenticationResponse = authService.authenticate(authenticationRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @GetMapping("/register/verify/")
    public ResponseEntity<HttpResponse> verifyAccount(@RequestParam(name = "email") String email, @RequestParam(name = "token") String token)
            throws EmailNotFoundException, NoSuchAlgorithmException, ConfirmationTokenExpiredException {
        return new ResponseEntity<>(authService.verifyEmail(email, token), HttpStatus.OK);
    }

    @GetMapping("/reset")
    public ResponseEntity<HttpResponse> resetPassword(@RequestParam("email") String email) throws EmailNotFoundException, MessagingException, UnsupportedEncodingException {
        return new ResponseEntity<>(authService.resetPassword(email), HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<HttpResponse> setNewPassword(@RequestParam("email") String email, @RequestParam("token") String token,
                                                       @RequestBody PasswordDto passwordDto)
            throws EmailNotFoundException, PasswordsMismatchException, ConfirmationTokenExpiredException {
        return new ResponseEntity<>(authService.recoverPassword(email, token, passwordDto.getPassword(), passwordDto.getNewPassword()), HttpStatus.OK);
    }
}
