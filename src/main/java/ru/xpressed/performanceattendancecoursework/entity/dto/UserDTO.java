/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.entity.dto;

import lombok.*;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Size(max = 40)
    private String username;

    private Role role;

    @Size(max = 20)
    private String surname;

    @Size(max = 15)
    private String name;

    @Size(max = 20)
    private String patronymic;

    @Size(max = 10)
    private String groupName;
}
