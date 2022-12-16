/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import ru.xpressed.performanceattendancecoursework.entity.dto.AttendanceDTO;
import ru.xpressed.performanceattendancecoursework.entity.dto.PerformanceDTO;
import ru.xpressed.performanceattendancecoursework.entity.dto.UserDTO;

import java.util.List;

public interface ApiService {
    UserDTO getUserById(String username, String token);

    List<UserDTO> getUsers(String token,
                           String surname,
                           String name,
                           String patronymic,
                           String groupName);

    PerformanceDTO getPerformanceById(Integer id, String token);

    List<PerformanceDTO> getPerformances(String token,
                                         String name,
                                         String mark,
                                         String year,
                                         String username);

    AttendanceDTO getAttendanceById(Integer id, String token);

    List<AttendanceDTO> getAttendances(String token,
                                       String date,
                                       String enterTime,
                                       String exitTime,
                                       String username);
}
