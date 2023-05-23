package com.company.service;

import com.google.common.cache.CacheBuilder;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginAttemptService {
    private static final int MAXIMUM_LOGIN_ATTEMPTS = 5;
    private LoadingCache<String, Integer> loginAttempts;

    public LoginAttemptService() {
        super();
        loginAttempts = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .maximumSize(100)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void evictUserFromLoginAttemptCache(String userName) {
        loginAttempts.invalidate(userName);
    }

    public void addUserLoginAttemptCache(String userName) {

        int attempts;
        try {
            attempts = loginAttempts.get(userName);
            attempts++;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        loginAttempts.put(userName, attempts);
    }

    public boolean isExceededMaxAttempts(String userName) {
        try {
            return loginAttempts.get(userName) >= MAXIMUM_LOGIN_ATTEMPTS;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
