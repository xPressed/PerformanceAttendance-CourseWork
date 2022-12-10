/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.xpressed.performanceattendancecoursework.entity.Performance;

import java.util.List;

/**
 * Performance Repository is the JPA repository to work with database.
 *
 * @see Performance
 */
@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {
    @Query(value = "SELECT * FROM public.performance WHERE (:name is null or name = :name) AND" +
            "(:mark is null or mark = :mark) AND" +
            "(:year is null or year = :year) AND" +
            "(:username is null or user_username = :username)", nativeQuery = true)
    List<Performance> findByParams(@Param("name") String name, @Param("mark") String mark, @Param("year") String year, @Param("username") String username);
}
