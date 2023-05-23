package com.company.eventandlistener.listener;

import com.company.entity.ConfirmationToken;
import com.company.entity.User;
import com.company.eventandlistener.event.OnRegistrationCompleteEvent;
import com.company.service.ConfirmationTokenService;
import com.company.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteListener {
    private final ConfirmationTokenService tokenService;
    private final EmailService emailService;

    @EventListener
    public void onRegistrationCompleteEvent(OnRegistrationCompleteEvent event) throws MessagingException, UnsupportedEncodingException {
        User user = event.getUser();

        ConfirmationToken token = ConfirmationTokenService.generateConfirmationToken(user.getEmail());

        tokenService.saveToken(token);

        String verificationUrl = event.getApplicationUrl() + "register/verify/?email=" + user.getEmail() + "&token=" + token;

        emailService.sendVerificationEmail(verificationUrl,user);

    }
}
