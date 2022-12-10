/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "PerformanceAttendance-CourseWork", contact = @Contact(name = "xPressed", url = "https://github.com/xPressed/PerformanceAttendance-CourseWork")))
public class PerformanceAttendanceCourseWorkApplication {
    /**
     * Spring Boot Application main class that is used to start the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(PerformanceAttendanceCourseWorkApplication.class, args);
    }

}
