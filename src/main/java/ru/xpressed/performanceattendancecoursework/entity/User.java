/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

/**
 * Entity to store user data.
 *
 * @see ru.xpressed.performanceattendancecoursework.repository.UserRepository
 * @see ru.xpressed.performanceattendancecoursework.controller.UsersController
 * @see Role
 * @see Attendance
 * @see Performance
 * @see ru.xpressed.performanceattendancecoursework.security.SecurityUserDetailsService
 */
@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Email(regexp = "^[a-zA-Z\\.\\-\\_]*[@][a-zA-Z]*[\\.].[a-zA-Z]*", message = "E-Mail must be valid!")
    @Size(max = 40, message = "E-Mail must be less than 40 symbols!")
    @NonNull
    private String username;

    @NonNull
    private String password;

    @Transient
    private String repeatedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull
    private List<Role> roles;

    @NonNull
    @Size(min = 1, message = "Surname must not be empty!")
    @Size(max = 20, message = "Surname must be less tan 15 symbols!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*", message = "Surname must contain only letters!")
    private String surname;

    @NonNull
    @Size(min = 1, message = "Name must not be empty!")
    @Size(max = 15, message = "Surname must be less tan 15 symbols!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*", message = "Name must contain only letters!")
    private String name;

    @NonNull
    @Size(max = 20, message = "Patronymic must be less than 15 symbols!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*", message = "Patronymic must contain only letters!")
    private String patronymic;

    @Size(max = 10, message = "Group Name must be less than 15 symbols!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\-]*", message = "Group must contain only letters and numbers!")
    private String groupName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Performance> performances;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Attendance> attendances;

    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !roles.contains(Role.ROLE_DEFAULT);
    }
}
