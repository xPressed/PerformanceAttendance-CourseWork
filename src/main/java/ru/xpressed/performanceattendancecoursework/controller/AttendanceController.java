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
import ru.xpressed.performanceattendancecoursework.entity.Attendance;
import ru.xpressed.performanceattendancecoursework.service.AttendanceService;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller to work with attendance table
 *
 * @see Attendance
 * @see AttendanceService
 */
@Controller
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    /**
     * Method for GET REQUEST to show the attendance table.
     *
     * @param authentication to check for permission
     * @param model          to build the template
     * @param username       to check if current user is the table owner
     * @param add            to check if add page is shown
     * @param update         to check if update page is shown
     * @param id             to check for deletion
     * @return the template or redirect
     */
    @GetMapping("/attendance")
    public String showAttendance(Authentication authentication, Model model,
                                 @RequestParam("username") String username,
                                 @RequestParam("add") Optional<String> add,
                                 @RequestParam("update") Optional<String> update,
                                 @RequestParam("delete") Optional<Integer> id,
                                 @RequestParam("account") Optional<String> account) {
        return attendanceService.showAttendance(authentication, model, username, add.orElse(null),
                update.orElse(null), id.orElse(null), account.orElse(null));
    }

    /**
     * Method for GET REQUEST to add attendance record.
     *
     * @param model    to build template
     * @param username to choose user
     * @return the template or redirect
     */
    @GetMapping("/attendance/add")
    public String getAddAttendanceRecord(Model model, @RequestParam("username") String username) {
        return attendanceService.getAddAttendanceRecord(model, username);
    }

    /**
     * Method for POST REQUEST to validate and save new attendance record.
     *
     * @param username      to choose user
     * @param attendance    data to validate and save
     * @param bindingResult to validate
     * @param model         to return errors
     * @return the redirect or template
     */
    @PostMapping("/attendance/add")
    public String postAddAttendanceRecord(@RequestParam("username") String username,
                                          @Valid Attendance attendance,
                                          BindingResult bindingResult, Model model) {
        return attendanceService.postAddAttendanceRecord(username, attendance, bindingResult, model);
    }

    /**
     * Method for GET REQUEST to update the attendance record.
     *
     * @param id    to choose the record
     * @param model to build template
     * @return the redirect or template
     */
    @GetMapping("/attendance/update")
    public String getUpdateAttendanceRecord(@RequestParam("id") Integer id, Model model) {
        return attendanceService.getUpdateAttendanceRecord(id, model);
    }

    /**
     * Method for POST REQUEST to validate and save attendance record.
     *
     * @param id            to choose the record
     * @param newAttendance new data of record
     * @param bindingResult to check for errors
     * @param model         to build template and return errors
     * @return the redirect or template
     */
    @PostMapping("/attendance/update")
    public String postUpdateAttendanceRecord(@RequestParam("id") Integer id, @Valid Attendance newAttendance,
                                             BindingResult bindingResult, Model model) {
        return attendanceService.postUpdateAttendanceRecord(id, newAttendance, bindingResult, model);
    }
}
