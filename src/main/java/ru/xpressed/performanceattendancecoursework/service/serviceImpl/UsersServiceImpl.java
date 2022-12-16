/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.UsersService;

import java.util.Optional;

/**
 * Users Service implementation for Users Controller.
 *
 * @see UsersService
 * @see User
 * @see Role
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;

    /**
     * Logic for Users Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.UsersController#showUsersPage(Authentication, Model, Optional, Optional, Optional)
     */
    @Override
    public String showUsersPage(Authentication authentication, Model model,
                                String update,
                                String view,
                                String account) {
        //Template building
        model.addAttribute("username", authentication.getName());

        if (update != null || view != null || account != null) {
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
     * Logic for Users Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.UsersController#getUpdateUser(String, Authentication, Model)
     */
    @Override
    public String getUpdateUser(String username, Authentication authentication, Model model) {
        if (authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("roleUpdate", true);
        } else {
            model.addAttribute("roleUpdate", false);
        }

        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        model.addAttribute("user", user);
        return "users/update";
    }

    /**
     * Logic for Users Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.UsersController#postUpdateUser(String, User, BindingResult, Model, Authentication)
     */
    @Override
    public String postUpdateUser(String username, User newUser,
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
        User oldUser = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        userRepository.save(oldUser.toBuilder().surname(newUser.getSurname()).name(newUser.getName())
                .patronymic(newUser.getPatronymic()).groupName(newUser.getGroupName()).roles(newUser.getRoles()).build());
        return "users/update";
    }

    /**
     * Logic for Users Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.UsersController#getViewUser(String, Model)
     */
    @Override
    public String getViewUser(String username, Model model) {
        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        model.addAttribute("user", user);
        return "users/view";
    }

    /**
     * Logic for Users Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.UsersController#deleteUser(String)
     */
    @Override
    public String deleteUser(String username) {
        userRepository.deleteById(username);
        return "redirect:/users";
    }
}
