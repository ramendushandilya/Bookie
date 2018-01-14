package com.bookworm.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by failedOptimus on 14-01-2018.
 */

@Component
public class SecurityUtility {

    private static final String SALT = "salt"; //Salt should be a complicated string stored somewhere securely

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Bean
    public static String randomPassword() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYS1234567890";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();

        while(salt.length() < 18) {
            int index = (int) (random.nextFloat()*SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
