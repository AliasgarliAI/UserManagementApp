package com.company.service;

import com.company.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

import static com.company.constant.ApplicationMessageConstants.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String verificationLink, User user) throws MessagingException, UnsupportedEncodingException {
        String text = buildEmailText(user.getFirstName(), verificationLink,EMAIL_VERIFICATION,SIGHUP_MESSAGE);
        String subject = "Email verification";
        sendEmailTo(subject, text, APPLICATION_NAME, user.getEmail());
    }

    public void sendEmailTo(String subject, String text, String sendFrom, String... emails) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(message,"utf-8");

        messageHelper.setFrom("info@usermanagementportal.az", sendFrom);
        messageHelper.setSubject(subject);
        messageHelper.setText(text);
        messageHelper.setTo(emails);

        javaMailSender.send(message);
    }

//    public void sendEmailToAndCc() {
//
//    }

    public static String buildEmailText(String name, String link, String header,String text) {

        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>" + header + "</title>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"font-family: Arial, sans-serif;\">\n" +
                "\n" +
                "    <h2>Email Verification</h2>\n" +
                "\n" +
                "    <p>Dear " + name + ",</p>\n" +
                "\n" +
                "    <p>"+ text +"</p>\n" +
                "\n" +
                "    <p>\n" +
                "        <a href=\"" + link + "\" style=\"display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none;\">\n" +
                "            Click Button\n" +
                "        </a>\n" +
                "    </p>\n" +
                "\n" +
                "    <p>If the above button doesn't work, you can also copy and paste the following URL into your web browser:</p>\n" +
                "\n" +
                "    <p>" + link + "</p>\n" +
                "\n" +
                "    <p>\n" +
                "        Note: This verification link will expire in 24 hours.\n" +
                "    </p>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
    }
}
