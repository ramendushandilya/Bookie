package com.bookworm.service;

import com.bookworm.domain.User;
import com.bookworm.domain.security.PasswordResetToken;
import com.bookworm.domain.security.UserRole;

import java.util.Set;

/**
 * Created by failedOptimus on 15-01-2018.
 */
public interface UserService {

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String name);

    User findByEmail(String email);

    User createUser(User user, Set<UserRole> userRoles) throws Exception;

}
