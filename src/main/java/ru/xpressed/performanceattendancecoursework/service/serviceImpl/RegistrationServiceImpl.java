/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.security.SecurityConfiguration;
import ru.xpressed.performanceattendancecoursework.service.EmailService;
import ru.xpressed.performanceattendancecoursework.service.RegistrationService;
import ru.xpressed.performanceattendancecoursework.service.TokenService;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

/**
 * Registration Service implementation for Registration Controller.
 *
 * @see RegistrationService
 * @see User
 * @see Role
 * @see UserRepository
 * @see SecurityConfiguration
 * @see EmailService
 * @see TokenService
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;
    private final TokenService tokenService;
    private final EmailService emailService;

    /**
     * Logic for Registration Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.RegistrationController#showRegistrationForm(Model, Optional)
     */
    @Override
    public String showRegistration(Model model, String token) {
        if (token != null) {
            User user = userRepository.findByToken(token).orElse(null);
            if (user != null) {
                user.setRoles(List.of(Role.ROLE_STUDENT));
                userRepository.save(user);
            }
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        return "registration";
    }

    /**
     * Logic for Registration Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.RegistrationController#completeRegistration(User, BindingResult, Model)
     */
    @Override
    public String completeRegistration(User user, BindingResult bindingResult, Model model) throws MessagingException {
        boolean hasErrors = false;
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            hasErrors = true;
        }

        //Validate password
        if (user.getPassword().length() < 5) {
            bindingResult.rejectValue("password", "user.password", "Password must be at least 5 symbols!");
            hasErrors = true;
        } else if (user.getPassword().length() > 20) {
            bindingResult.rejectValue("password", "user.password", "Password must be less than 20 symbols!");
            hasErrors = true;
        }

        //Validate password confirmation
        if (user.getRepeatedPassword().isEmpty()) {
            bindingResult.rejectValue("repeatedPassword", "user.repeatedPassword", "Repeated password must not be empty!");
            hasErrors = true;
        } else if (!user.getPassword().equals(user.getRepeatedPassword())) {
            bindingResult.rejectValue("repeatedPassword", "user.repeatedPassword", "Passwords do not match!");
            hasErrors = true;
        }

        if (hasErrors) {
            return "registration";
        }

        //Check for user absence in database or if user did not verify email
        if (userRepository.findById(user.getUsername()).isEmpty() || userRepository.findById(user.getUsername()).get().getRoles().contains(Role.ROLE_DEFAULT)) {
            user.setPassword(securityConfiguration.encoder().encode(user.getPassword()));
            user.setRoles(List.of(Role.ROLE_DEFAULT));

            user.setToken(tokenService.generateNewToken());

            //Send verification message
            emailService.sendMessage(user.getUsername(), user.getToken());

            userRepository.save(user);
        } else {
            bindingResult.rejectValue("username", "user.username", "This username is already taken!");
            return "registration";
        }

        return "redirect:/login";
    }
}
