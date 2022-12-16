/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import javax.mail.MessagingException;

public interface EmailService {
    void sendMessage(String address, String body) throws MessagingException;
}
