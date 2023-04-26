package com.company.service.impl;

import com.company.entity.User;
import com.company.entity.UserPrincipal;
import com.company.repository.UserRepository;
import com.company.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.company.constant.ExceptionMessageConstant.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    }

    @Override
    public UserPrincipal loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = getUserByUserName(userName);

        if (!ObjectUtils.isNotEmpty(user)) {
            log.info("user not found with this user: {} ", userName);
            throw new UsernameNotFoundException(USERNAME_NOT_FOUND);
        }

        user.setLastLoginDate(LocalDateTime.now());
        user.setLastLoginDateDisplay(LocalDateTime.now());

        return new UserPrincipal(user);
    }
}
