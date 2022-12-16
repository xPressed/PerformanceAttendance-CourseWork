/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.xpressed.performanceattendancecoursework.entity.Attendance;

import javax.validation.Valid;

public interface AttendanceService {
    String showAttendance(Authentication authentication, Model model,
                          String username,
                          String add,
                          String update,
                          Integer id,
                          String account);

    String getAddAttendanceRecord(Model model, String username);

    String postAddAttendanceRecord(String username, @Valid Attendance attendance,
                                   BindingResult bindingResult, Model model);

    String getUpdateAttendanceRecord(Integer id, Model model);

    String postUpdateAttendanceRecord(Integer id, @Valid Attendance newAttendance,
                                      BindingResult bindingResult, Model model);
}
