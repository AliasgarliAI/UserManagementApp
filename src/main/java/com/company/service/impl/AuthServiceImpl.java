package com.company.service.impl;

import com.company.constant.ApplicationMessageConstants;
import com.company.constant.SecurityConstants;
import com.company.dto.AuthenticationRequest;
import com.company.dto.AuthenticationResponse;
import com.company.dto.HttpResponse;
import com.company.dto.UserDto;
import com.company.entity.ConfirmationToken;
import com.company.entity.User;
import com.company.entity.UserDetailsImpl;
import com.company.eventandlistener.event.OnRegistrationCompleteEvent;
import com.company.exception.domain.*;
import com.company.mapper.UserMapper;
import com.company.repository.UserRepository;
import com.company.security.jwt.JwtAuthenticationProvider;
import com.company.service.ConfirmationTokenService;
import com.company.service.EmailService;
import com.company.service.inter.AuthService;
import com.company.util.ApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;

import static com.company.constant.ExceptionMessageConstant.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final HttpServletRequest request;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    @Override
    public AuthenticationResponse register(UserDto userDto) throws UserNameExistsException, EmailExistsException {

        validateUserName(userDto.getUserName());
        validateEmail(userDto.getEmail());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setJoinDate(LocalDateTime.now());
        log.info("user dto object -------------------->{}", userDto);

        User newUser = userMapper.dtoToUser(userDto);

        userRepository.saveAndFlush(newUser);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUser, createApplicationUrl(request)));


        return new AuthenticationResponse(newUser.getUserName(), newUser.getEmail(),
                userMapper.roleListToDtoList(newUser.getRoles()), StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
        if (ObjectUtils.isEmpty(user.getLastLoginDate()) && user.isActive()) {
            user.setNotLocked(true);
        }

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

    @Override
    public HttpResponse verifyEmail(String email, String token) throws EmailNotFoundException, ConfirmationTokenExpiredException, NoSuchAlgorithmException {
        String hashedToken = ConfirmationTokenService.hashToken(token);
        log.error("email --> {} and token --> {}", email, token);
        if (confirmationTokenService.verifyConfirmationToken(email, hashedToken)) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));
            user.setActive(true);
            return HttpResponse.builder()
                    .timeStamp(new Date()).httpStatus(HttpStatus.OK)
                    .reason(HttpStatus.OK.getReasonPhrase().toUpperCase())
                    .httpStatusCode(HttpStatus.OK.value()).message("Account successfully verified").build();
        }

        return HttpResponse.builder().httpStatus(HttpStatus.NOT_ACCEPTABLE).httpStatusCode(HttpStatus.NOT_ACCEPTABLE.value())
                .timeStamp(new Date()).reason(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase().toUpperCase())
                .message(ACCOUNT_NOT_VERIFIED).build();
    }

    @Override
    public HttpResponse resetPassword(String email) throws EmailNotFoundException, MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));

        ConfirmationToken confirmationToken = ConfirmationTokenService.generateConfirmationToken(user.getEmail());

        confirmationTokenService.saveToken(confirmationToken);

        String url = createApplicationUrl(request) + "/reset?email=" + user.getEmail() + "&token=" + confirmationToken.getToken();
        String subject = "Password Recovery";
        String text = EmailService.buildEmailText(user.getFirstName(), url, subject, ApplicationMessageConstants.PASSWORD_RESET_MESSAGE);

        emailService.sendEmailTo(subject, text, ApplicationMessageConstants.APPLICATION_NAME, user.getEmail());

        return ApplicationUtil.getHttpResponseObject(HttpStatus.OK, ApplicationMessageConstants.OPERATION_PERFORMED_SUCCESSFULLY);
    }

    @Override
    public HttpResponse recoverPassword(String email, String token, String password, String passwordRepeat)
            throws EmailNotFoundException, ConfirmationTokenExpiredException, PasswordsMismatchException {
        if (!password.equals(passwordRepeat)) {
            throw new PasswordsMismatchException(PASSWORD_MISMATCH);
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));


        if (confirmationTokenService.verifyConfirmationToken(user.getEmail(), token)) {
            user.setPassword(passwordEncoder.encode(password));
            if (!user.isNotLocked()) {
                user.setNotLocked(true);
            }
            return ApplicationUtil.getHttpResponseObject(HttpStatus.OK,ApplicationMessageConstants.OPERATION_PERFORMED_SUCCESSFULLY);
        }
        return ApplicationUtil.getHttpResponseObject(HttpStatus.FORBIDDEN,"Provided token not recognized");
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

    private String createApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
    }
}
