/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.entity.Attendance;
import ru.xpressed.performanceattendancecoursework.entity.Performance;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.entity.dto.AttendanceDTO;
import ru.xpressed.performanceattendancecoursework.entity.dto.PerformanceDTO;
import ru.xpressed.performanceattendancecoursework.entity.dto.UserDTO;
import ru.xpressed.performanceattendancecoursework.repository.AttendanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.PerformanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.ApiService;
import ru.xpressed.performanceattendancecoursework.service.RequesterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * API Service implementation for API Controller logic.
 *
 * @see ApiService
 * @see ru.xpressed.performanceattendancecoursework.controller.ApiController
 * @see RequesterService
 * @see User
 * @see UserDTO
 * @see UserRepository
 * @see Performance
 * @see PerformanceDTO
 * @see PerformanceRepository
 * @see Attendance
 * @see AttendanceDTO
 * @see AttendanceRepository
 */
@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
    private final RequesterService requesterService;
    private final UserRepository userRepository;
    private final PerformanceRepository performanceRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * Logic for API Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.ApiController#getUserById(String, String)
     */
    @Override
    public UserDTO getUserById(String username, String token) {
        requesterService.checkRequester(token);
        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new UserDTO(user.getUsername(), user.getRoles().get(0),
                user.getSurname(), user.getName(), user.getPatronymic(), user.getGroupName());
    }

    /**
     * Logic for API Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.ApiController#getUsers(String, Optional, Optional, Optional, Optional)
     */
    @Override
    public List<UserDTO> getUsers(String token,
                                  String surname,
                                  String name,
                                  String patronymic,
                                  String groupName) {
        requesterService.checkRequester(token);

        List<User> userList = userRepository.findByParams(surname, name, patronymic, groupName);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(new UserDTO(user.getUsername(), user.getRoles().get(0),
                    user.getSurname(), user.getName(), user.getPatronymic(), user.getGroupName()));
        }
        return userDTOList;
    }

    /**
     * Logic for API Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.ApiController#getPerformanceById(Integer, String)
     */
    @Override
    public PerformanceDTO getPerformanceById(Integer id, String token) {
        requesterService.checkRequester(token);
        Performance performance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new PerformanceDTO(performance.getName(), performance.getMark(),
                performance.getYear(), performance.getUser().getUsername());
    }

    /**
     * Logic for API Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.ApiController#getPerformances(String, Optional, Optional, Optional, Optional)
     */
    @Override
    public List<PerformanceDTO> getPerformances(String token,
                                                String name,
                                                String mark,
                                                String year,
                                                String username) {
        requesterService.checkRequester(token);
        List<Performance> performanceList = performanceRepository.findByParams(name, mark, year, username);
        List<PerformanceDTO> performanceDTOList = new ArrayList<>();
        for (Performance performance : performanceList) {
            performanceDTOList.add(new PerformanceDTO(performance.getName(), performance.getMark(),
                    performance.getYear(), performance.getUser().getUsername()));
        }
        return performanceDTOList;
    }

    /**
     * Logic for API Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.ApiController#getAttendanceById(Integer, String)
     */
    @Override
    public AttendanceDTO getAttendanceById(Integer id, String token) {
        requesterService.checkRequester(token);
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return new AttendanceDTO(attendance.getDate(), attendance.getEnterTime(),
                attendance.getExitTime(), attendance.getUser().getUsername());
    }

    /**
     * Logic for API Controller method.
     *
     * @see ru.xpressed.performanceattendancecoursework.controller.ApiController#getAttendances(String, Optional, Optional, Optional, Optional)
     */
    @Override
    public List<AttendanceDTO> getAttendances(String token,
                                              String date,
                                              String enterTime,
                                              String exitTime,
                                              String username) {
        requesterService.checkRequester(token);
        List<Attendance> attendanceList = attendanceRepository.findByParams(date, enterTime, exitTime, username);
        List<AttendanceDTO> attendanceDTOList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            attendanceDTOList.add(new AttendanceDTO(attendance.getDate(), attendance.getEnterTime(),
                    attendance.getExitTime(), attendance.getUser().getUsername()));
        }
        return attendanceDTOList;
    }
}
