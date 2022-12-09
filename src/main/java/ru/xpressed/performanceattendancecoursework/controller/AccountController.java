/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.TokenService;

import java.util.Optional;

/**
 * Controller to work with account.
 *
 * @see User
 * @see UserRepository
 * @see TokenService
 */
@Controller
public class AccountController {
    private UserRepository userRepository;

    private TokenService tokenService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Method for GET REQUEST to show account page.
     *
     * @param authentication to get user principal
     * @param model to build template
     * @param newToken to check for new token generation request
     * @return page template
     */
    @GetMapping("/account")
    public String showAccount(Authentication authentication, Model model, @RequestParam("new") Optional<String> newToken) {
        User user = (User) authentication.getPrincipal();

        if (newToken.isPresent()) {
            user.setToken(tokenService.generateNewToken());
            userRepository.save(user);
        }

        model.addAttribute("token", user.getToken());
        model.addAttribute("owner", user.getUsername());
        return "account";
    }
}
