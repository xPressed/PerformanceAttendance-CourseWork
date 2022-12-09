/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xpressed.performanceattendancecoursework.domain.Attendance;

/**
 * Attendance Repository is the JPA repository to work with database.
 *
 * @see Attendance
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
}
