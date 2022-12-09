/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.domain;

import lombok.*;
import ru.xpressed.performanceattendancecoursework.repository.PerformanceRepository;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Entity to store data of performance records.
 *
 * @see PerformanceRepository
 * @see ru.xpressed.performanceattendancecoursework.controller.PerformanceController
 * @see User
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @Size(min = 1, message = "Discipline Name must not be empty!")
    @Size(max = 75, message = "Discipline Name must be less than 75 symbols!")
    private String name;

    @NonNull
    @NotEmpty(message = "Mark must not be empty!")
    private String mark;

    @NonNull
    @Size(min = 1, message = "Year must not be empty!")
    @Size(max = 4, message = "Year must be less than 4 symbols!")
    @Pattern(regexp = "^[0-9]*", message = "Year must contain only numbers!")
    private String year;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private User user;
}
