/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.domain.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

/**
 * Requester Service to check for API requesters' permissions.
 *
 * @see UserRepository
 * @see ru.xpressed.performanceattendancecoursework.controller.ApiController
 * @see Role
 */
@Service
public class RequesterService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkRequester(String token) {
        User requester = userRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (!(requester.getRoles().contains(Role.ROLE_TEACHER) || requester.getRoles().contains(Role.ROLE_ADMIN))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
