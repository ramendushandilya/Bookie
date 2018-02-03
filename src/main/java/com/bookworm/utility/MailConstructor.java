package com.bookworm.utility;

import com.bookworm.domain.Order;
import com.bookworm.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import java.util.Locale;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by failedOptimus on 15-01-2018.
 */

@Component
public class MailConstructor {

    @Autowired
    private Environment environment;

    @Autowired
    private TemplateEngine templateEngine;

    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user, String password) {
        String url = contextPath + "/newUser?token="+token;
        String message = "\nPlease click on this link to verify your email, and edit your account. Your password is "
                +password;
        SimpleMailMessage  mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("New user registration");
        mail.setText(url + message);
        mail.setFrom(environment.getProperty("support.email"));
        return mail;
    }

    public MimeMessagePreparator constructOrderConfirmationEmail(Order order, User user, Locale locale) {
        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("user", user);
        context.setVariable("cartItemList", order.getCartItemList());
        String text = templateEngine.process("orderConfirmationEmailTemplate", context);

        /*MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo(user.getEmail());
                email.setSubject("orderConfirmation - " +  order.getId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("ramendu9886@gmail.com"));
            }
        };
        return messagePreparator;*/

        return new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo(user.getEmail());
                email.setSubject("orderConfirmation - " +  order.getId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("ramendu9886@gmail.com"));
            }
        };
    }
}
