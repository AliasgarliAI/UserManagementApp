package com.company.exception;

import static com.company.constant.ExceptionMessageConstant.*;

import com.company.dto.HttpResponse;
import com.company.exception.domain.EmailExistsException;
import com.company.exception.domain.EmailNotFoundException;
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
import java.util.Date;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(DisabledException dex) {
        log.info(dex.getMessage());
        return createExceptionResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> userNameNotFoundException(UsernameNotFoundException uex) {
        log.info(uex.getMessage());
        return createExceptionResponse(HttpStatus.BAD_REQUEST, USERNAME_NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException aex) {
        log.info(aex.getMessage());
        return createExceptionResponse(HttpStatus.FORBIDDEN, NOT_ENOUGHT_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> accountLockedException(LockedException lex) {
        log.info(lex.getMessage());
        return createExceptionResponse(HttpStatus.LOCKED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<HttpResponse> emailExistsException(EmailExistsException eex) {
        log.info(eex.getMessage());
        return createExceptionResponse(HttpStatus.NOT_ACCEPTABLE, eex.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException eex) {
        log.info(eex.getMessage());
        return createExceptionResponse(HttpStatus.NOT_FOUND, eex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(ExpiredJwtException jex) {
        log.info(jex.getMessage());
        return createExceptionResponse(HttpStatus.BAD_REQUEST, jex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException bex) {
        bex.printStackTrace();
        return createExceptionResponse(HttpStatus.BAD_REQUEST, bex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException mex) {
        log.info(mex.getMessage());
        HttpMethod httpMethod = Objects.requireNonNull(mex.getSupportedHttpMethods()).iterator().next();
        return createExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_NOT_ALLOWED, httpMethod));
    }


    private ResponseEntity<HttpResponse> createExceptionResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message, new Date());
        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}
