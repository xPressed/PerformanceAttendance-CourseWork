/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    /**
     * Controller is showing the authorization page with login form
     *
     * @return the name of the template
     * @see ru.xpressed.performanceattendancecoursework.security.SecurityConfiguration#filterChain(HttpSecurity)  SecurityConfiguration for settings
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
