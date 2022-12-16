/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.TokenService;

/**
 * Token Service implementation to work with tokens.
 *
 * @see TokenService
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserRepository userRepository;

    /**
     * Method to generate new 32 symbol token and check if it is taken.
     *
     * @return generated token
     */
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
