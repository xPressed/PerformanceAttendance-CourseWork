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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.service.UsersService;

import javax.validation.Valid;
import java.util.Optional;

/**
 * UsersController to work with the users table.
 *
 * @see UsersService
 * @see User
 */
@Controller
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    /**
     * Mapping for GET REQUEST to show users table and apply overflow and blur on view or update.
     *
     * @param authentication to get current autenticated user's principal
     * @param model          to interact with templates by Thymeleaf
     * @param update         to check if update page is opened
     * @param view           to check if view page is opened
     * @return the template of page
     */
    @GetMapping("/users")
    public String showUsersPage(Authentication authentication, Model model,
                                @RequestParam("update") Optional<String> update,
                                @RequestParam("view") Optional<String> view,
                                @RequestParam("account") Optional<String> account) {
        return usersService.showUsersPage(authentication, model,
                update.orElse(null), view.orElse(null), account.orElse(null));
    }

    /**
     * Mapping for GET REQUEST to update user's data.
     *
     * @param username       to choose the user
     * @param authentication to check for admin role
     * @param model          to show update buttons for admins only
     * @return the template of page
     */
    @GetMapping("/users/update")
    public String getUpdateUser(@RequestParam("username") String username, Authentication authentication, Model model) {
        return usersService.getUpdateUser(username, authentication, model);
    }

    /**
     * Method for POST REQUEST to update user's data.
     *
     * @param username       to choose the user
     * @param newUser        to get new data
     * @param bindingResult  to validate new data
     * @param model          to return errors
     * @param authentication to check for admin role
     * @return the template page
     */
    @PostMapping("/users/update")
    public String postUpdateUser(@RequestParam("username") String username, @Valid User newUser,
                                 BindingResult bindingResult, Model model, Authentication authentication) {
        return usersService.postUpdateUser(username, newUser, bindingResult, model, authentication);
    }

    /**
     * Method for GET REQUEST to check full user's data.
     *
     * @param username to choose user
     * @param model    to show user's full data
     * @return template of page
     */
    @GetMapping("/users/view")
    public String getViewUser(@RequestParam("username") String username, Model model) {
        return usersService.getViewUser(username, model);
    }

    /**
     * Method for GET REQUEST to delete user.
     *
     * @param username to choose user
     * @return redirects to users' table
     */
    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("username") String username) {
        return usersService.deleteUser(username);
    }
}
