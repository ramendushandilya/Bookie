package com.bookworm.service;

import com.bookworm.domain.User;
import com.bookworm.domain.UserBilling;
import com.bookworm.domain.UserPayment;
import com.bookworm.domain.UserShipping;
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

    User save(User user);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

    void updateUserShipping(UserShipping userShipping, User user);

    void setUserDefaultPayment(Long defaultPaymentId, User user);

    void setDefaultShippingAddress(Long defaultShippingAddressId, User user);
}
