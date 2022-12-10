/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.domain.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDTO {
    @Size(max = 75)
    private String name;

    @Size(max = 1)
    private String mark;

    @Size(max = 4)
    private String year;

    @Size(max = 40)
    private String username;
}
