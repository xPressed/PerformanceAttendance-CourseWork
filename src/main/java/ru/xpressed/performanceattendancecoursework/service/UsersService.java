/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.xpressed.performanceattendancecoursework.entity.User;

import javax.validation.Valid;

public interface UsersService {
    String showUsersPage(Authentication authentication, Model model, String update, String view, String account);

    String getUpdateUser(String username, Authentication authentication, Model model);

    String postUpdateUser(String username, @Valid User newUser,
                          BindingResult bindingResult, Model model, Authentication authentication);

    String getViewUser(String username, Model model);

    String deleteUser(String username);
}
