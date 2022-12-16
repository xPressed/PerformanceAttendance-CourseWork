/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.xpressed.performanceattendancecoursework.service.ErrorService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Service
public class ErrorServiceImpl implements ErrorService {
    @Override
    public String showErrorPage(HttpServletRequest request, Model model) {
        Object obj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        //Check if request was forced and no error caught
        if (obj != null) {
            //Custom error page build for 403 error
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
