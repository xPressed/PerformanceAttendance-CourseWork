/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    /**
     * Controller is showing the Error Page from templates with specific HTTP request status code transferred to HTML by Thymeleaf.
     *
     * @param request is used to extract the HTTP request status code
     * @param model   is used to interact with template model
     * @return the name of the template
     */
    @RequestMapping("/error")
    @ExceptionHandler(Exception.class)
    public String showErrorPage(HttpServletRequest request, Model model) {
        Object obj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (obj != null) {
            if (obj.equals(403)) {
                model.addAttribute("code", "Status code: " + obj + " Access denied!");
            } else {
                model.addAttribute("code", "Status code: " + obj);
            }
        } else {
            model.addAttribute("code", "Status code: Forced call!");
        }

        return "error";
    }
}
