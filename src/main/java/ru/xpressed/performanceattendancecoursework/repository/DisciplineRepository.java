/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.xpressed.performanceattendancecoursework.entity.Discipline;

/**
 * Discipline Repository is the JPA repository to work with database.
 *
 * @see Discipline
 */
@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {
}
