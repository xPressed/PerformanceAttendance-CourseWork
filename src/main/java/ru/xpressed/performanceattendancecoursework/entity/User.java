/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {
    /**
     * ID of any user in database and application.
     */
    @Id
    @Size(min = 3, message = "Username must be at least 3 symbols!")
    @Size(max = 10, message = "Username must be less than 10 symbols!")
    @Pattern(regexp = "^[a-zA-Z0-9]*", message = "Username must contain only letters an numbers!")
    @NonNull
    private String username;

    /**
     * Password of any user in database stored encrypted.
     */
    @NonNull
    private String password;

    /**
     * Repeated Password field is not stored.
     * Is used to confirm password during registration.
     */
    @Transient
    private String repeatedPassword;

    /**
     * List of Roles appended to user.
     *
     * @see Role
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull
    private List<Role> roles = new ArrayList<>();

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
    @Size(max = 20, message = "Patronymic must be less tan 15 symbols!")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*", message = "Patronymic must contain only letters!")
    private String patronymic;

    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9]*", message = "Group must contain only letters and numbers!")
    private String groupName;

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
