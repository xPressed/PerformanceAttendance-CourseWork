/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.domain.dto;

import lombok.*;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private Role role;
    private String surname;
    private String name;
    private String patronymic;
    private String groupName;
}
