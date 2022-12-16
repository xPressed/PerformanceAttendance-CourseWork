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
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.RequesterService;

/**
 * Requester Service implementation for API Controller.
 *
 * @see RequesterService
 * @see User
 * @see Role
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class RequesterServiceImpl implements RequesterService {
    private final UserRepository userRepository;

    /**
     * Check access for user by token.
     *
     * @param token to find user
     */
    public void checkRequester(String token) {
        User requester = userRepository.findByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        if (!(requester.getRoles().contains(Role.ROLE_TEACHER) || requester.getRoles().contains(Role.ROLE_ADMIN))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
