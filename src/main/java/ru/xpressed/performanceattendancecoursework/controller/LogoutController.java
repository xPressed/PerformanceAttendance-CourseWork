/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logout Controller to forget authorized users.
 *
 * @see ru.xpressed.performanceattendancecoursework.security.SecurityConfiguration#filterChain(HttpSecurity)
 */
@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String executeLogout(HttpServletRequest request, Authentication authentication, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/";
    }
}
