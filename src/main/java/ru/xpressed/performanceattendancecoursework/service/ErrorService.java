/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

public interface ErrorService {
    String showErrorPage(HttpServletRequest request, Model model);
}
