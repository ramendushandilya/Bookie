package com.bookworm.service.impl;

import com.bookworm.domain.User;
import com.bookworm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by failedOptimus on 14-01-2018.
 */

@Service
public class UserSecurityService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByName(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(null == user) {
            throw new  UsernameNotFoundException("Username not found");
        }

        return user;
    }
}