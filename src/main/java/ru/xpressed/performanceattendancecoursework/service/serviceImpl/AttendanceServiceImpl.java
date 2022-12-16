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
import ru.xpressed.performanceattendancecoursework.entity.Attendance;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.AttendanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.AttendanceService;

import java.util.Objects;
import java.util.Optional;

/**
 * Attendance Service implementation for Account Controller logic.
 *
 * @see AttendanceService
 * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController
 * @see User
 * @see Role
 * @see UserRepository
 * @see Attendance
 * @see AttendanceRepository
 */
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /**
     * Logic for Attendance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController#showAttendance(Authentication, Model, String, Optional, Optional, Optional, Optional)
     */
    @Override
    public String showAttendance(Authentication authentication, Model model,
                                 String username,
                                 String add,
                                 String update,
                                 Integer id,
                                 String account) {
        if (!Objects.equals(username, authentication.getName()) && authentication.getAuthorities().contains(Role.ROLE_STUDENT)) {
            return "redirect:/attendance?username=" + authentication.getName();
        }

        //Template build
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

            //Check and deletion of attendance
            if (id != null) {
                Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

                attendance.setUser(null);
                attendanceRepository.save(attendance);

                attendanceRepository.deleteById(id);
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
        model.addAttribute("rows", Objects.requireNonNull(userRepository.findById(username).orElse(null)).getAttendances());
        return "attendance/main";
    }

    /**
     * Logic for Attendance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController#getAddAttendanceRecord(Model, String)
     */
    @Override
    public String getAddAttendanceRecord(Model model, String username) {
        model.addAttribute("attendance", new Attendance());
        return "attendance/add";
    }

    /**
     * Logic for Attendance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController#postAddAttendanceRecord(String, Attendance, BindingResult, Model)
     */
    @Override
    public String postAddAttendanceRecord(String username, Attendance attendance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("attendance", attendance);
            return "attendance/add";
        }

        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        attendance.setUser(user);

        //Add new attendance record to user
        user.getAttendances().add(attendance);

        userRepository.save(user);
        return "attendance/add";
    }

    /**
     * Logic for Attendance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController#getUpdateAttendanceRecord(Integer, Model)
     */
    @Override
    public String getUpdateAttendanceRecord(Integer id, Model model) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        model.addAttribute("attendance", attendance);
        return "attendance/update";
    }

    /**
     * Logic for Attendance Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController#postUpdateAttendanceRecord(Integer, Attendance, BindingResult, Model)
     */
    @Override
    public String postUpdateAttendanceRecord(Integer id, Attendance newAttendance, BindingResult bindingResult, Model model) {
        //Check for validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("attendance", newAttendance);
            return "attendance/update";
        }

        //Build attendance with new data from new
        Attendance oldAttendance = attendanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        attendanceRepository.save(oldAttendance.toBuilder().date(newAttendance.getDate())
                .enterTime(newAttendance.getEnterTime()).exitTime(newAttendance.getExitTime()).build());
        return "attendance/update";
    }
}
