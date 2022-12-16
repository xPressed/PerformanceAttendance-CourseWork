/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.xpressed.performanceattendancecoursework.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Email Service implementation for Email logic.
 *
 * @see EmailService
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

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
