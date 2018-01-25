package com.bookworm.service.impl;

import com.bookworm.domain.User;
import com.bookworm.domain.UserBilling;
import com.bookworm.domain.UserPayment;
import com.bookworm.domain.UserShipping;
import com.bookworm.domain.security.PasswordResetToken;
import com.bookworm.domain.security.UserRole;
import com.bookworm.repository.*;
import com.bookworm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by failedOptimus on 15-01-2018.
 */

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserShippingRespository userShippingRespository;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception{
        User localUser = userRepository.findByUsername(user.getUsername());

        if(localUser != null) {
            LOG.info("User {} already exits", user.getUsername());
        } else {
            for(UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            localUser = userRepository.save(user);
        }

        return localUser;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
        userPayment.setUser(user);
        userPayment.setUserBilling(userBilling);
        userPayment.setDefaultPayment(true);
        userBilling.setUserPayment(userPayment);
        user.getUserPaymentList().add(userPayment);
        save(user);
    }

    @Override
    public void updateUserShipping(UserShipping userShipping, User user) {
        userShipping.setUser(user);
        userShipping.setUserShippingDefault(true);
        user.getUserShippingList().add(userShipping);
        save(user);
    }

    @Override
    public void setUserDefaultPayment(Long defaultPaymentId, User user) {
        List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
        for(UserPayment up : userPaymentList) {
            if(up.getId() == defaultPaymentId) {
                up.setDefaultPayment(true);
                userPaymentRepository.save(up);
            } else {
                up.setDefaultPayment(false);
                userPaymentRepository.save(up);
            }
        }
    }

    @Override
    public void setDefaultShippingAddress(Long defaultShippingAddressId, User user) {
        List<UserShipping> userShippingList = (List<UserShipping>) userShippingRespository.findAll();
        for(UserShipping userShipping : userShippingList) {
            if(userShipping.getId() == defaultShippingAddressId) {
                userShipping.setUserShippingDefault(true);
                userShippingRespository.save(userShipping);
            } else {
                userShipping.setUserShippingDefault(false);
                userShippingRespository.save(userShipping);
            }
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }
}
