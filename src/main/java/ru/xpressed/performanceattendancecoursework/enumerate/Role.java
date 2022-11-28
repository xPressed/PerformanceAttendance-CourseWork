/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.enumerate;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enumerated list of Roles for Users.
 * @see ru.xpressed.performanceattendancecoursework.entity.User
 */
public enum Role implements GrantedAuthority {
    ROLE_DEFAULT, ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
