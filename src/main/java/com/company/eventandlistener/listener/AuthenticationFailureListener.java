package com.company.eventandlistener.listener;

import com.company.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFailureListener {
    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event){
        log.error("listener worked");
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof String) {
            log.error("inside of authentication failure listener");
            String userName = (String) principal;
            log.error("username {}",userName);
            loginAttemptService.addUserLoginAttemptCache(userName);
        }
    }
}
