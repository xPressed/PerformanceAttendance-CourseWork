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
import ru.xpressed.performanceattendancecoursework.entity.Performance;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.PerformanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.PerformanceService;

import java.util.Objects;
import java.util.Optional;

/**
 * Performance Service implementation for Performance Controller logic.
 *
 * @see PerformanceService
 * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController
 * @see User
 * @see Role
 * @see UserRepository
 * @see Performance
 * @see PerformanceRepository
 */
@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

    /**
     * Logic for Performance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController#redirectIndex(Authentication)
     */
    @Override
    public String redirectIndex(Authentication authentication) {
        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            return "redirect:/users";
        }
        return "redirect:/performance?username=" + authentication.getName();
    }

    /**
     * Logic for Performance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController#showPerformanceTable(Authentication, Model, String, Optional, Optional, Optional, Optional)
     */
    @Override
    public String showPerformanceTable(Authentication authentication, Model model,
                                       String username,
                                       String add,
                                       String update,
                                       Integer id,
                                       String account) {
        if (!Objects.equals(username, authentication.getName()) && authentication.getAuthorities().contains(Role.ROLE_STUDENT)) {
            return "redirect:/performance?username=" + authentication.getName();
        }

        //Build template
        model.addAttribute("username", authentication.getName());

        if (authentication.getAuthorities().contains(Role.ROLE_STUDENT)) {
            model.addAttribute("navHref", "/performance?username=" + authentication.getName());
            model.addAttribute("navText", "Tables");
            model.addAttribute("actions", false);
            model.addAttribute("owner", authentication.getName());
        } else {
            model.addAttribute("navHref", "/users");
            model.addAttribute("navText", "Users");
            model.addAttribute("actions", true);
            model.addAttribute("owner", username);

            //Check for deletion
            if (id != null) {
                //Delete foreign key from discipline
                Performance performance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

                performance.setUser(null);
                performanceRepository.save(performance);

                //Delete discipline
                performanceRepository.deleteById(id);
            }
        }

        //Check for overflow page
        if (update != null || add != null || account != null) {
            model.addAttribute("overflow", "hidden");
            model.addAttribute("blur", "5px");
        } else {
            model.addAttribute("overflow", "visible");
            model.addAttribute("blur", "0");
        }

        //Table data
        model.addAttribute("rows", Objects.requireNonNull(userRepository.findById(username).orElse(null)).getPerformances());
        return "performance/main";
    }

    /**
     * Logic for Performance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController#getAddPerformanceRecord(Model, String)
     */
    @Override
    public String getAddPerformanceRecord(Model model, String username) {
        model.addAttribute("performance", new Performance());
        return "performance/add";
    }

    /**
     * Logic for Performance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController#postAddPerformanceRecord(String, Performance, BindingResult, Model)
     */
    @Override
    public String postAddPerformanceRecord(String username, Performance performance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("performance", performance);
            return "performance/add";
        }

        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        performance.setUser(user);
        user.getPerformances().add(performance);
        userRepository.save(user);
        return "performance/add";
    }

    /**
     * Logic for Performance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController#getUpdatePerformanceRecord(Integer, Model)
     */
    @Override
    public String getUpdatePerformanceRecord(Integer id, Model model) {
        Performance performance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        model.addAttribute("performance", performance);
        return "performance/update";
    }

    /**
     * Logic for Performance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController#postUpdatePerformanceRecord(Integer, Performance, BindingResult, Model)
     */
    @Override
    public String postUpdatePerformanceRecord(Integer id, Performance newPerformance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("performance", newPerformance);
            return "performance/update";
        }

        Performance oldPerformance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        performanceRepository.save(oldPerformance.toBuilder().name(newPerformance.getName())
                .mark(newPerformance.getMark()).year(newPerformance.getYear()).build());
        return "performance/update";
    }
}
