/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.security.SecurityConfiguration;
import ru.xpressed.performanceattendancecoursework.service.EmailService;
import ru.xpressed.performanceattendancecoursework.service.TokenService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Registration Controller to sign up new users.
 *
 * @see UserRepository
 * @see User
 * @see Role
 * @see SecurityConfiguration#encoder()
 * @see EmailService
 * @see TokenService
 */
@Controller
public class RegistrationController {
    private UserRepository userRepository;

    private SecurityConfiguration securityConfiguration;

    private EmailService emailService;

    private TokenService tokenService;

    @Autowired
    public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Mapping for GET REQUEST of the Controller is showing registration form.
     *
     * @param model is used to interact with templates by Thymeleaf
     * @param token is used to confirm email by token
     * @return the name of the template or redirects to login page
     */
    @GetMapping("/registration")
    public String showRegistrationForm(Model model, @RequestParam("token") Optional<String> token) {
        //Check for token and update user if token found in database
        if (token.isPresent()) {
            User user = userRepository.findByToken(token.orElse(null));
            user.setRoles(List.of(Role.ROLE_STUDENT));
            userRepository.save(user);
            return "redirect:/login";
        }
        model.addAttribute("user", new User());
        return "registration";
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
