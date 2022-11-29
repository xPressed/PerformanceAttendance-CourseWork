/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * E-Mail service to send verification messages.
 *
 * @see ru.xpressed.performanceattendancecoursework.controller.RegistrationController
 */
@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String address, String body) throws MessagingException {
        //Create message and helper to build it
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //Create message body in HTML
        String msg = "<div style=\"text-align: center\">\n" +
                "<h2>Spring Table E-Mail Verification</h2>\n" +
                "<h4>Please, follow the next link!</h4>\n" +
                "<a href=\"http://localhost/registration?token=" + body + "\">Click Me!</a>\n" +
                "</div>";

        //Setting message settings with helper
        helper.setText(msg, true);
        helper.setTo(address);
        helper.setSubject("Email Verification");

        javaMailSender.send(message);
    }
}
