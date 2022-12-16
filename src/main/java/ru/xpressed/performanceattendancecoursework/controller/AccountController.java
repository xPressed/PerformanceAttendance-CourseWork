/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.service.AccountService;

import java.util.Optional;

/**
 * Controller to work with account.
 *
 * @see AccountService
 */
@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * Method for GET REQUEST to show account page.
     *
     * @param authentication to get user principal
     * @param model          to build template
     * @param newToken       to check for new token generation request
     * @return page template
     */
    @GetMapping("/account")
    public String showAccount(Authentication authentication, Model model,
                              @RequestParam("new") Optional<String> newToken) {
        return accountService.showAccount(authentication, model, newToken.orElse(null));
    }
}
