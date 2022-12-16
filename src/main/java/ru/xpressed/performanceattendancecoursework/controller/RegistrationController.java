/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.service.RegistrationService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Registration Controller to sign up new users.
 *
 * @see RegistrationService
 * @see User
 */
@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    /**
     * Mapping for GET REQUEST of the Controller is showing registration form.
     *
     * @param model is used to interact with templates by Thymeleaf
     * @param token is used to confirm email by token
     * @return the name of the template or redirects to login page
     */
    @GetMapping("/registration")
    public String showRegistrationForm(Model model, @RequestParam("token") Optional<String> token) {
        return registrationService.showRegistration(model, token.orElse(null));
    }

    /**
     * Mapping for POST REQUEST of the Registration Controller validating and accepting or denying the data from form.
     * Is using UserRepository to save new users.
     *
     * @param user          the entity with data from form
     * @param bindingResult the result of validation
     * @param model         is used to return user data if it has errors
     * @return the name of template or redirects to login page
     * @throws MessagingException to catch email sending exceptions
     */
    @PostMapping("/registration")
    public String completeRegistration(@Valid User user, BindingResult bindingResult, Model model) throws MessagingException {
        return registrationService.completeRegistration(user, bindingResult, model);
    }
}
