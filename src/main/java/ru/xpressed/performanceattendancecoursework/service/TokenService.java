/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

/**
 * Token Service to generate new tokens for users.
 *
 * @see UserRepository
 */
@Service
public class TokenService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateNewToken() {
        boolean isGenerated = false;
        String generated = null;
        while (!isGenerated) {
            generated = RandomString.make(32);
            if (userRepository.findByToken(generated).orElse(null) == null) {
                isGenerated = true;
            }
        }
        return generated;
    }
}
