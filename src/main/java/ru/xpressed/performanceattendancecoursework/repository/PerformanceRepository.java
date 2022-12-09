/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xpressed.performanceattendancecoursework.domain.Performance;

/**
 * Performance Repository is the JPA repository to work with database.
 *
 * @see Performance
 */
@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {
}
