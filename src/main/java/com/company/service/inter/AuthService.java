package com.company.service.inter;

import com.company.dto.AuthenticationRequest;
import com.company.dto.AuthenticationResponse;
import com.company.dto.HttpResponse;
import com.company.dto.UserDto;
import com.company.exception.domain.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface AuthService {
    AuthenticationResponse register(UserDto userDto) throws UserNameExistsException, EmailExistsException;
    AuthenticationResponse authenticate(AuthenticationRequest request) throws EmailNotFoundException;
    HttpResponse verifyEmail(String email,String token) throws EmailNotFoundException, ConfirmationTokenExpiredException, NoSuchAlgorithmException;

    HttpResponse resetPassword(String email) throws EmailNotFoundException, MessagingException, UnsupportedEncodingException;
    HttpResponse recoverPassword(String email,String token,String password,String passwordRepeat) throws EmailNotFoundException, ConfirmationTokenExpiredException, PasswordsMismatchException;
}
