/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.xpressed.performanceattendancecoursework.service.ErrorService;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller is showing the Error Page from templates with specific HTTP request status code transferred to HTML by Thymeleaf.
 *
 * @see ErrorService
 */
@Controller
@RequiredArgsConstructor
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private final ErrorService errorService;

    /**
     * Request Mapping to show Error Page.
     *
     * @param request is used to extract the HTTP request status code
     * @param model   is used to interact with template model
     * @return the name of the template
     */
    @RequestMapping("/error")
    public String showErrorPage(HttpServletRequest request, Model model) {
        return errorService.showErrorPage(request, model);
    }
}
