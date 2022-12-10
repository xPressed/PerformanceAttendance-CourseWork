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

/**
 * Rest Controller to provide API to permitted users.
 *
 * @see UserRepository
 * @see User
 * @see UserDTO
 *
 * @see PerformanceRepository
 * @see Performance
 * @see PerformanceDTO
 *
 * @see AttendanceRepository
 * @see Attendance
 * @see AttendanceDTO
 *
 * @see RequesterService
 * @see ExceptionDTO
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private UserRepository userRepository;
    private PerformanceRepository performanceRepository;
    private AttendanceRepository attendanceRepository;
    private RequesterService requesterService;

    /**
     * Exception Handler to handle Response Status Exceptions.
     *
     * @param e Response Status Exception
     * @return ExceptionDTO with code and reason
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ExceptionDTO restError(ResponseStatusException e) {
        return new ExceptionDTO(e.getRawStatusCode(), e.getMessage());
    }

    /**
     * Exception Handler to handle Missing Request Parameter Exceptions.
     *
     * @param e Missing Request Parameter Exception
     * @return ExceptionDTO with code and reason
     */
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

    /**
     * Method for REST GET REQUEST to get JSON formatted User data.
     *
     * @param username is the id of user
     * @param token to check for permission
     * @return UserDTO
     */
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

    /**
     * Method for REST GET REQUEST to get JSON formatted list of User data.
     *
     * @param token to check for permission
     * @param surname optional param
     * @param name optional param
     * @param patronymic optional param
     * @param groupName optional param
     * @return list of UserDTO
     */
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

    /**
     * Method for REST GET REQUEST to get JSON formatted Performance data.
     *
     * @param id of performance record
     * @param token to check for permission
     * @return PerformanceDTO
     */
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

    /**
     * Method for REST GET REQUEST to get JSON formatted list of Performance data.
     *
     * @param token to check for permission
     * @param name optional param
     * @param mark optional param
     * @param year optional param
     * @param username optional param
     * @return list of PerformanceDTO
     */
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

    /**
     * Method for REST GET REQUEST to get JSON formatted Attendance data.
     *
     * @param id of attendance record
     * @param token to check for permission
     * @return AttendanceDTO
     */
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

    /**
     * Method for REST GET REQUEST to get JSON formatted list of Attendance data.
     *
     * @param token to check for permission
     * @param date optional param
     * @param enterTime optional param
     * @param exitTime optional param
     * @param username optional param
     * @return list of AttendanceDTO
     */
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
