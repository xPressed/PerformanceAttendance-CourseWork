/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.AccountService;
import ru.xpressed.performanceattendancecoursework.service.TokenService;

import java.util.Optional;

/**
 * Account Service implementation for Account Controller logic.
 *
 * @see AccountService
 * @see ru.xpressed.performanceattendancecoursework.controller.AccountController
 * @see User
 * @see UserRepository
 * @see TokenService
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    /**
     * Logic for Account Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.AccountController#showAccount(Authentication, Model, Optional)
     */
    @Override
    public String showAccount(Authentication authentication, Model model, String newToken) {
        User user = (User) authentication.getPrincipal();

        if (newToken != null) {
            user.setToken(tokenService.generateNewToken());
            userRepository.save(user);
        }

        model.addAttribute("token", user.getToken());
        model.addAttribute("owner", user.getUsername());
        return "account";
    }
}
