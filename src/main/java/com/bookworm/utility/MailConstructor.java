package com.bookworm.utility;

import com.bookworm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by failedOptimus on 15-01-2018.
 */

@Component
public class MailConstructor {

    @Autowired
    private Environment environment;

    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user, String password) {
        String url = contextPath = "/newUser?token"+token;
        String message = "\nPlease click on this link to verify your email, and edit your account. Your password is "
                +password;
        SimpleMailMessage  mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("New user registration");
        mail.setText(url + message);
        mail.setFrom(environment.getProperty("support.email"));
        return mail;
    }
}
