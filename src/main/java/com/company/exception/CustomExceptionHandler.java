package com.company.exception;

import static com.company.constant.ExceptionMessageConstant.*;

import com.company.dto.HttpResponse;
import com.company.exception.domain.ConfirmationTokenExpiredException;
import com.company.exception.domain.EmailExistsException;
import com.company.exception.domain.EmailNotFoundException;
import com.company.exception.domain.PasswordsMismatchException;
import com.company.util.ApplicationUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(DisabledException dex) {
        log.info(dex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.UNAUTHORIZED, ACCOUNT_DISABLED);
        return new ResponseEntity<>(httpResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> userNameNotFoundException(UsernameNotFoundException uex) {
        log.info(uex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.NOT_FOUND, USERNAME_NOT_FOUND);
        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException aex) {
        log.info(aex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
        return new ResponseEntity<>(httpResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> accountLockedException(LockedException lex) {
        log.info(lex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.LOCKED, ACCOUNT_LOCKED);
        return new ResponseEntity<>(httpResponse, HttpStatus.LOCKED);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<HttpResponse> emailExistsException(EmailExistsException eex) {
        log.info(eex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.NOT_ACCEPTABLE, EMAIL_EXISTS);
        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException eex) {
        log.info(eex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.NOT_FOUND, EMAIL_NOT_FOUND);
        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(ExpiredJwtException jex) {
        log.info(jex.getMessage());
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.BAD_REQUEST, CONFIRMATION_TOKEN_IS_EXPIRED);
        return new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.BAD_REQUEST, USER_OR_PASSWORD_INCORRECT);
        return new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException mex) {
        log.info(mex.getMessage());
        HttpMethod httpMethod = Objects.requireNonNull(mex.getSupportedHttpMethods()).iterator().next();
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_NOT_ALLOWED, httpMethod));
        return new ResponseEntity<>(httpResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ConfirmationTokenExpiredException.class)
    public ResponseEntity<HttpResponse> confirmationTokenExpiredException() {
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.NOT_ACCEPTABLE, CONFIRMATION_TOKEN_IS_EXPIRED);
        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(PasswordsMismatchException.class)
    public ResponseEntity<HttpResponse> newPassWordsMismatchException(){
        HttpResponse httpResponse = ApplicationUtil.getHttpResponseObject(HttpStatus.NOT_ACCEPTABLE,PASSWORD_MISMATCH);
        return new ResponseEntity<>(httpResponse,HttpStatus.NOT_ACCEPTABLE);
    }


}
