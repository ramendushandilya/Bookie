package com.bookworm.service.impl;

import com.bookworm.domain.User;
import com.bookworm.domain.security.PasswordResetToken;
import com.bookworm.domain.security.UserRole;
import com.bookworm.repository.PasswordResetTokenRepository;
import com.bookworm.repository.RoleRepository;
import com.bookworm.repository.UserRepository;
import com.bookworm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by failedOptimus on 15-01-2018.
 */

@Service
public class UserServiceImpl implements UserService{

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
            throw new Exception("User already exits");
        } else {
            for(UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            localUser = userRepository.save(user);
        }

        return localUser;
    }


}
