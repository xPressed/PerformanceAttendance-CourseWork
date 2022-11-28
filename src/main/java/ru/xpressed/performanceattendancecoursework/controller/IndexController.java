/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping({"/index", "/", "/home"})
    public String showIndexPage(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("linkOutOrUp", "/logout");
            model.addAttribute("textOutOrUp", "Log Out");
            model.addAttribute("linkInOrAccount", "/account/" + authentication.getName());
            model.addAttribute("textInOrAccount", "Account");
        } else {
            model.addAttribute("username", "Stranger?");
            model.addAttribute("linkOutOrUp", "/index?registration");
            model.addAttribute("textOutOrUp", "Sign Up");
            model.addAttribute("linkInOrAccount", "/index?login");
            model.addAttribute("textInOrAccount", "Log In");
        }

        return "index";
    }
}
