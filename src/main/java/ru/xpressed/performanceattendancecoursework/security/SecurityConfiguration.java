/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security Configuration class with some settings and password encoder.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration", "/error", "/icons/**", "/logout").permitAll()

                .antMatchers("/users").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/users/**").hasAnyRole("TEACHER", "ADMIN")
                .antMatchers("/users/delete").hasRole("ADMIN")

                .antMatchers("/performance").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                .antMatchers("/performance/**").hasAnyRole("TEACHER", "ADMIN")

                .antMatchers("/attendance").hasAnyRole("STUDENT", "TEACHER", "ADMIN")
                .antMatchers("/attendance/**").hasAnyRole("TEACHER", "ADMIN")

                .antMatchers("/api/**").permitAll()

                .anyRequest().hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/perform-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID").permitAll()

                .and()
                .headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
