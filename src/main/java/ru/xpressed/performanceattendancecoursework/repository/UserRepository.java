/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xpressed.performanceattendancecoursework.entity.User;

/**
 * User Repository is a JPA repository to work with database.
 *
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
