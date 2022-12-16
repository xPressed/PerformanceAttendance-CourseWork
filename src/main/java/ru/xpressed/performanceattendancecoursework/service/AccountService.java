/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public interface AccountService {
    String showAccount(Authentication authentication, Model model, String newToken);
}
