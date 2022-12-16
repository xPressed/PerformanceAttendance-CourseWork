/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.xpressed.performanceattendancecoursework.entity.User;

import javax.mail.MessagingException;
import javax.validation.Valid;

public interface RegistrationService {
    String showRegistration(Model model, String token);

    String completeRegistration(@Valid User user, BindingResult bindingResult, Model model) throws MessagingException;
}
