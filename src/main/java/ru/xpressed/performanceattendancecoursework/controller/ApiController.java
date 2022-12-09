/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.domain.User;
import ru.xpressed.performanceattendancecoursework.domain.dto.ExceptionDTO;
import ru.xpressed.performanceattendancecoursework.domain.dto.UserDTO;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;
import ru.xpressed.performanceattendancecoursework.service.RequesterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    private UserRepository userRepository;
    private RequesterService requesterService;

    @ExceptionHandler(ResponseStatusException.class)
    public ExceptionDTO restError(ResponseStatusException e) {
        return new ExceptionDTO(e.getRawStatusCode(), e.getMessage());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRequesterService(RequesterService requesterService) {
        this.requesterService = requesterService;
    }

    @GetMapping("/user")
    public UserDTO getUserById(@RequestParam("username") String username,
                               @RequestParam("token") String token) {
        requesterService.checkRequester(token);

        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return new UserDTO(user.getUsername(), user.getRoles().get(0), user.getSurname(), user.getName(), user.getPatronymic(), user.getGroupName());
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers(@RequestParam("token") String token,
                                  @RequestParam("surname") Optional<String> surname,
                                  @RequestParam("name") Optional<String> name,
                                  @RequestParam("patronymic") Optional<String> patronymic,
                                  @RequestParam("groupName") Optional<String> groupName) {
        requesterService.checkRequester(token);

        List<User> userList = userRepository.findByParams(surname.orElse(null), name.orElse(null), patronymic.orElse(null), groupName.orElse(null));
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            userDTOList.add(new UserDTO(user.getUsername(), user.getRoles().get(0), user.getSurname(), user.getName(), user.getPatronymic(), user.getGroupName()));
        }
        return userDTOList;
    }
}
