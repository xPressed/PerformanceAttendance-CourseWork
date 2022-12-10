/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.domain.Attendance;
import ru.xpressed.performanceattendancecoursework.domain.Performance;
import ru.xpressed.performanceattendancecoursework.domain.User;
import ru.xpressed.performanceattendancecoursework.domain.dto.AttendanceDTO;
import ru.xpressed.performanceattendancecoursework.domain.dto.ExceptionDTO;
import ru.xpressed.performanceattendancecoursework.domain.dto.PerformanceDTO;
import ru.xpressed.performanceattendancecoursework.domain.dto.UserDTO;
import ru.xpressed.performanceattendancecoursework.repository.AttendanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.PerformanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.RequesterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    private UserRepository userRepository;
    private PerformanceRepository performanceRepository;
    private AttendanceRepository attendanceRepository;
    private RequesterService requesterService;

    @ExceptionHandler(ResponseStatusException.class)
    public ExceptionDTO restError(ResponseStatusException e) {
        return new ExceptionDTO(e.getRawStatusCode(), e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ExceptionDTO paramError(MissingServletRequestParameterException e) {
        return new ExceptionDTO(400, e.getMessage());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPerformanceRepository(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Autowired
    public void setAttendanceRepository(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Autowired
    public void setRequesterService(RequesterService requesterService) {
        this.requesterService = requesterService;
    }

    @Operation(summary = "GET User by ID")
    @ApiResponse(responseCode = "200", description = "API is working",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)))
    @GetMapping("/user")
    public UserDTO getUserById(@RequestParam("username") String username,
                               @RequestParam("token") String token) {
        requesterService.checkRequester(token);
        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new UserDTO(user.getUsername(), user.getRoles().get(0), user.getSurname(), user.getName(), user.getPatronymic(), user.getGroupName());
    }

    @Operation(summary = "GET Users by Params")
    @ApiResponse(responseCode = "200", description = "API is working",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class)))
    @GetMapping("/users")
    public List<UserDTO> getUsers(@RequestParam("token") String token,
                                  @RequestParam("surname") Optional<String> surname,
                                  @RequestParam("name") Optional<String> name,
                                  @RequestParam("patronymic") Optional<String> patronymic,
                                  @RequestParam("groupName") Optional<String> groupName) {
        requesterService.checkRequester(token);

        List<User> userList = userRepository.findByParams(surname.orElse(null), name.orElse(null), patronymic.orElse(null), groupName.orElse(null));
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(new UserDTO(user.getUsername(), user.getRoles().get(0), user.getSurname(), user.getName(), user.getPatronymic(), user.getGroupName()));
        }
        return userDTOList;
    }

    @Operation(summary = "GET Performance by ID")
    @ApiResponse(responseCode = "200", description = "API is working",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerformanceDTO.class)))
    @GetMapping("/performance")
    public PerformanceDTO getPerformanceById(@RequestParam("id") Integer id,
                                             @RequestParam("token") String token) {
        requesterService.checkRequester(token);
        Performance performance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new PerformanceDTO(performance.getName(), performance.getMark(), performance.getYear(), performance.getUser().getUsername());
    }

    @Operation(summary = "GET Performances by Params")
    @ApiResponse(responseCode = "200", description = "API is working",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PerformanceDTO.class)))
    @GetMapping("/performances")
    public List<PerformanceDTO> getPerformances(@RequestParam("token") String token,
                                                @RequestParam("name") Optional<String> name,
                                                @RequestParam("mark") Optional<String> mark,
                                                @RequestParam("year") Optional<String> year,
                                                @RequestParam("username") Optional<String> username) {
        requesterService.checkRequester(token);
        List<Performance> performanceList = performanceRepository.findByParams(name.orElse(null), mark.orElse(null), year.orElse(null), username.orElse(null));
        List<PerformanceDTO> performanceDTOList = new ArrayList<>();
        for (Performance performance : performanceList) {
            performanceDTOList.add(new PerformanceDTO(performance.getName(), performance.getMark(), performance.getYear(), performance.getUser().getUsername()));
        }
        return performanceDTOList;
    }

    @Operation(summary = "GET Attendance by ID")
    @ApiResponse(responseCode = "200", description = "API is working",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttendanceDTO.class)))
    @GetMapping("/attendance")
    public AttendanceDTO getAttendanceById(@RequestParam("id") Integer id,
                                           @RequestParam("token") String token) {
        requesterService.checkRequester(token);
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new AttendanceDTO(attendance.getDate(), attendance.getEnterTime(), attendance.getExitTime(), attendance.getUser().getUsername());
    }

    @Operation(summary = "GET Attendances by Params")
    @ApiResponse(responseCode = "200", description = "API is working",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AttendanceDTO.class)))
    @GetMapping("/attendances")
    public List<AttendanceDTO> getAttendances(@RequestParam("token") String token,
                                              @RequestParam("date") Optional<String> date,
                                              @RequestParam("enterTime") Optional<String> enterTime,
                                              @RequestParam("exitTime") Optional<String> exitTime,
                                              @RequestParam("username") Optional<String> username) {
        requesterService.checkRequester(token);
        List<Attendance> attendanceList = attendanceRepository.findByParams(date.orElse(null), enterTime.orElse(null), exitTime.orElse(null), username.orElse(null));
        List<AttendanceDTO> attendanceDTOList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            attendanceDTOList.add(new AttendanceDTO(attendance.getDate(), attendance.getEnterTime(), attendance.getExitTime(), attendance.getUser().getUsername()));
        }
        return attendanceDTOList;
    }
}
