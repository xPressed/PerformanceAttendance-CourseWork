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
import ru.xpressed.performanceattendancecoursework.entity.Performance;
import ru.xpressed.performanceattendancecoursework.service.PerformanceService;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller to work with performance table.
 *
 * @see PerformanceService
 * @see Performance
 */
@Controller
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;

    /**
     * !!! INDEX MAPPING !!!
     * Method for GET REQUEST always redirects to users (teacher/admin) or performance (student) table.
     *
     * @param authentication to check for permission to view others' tables
     * @return redirect to other page
     */
    @GetMapping({"/index", "/", "/home"})
    public String redirectIndex(Authentication authentication) {
        return performanceService.redirectIndex(authentication);
    }

    /**
     * Method for GET REQUEST to show performance table.
     *
     * @param authentication to check for permission
     * @param model          to build template
     * @param username       to choose user (student -> himself)
     * @param add            to check for add page
     * @param update         to check for update page
     * @param id             for deletion
     * @return the redirect or template
     */
    @GetMapping("/performance")
    public String showPerformanceTable(Authentication authentication, Model model,
                                       @RequestParam("username") String username,
                                       @RequestParam("add") Optional<String> add,
                                       @RequestParam("update") Optional<String> update,
                                       @RequestParam("delete") Optional<Integer> id,
                                       @RequestParam("account") Optional<String> account) {
        return performanceService.showPerformanceTable(authentication, model, username,
                add.orElse(null), update.orElse(null), id.orElse(null), account.orElse(null));
    }

    /**
     * Method for GET REQUEST to add performance record.
     *
     * @param model    to build template
     * @param username to choose user
     * @return the template or redirect
     */
    @GetMapping("/performance/add")
    public String getAddPerformanceRecord(Model model, @RequestParam("username") String username) {
        return performanceService.getAddPerformanceRecord(model, username);
    }

    /**
     * Method for POST REQUEST to validate and save new performance record.
     *
     * @param username      to choose user
     * @param performance   data to validate and save
     * @param bindingResult to validate
     * @param model         to return errors
     * @return the template or redirect
     */
    @PostMapping("/performance/add")
    public String postAddPerformanceRecord(@RequestParam("username") String username, @Valid Performance performance,
                                           BindingResult bindingResult, Model model) {
        return performanceService.postAddPerformanceRecord(username, performance, bindingResult, model);
    }

    /**
     * Method for GET REQUEST to update the performance record.
     *
     * @param id    to choose the record
     * @param model to build template
     * @return the template or redirect
     */
    @GetMapping("/performance/update")
    public String getUpdatePerformanceRecord(@RequestParam("id") Integer id, Model model) {
        return performanceService.getUpdatePerformanceRecord(id, model);
    }

    /**
     * Method for POST REQUEST to validate and save updated performance record.
     *
     * @param id             to choose the record
     * @param newPerformance new updated data
     * @param bindingResult  to validate
     * @param model          to return errors
     * @return the template or redirect
     */
    @PostMapping("/performance/update")
    public String postUpdatePerformanceRecord(@RequestParam("id") Integer id, @Valid Performance newPerformance,
                                              BindingResult bindingResult, Model model) {
        return performanceService.postUpdatePerformanceRecord(id, newPerformance, bindingResult, model);
    }
}
