/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.domain.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    @Pattern(regexp = "DD.MM.YYYY")
    private String date;

    @Pattern(regexp = "HH:MM")
    private String enterTime;

    @Pattern(regexp = "HH:MM")
    private String exitTime;

    @Size(max = 40)
    private String username;
}
