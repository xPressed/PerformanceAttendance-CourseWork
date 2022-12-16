/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.xpressed.performanceattendancecoursework.entity.Performance;

import javax.validation.Valid;

public interface PerformanceService {
    String redirectIndex(Authentication authentication);

    String showPerformanceTable(Authentication authentication, Model model,
                                String username,
                                String add,
                                String update,
                                Integer id,
                                String account);

    String getAddPerformanceRecord(Model model, String username);

    String postAddPerformanceRecord(String username, @Valid Performance performance,
                                    BindingResult bindingResult, Model model);

    String getUpdatePerformanceRecord(Integer id, Model model);

    String postUpdatePerformanceRecord(Integer id, @Valid Performance newPerformance,
                                       BindingResult bindingResult, Model model);
}
