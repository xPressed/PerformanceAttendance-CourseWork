/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * Entity to store data of attendance records.
 *
 * @see ru.xpressed.performanceattendancecoursework.repository.AttendanceRepository
 * @see ru.xpressed.performanceattendancecoursework.controller.AttendanceController
 * @see User
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @Pattern(message = "Date must be valid! [DD.MM.YYYY]", regexp = "^[0-3][0-9][.][0-1][0-9][.][0-9][0-9][0-9][0-9]")
    private String date;

    @NonNull
    @Pattern(message = "Enter Time must be valid! [HH:MM]", regexp = "^[0-2][0-9]:[0-5][0-9]")
    private String enterTime;

    @NonNull
    @Pattern(message = "Exit Time must be valid! [HH:MM]", regexp = "^[0-2][0-9]:[0-5][0-9]")
    private String exitTime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private User user;
}
