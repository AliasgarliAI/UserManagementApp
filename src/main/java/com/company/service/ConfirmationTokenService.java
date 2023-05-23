package com.company.service;

import com.company.entity.ConfirmationToken;
import com.company.exception.domain.ConfirmationTokenExpiredException;
import com.company.exception.domain.EmailNotFoundException;
import com.company.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static com.company.constant.ExceptionMessageConstant.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private static final int MAX_SIZE_OF_TOKEN = 32;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public static String hashToken(String token) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = messageDigest.digest(token.getBytes());

        return convertBytesToHexString(hashedBytes);
    }

    public static ConfirmationToken generateConfirmationToken(String email) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[MAX_SIZE_OF_TOKEN];

        secureRandom.nextBytes(bytes);
        String token = convertBytesToHexString(bytes);

        return ConfirmationToken.builder().token(token)
                .createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusDays(1)).userEmail(email).build();
    }

    public boolean verifyConfirmationToken(String email, String userProvidedToken) throws EmailNotFoundException, ConfirmationTokenExpiredException {
        ConfirmationToken confirmationToken = getConfirmationToken(email);

        if (isTokenNotExpired(confirmationToken.getExpiresAt())) {
            if (userProvidedToken.equals(confirmationToken.getToken())) {
                confirmationToken.setConfirmedAt(LocalDateTime.now());
                return true;
            }
            return false;
        }
        throw new ConfirmationTokenExpiredException(CONFIRMATION_TOKEN_IS_EXPIRED);
    }

    public void saveToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getConfirmationToken(String email) throws EmailNotFoundException {
        return confirmationTokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(EMAIL_NOT_FOUND));
    }

    private static boolean isTokenNotExpired(LocalDateTime expirationDate) {
        return expirationDate.isAfter(LocalDateTime.now());
    }

    private static String convertBytesToHexString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
