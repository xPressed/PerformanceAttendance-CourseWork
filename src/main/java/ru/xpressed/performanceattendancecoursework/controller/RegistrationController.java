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
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.security.SecurityConfiguration;

import javax.validation.Valid;
import java.util.List;

/**
 * Registration Controller to sign up new users.
 *
 * @see UserRepository
 * @see SecurityConfiguration#encoder()
 */
@Controller
public class RegistrationController {
    private UserRepository userRepository;

    private SecurityConfiguration securityConfiguration;

    @Autowired
    public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Mapping for GET REQUEST of the Controller is showing registration form.
     *
     * @param model is used to interact with templates by Thymeleaf
     * @return the name of the template
     */
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
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
     * @return the name of template
     */
    @PostMapping("/registration")
    public String completeRegistration(@Valid User user, BindingResult bindingResult, Model model) {
        boolean flag = false;
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            flag = true;
        }

        if (user.getPassword().length() < 5) {
            bindingResult.rejectValue("password", "user.password", "Password must be at least 5 symbols!");
            flag = true;
        } else if (user.getPassword().length() > 20) {
            bindingResult.rejectValue("password", "user.password", "Password must be less than 20 symbols!");
            flag = true;
        }

        if (user.getRepeatedPassword().isEmpty()) {
            bindingResult.rejectValue("repeatedPassword", "user.repeatedPassword", "Repeated password must not be empty!");
            flag = true;
        } else if (!user.getPassword().equals(user.getRepeatedPassword())) {
            bindingResult.rejectValue("repeatedPassword", "user.repeatedPassword", "Passwords do not match!");
            flag = true;
        }

        if (flag) {
            return "registration";
        }

        if (userRepository.findById(user.getUsername()).isEmpty()) {
            user.setPassword(securityConfiguration.encoder().encode(user.getPassword()));
            user.setRoles(List.of(Role.ROLE_DEFAULT));
            userRepository.save(user);
        } else {
            bindingResult.rejectValue("username", "user.username", "This username is already taken!");
        }

        return "registration";
    }
}
