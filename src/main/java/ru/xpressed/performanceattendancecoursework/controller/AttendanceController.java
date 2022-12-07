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
import ru.xpressed.performanceattendancecoursework.entity.Attendance;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.AttendanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller to work with attendance table
 *
 * @see UserRepository
 * @see User
 * @see AttendanceRepository
 * @see Attendance
 * @see Role
 */
@Controller
public class AttendanceController {
    private UserRepository userRepository;

    private AttendanceRepository attendanceRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setDisciplineRepository(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

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
                                 @RequestParam("delete") Optional<Integer> id) {
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

            //Check for update or add page
            if (update.isPresent() || add.isPresent()) {
                model.addAttribute("overflow", "hidden");
                model.addAttribute("blur", "5px");
            } else {
                model.addAttribute("overflow", "visible");
                model.addAttribute("blur", "0");
            }

            //Check and deletion of attendance
            if (id.isPresent()) {
                Attendance attendance = attendanceRepository.findById(id.orElse(null)).orElse(null);
                if (attendance == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                attendance.setUser(null);
                attendanceRepository.save(attendance);

                attendanceRepository.deleteById(id.orElse(null));
            }
        }

        //Table data
        model.addAttribute("rows", Objects.requireNonNull(userRepository.findById(username).orElse(null)).getAttendances());
        return "attendance/main";
    }

    /**
     * Method for GET REQUEST to add attendance record.
     *
     * @param model          to build template
     * @param username       to choose user
     * @return the template or redirect
     */
    @GetMapping("/attendance/add")
    public String getAddAttendanceRecord(Model model, @RequestParam("username") String username) {
        model.addAttribute("attendance", new Attendance());
        return "attendance/add";
    }

    /**
     * Method for POST REQUEST to validate and save new attendance record.
     *
     * @param username       to choose user
     * @param attendance     data to validate and save
     * @param bindingResult  to validate
     * @param model          to return errors
     * @return the redirect or template
     */
    @PostMapping("/attendance/add")
    public String postAddAttendanceRecord(@RequestParam("username") String username,
                                          @Valid Attendance attendance,
                                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("attendance", attendance);
            return "attendance/add";
        }

        User user = userRepository.findById(username).orElse(null);
        assert user != null;
        attendance.setUser(user);

        //Add new attendance record to user
        user.getAttendances().add(attendance);

        userRepository.save(user);
        return "attendance/add";
    }

    /**
     * Method for GET REQUEST to update the attendance record.
     *
     * @param id             to choose the record
     * @param model          to build template
     * @return the redirect or template
     */
    @GetMapping("/attendance/update")
    public String getUpdateAttendanceRecord(@RequestParam("id") Integer id, Model model) {
        Attendance attendance = attendanceRepository.findById(id).orElse(null);
        if (attendance == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("attendance", attendance);
        return "attendance/update";
    }

    /**
     * Method for POST REQUEST to validate and save attendance record.
     *
     * @param id             to choose the record
     * @param newAttendance  new data of record
     * @param bindingResult  to check for errors
     * @param model          to build template and return errors
     * @return the redirect or template
     */
    @PostMapping("/attendance/update")
    public String postUpdateAttendanceRecord(@RequestParam("id") Integer id, @Valid Attendance newAttendance,
                                             BindingResult bindingResult, Model model) {
        //Check for validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("attendance", newAttendance);
            return "attendance/update";
        }

        //Build attendance with new data from new
        Attendance oldAttendance = attendanceRepository.findById(id).orElse(null);
        if (oldAttendance == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        attendanceRepository.save(oldAttendance.toBuilder().date(newAttendance.getDate()).enterTime(newAttendance.getEnterTime()).exitTime(newAttendance.getExitTime()).build());
        return "attendance/update";
    }
}
