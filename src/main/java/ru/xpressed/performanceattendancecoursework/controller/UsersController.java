/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UsersController {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String showUsersPage(Authentication authentication, Model model,
                                @RequestParam("update") Optional<String> update) {
        model.addAttribute("username", authentication.getName());

        if (update.isPresent()) {
            model.addAttribute("overflow", "hidden");
            model.addAttribute("blur", "5px");
        } else {
            model.addAttribute("overflow", "visible");
            model.addAttribute("blur", "0");
        }

        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER)) {
            model.addAttribute("update", true);
            model.addAttribute("delete", false);
        } else {
            model.addAttribute("update", true);
            model.addAttribute("delete", true);
        }

        model.addAttribute("rows", userRepository.findAll());
        return "users/main";
    }

    @GetMapping("/users/update")
    public String getUpdateUser(@RequestParam("username") String username, Authentication authentication, Model model) {
        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("user", userRepository.findById(username));
            return "users/update";
        }
        return "redirect:/users";
    }

    @PostMapping("/users/update")
    public String postUpdateUser(@RequestParam("username") String username, @Valid User newUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", newUser);
            return "users/update";
        }
        User oldUser = userRepository.findById(username).orElse(null);
        assert oldUser != null;
        userRepository.save(oldUser.toBuilder().surname(newUser.getSurname()).name(newUser.getName()).patronymic(newUser.getPatronymic()).groupName(newUser.getGroupName()).build());
        return "users/update";
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam("username") String username, Authentication authentication) {
        if (authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            userRepository.deleteById(username);
        }
        return "redirect:/users";
    }
}
