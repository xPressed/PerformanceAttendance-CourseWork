/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.domain.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

/**
 * UsersController to work with the users table.
 *
 * @see UserRepository
 * @see User
 * @see Role
 */
@Controller
public class UsersController {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
    public String showUsersPage(Authentication authentication, Model model, @RequestParam("update") Optional<String> update, @RequestParam("view") Optional<String> view, @RequestParam("account") Optional<String> account) {
        //Template building
        model.addAttribute("username", authentication.getName());

        if (update.isPresent() || view.isPresent() || account.isPresent()) {
            model.addAttribute("overflow", "hidden");
            model.addAttribute("blur", "5px");
        } else {
            model.addAttribute("overflow", "visible");
            model.addAttribute("blur", "0");
        }

        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER)) {
            model.addAttribute("delete", false);
        } else {
            model.addAttribute("delete", true);
        }

        //Table data
        model.addAttribute("rows", userRepository.findAll());
        return "users/main";
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
        if (authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("roleUpdate", true);
        } else {
            model.addAttribute("roleUpdate", false);
        }

        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("user", user);
        return "users/update";
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
        if (authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("roleUpdate", true);
        } else {
            model.addAttribute("roleUpdate", false);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", newUser);
            return "users/update";
        }

        //Update old user with the data of new one
        User oldUser = userRepository.findById(username).orElse(null);
        if (oldUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(oldUser.toBuilder().surname(newUser.getSurname()).name(newUser.getName()).patronymic(newUser.getPatronymic()).groupName(newUser.getGroupName()).roles(newUser.getRoles()).build());
        return "users/update";
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
        User user =  userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("user", user);
        return "users/view";
    }

    /**
     * Method for GET REQUEST to delete user.
     *
     * @param username       to choose user
     * @return redirects to users' table
     */
    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("username") String username) {
        userRepository.deleteById(username);
        return "redirect:/users";
    }
}
