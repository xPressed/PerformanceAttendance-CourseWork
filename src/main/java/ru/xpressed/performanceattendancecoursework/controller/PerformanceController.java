/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.xpressed.performanceattendancecoursework.domain.Performance;
import ru.xpressed.performanceattendancecoursework.domain.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.PerformanceRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller to work with performance table.
 *
 * @see UserRepository
 * @see User
 * @see PerformanceRepository
 * @see Performance
 * @see Role
 */
@Controller
public class PerformanceController {
    private UserRepository userRepository;

    private PerformanceRepository performanceRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPerformanceRepository(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    /**
     * !!! INDEX MAPPING !!!
     * Method for GET REQUEST always redirects to users (teacher/admin) or performance (student) table.
     *
     * @param authentication to check for permission to view others' tables
     * @return redirect to other page
     */
    @GetMapping({"/index", "/", "/home"})
    public String redirectIndex(Authentication authentication) {
        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            return "redirect:/users";
        }
        return "redirect:/performance?username=" + authentication.getName();
    }

    /**
     * Method for GET REQUEST to show performance table.
     *
     * @param authentication to check for permission
     * @param model          to build template
     * @param username       to choose user (student -> himself)
     * @param add            to check for add page
     * @param update         to check for update page
     * @param id             for deletion
     * @return the redirect or template
     */
    @GetMapping("/performance")
    public String showPerformanceTable(Authentication authentication, Model model,
                                       @RequestParam("username") String username,
                                       @RequestParam("add") Optional<String> add,
                                       @RequestParam("update") Optional<String> update,
                                       @RequestParam("delete") Optional<Integer> id,
                                       @RequestParam("account") Optional<String> account) {
        if (!Objects.equals(username, authentication.getName()) && authentication.getAuthorities().contains(Role.ROLE_STUDENT)) {
            return "redirect:/performance?username=" + authentication.getName();
        }

        //Build template
        model.addAttribute("username", authentication.getName());

        if (authentication.getAuthorities().contains(Role.ROLE_STUDENT)) {
            model.addAttribute("navHref", "/performance?username=" + authentication.getName());
            model.addAttribute("navText", "Tables");
            model.addAttribute("actions", false);
            model.addAttribute("owner", authentication.getName());
        } else {
            model.addAttribute("navHref", "/users");
            model.addAttribute("navText", "Users");
            model.addAttribute("actions", true);
            model.addAttribute("owner", username);

            //Check for deletion
            if (id.isPresent()) {
                //Delete foreign key from discipline
                Performance performance = performanceRepository.findById(id.orElse(null)).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

                performance.setUser(null);
                performanceRepository.save(performance);

                //Delete discipline
                performanceRepository.deleteById(id.orElse(null));
            }
        }

        //Check for overflow page
        if (update.isPresent() || add.isPresent() || account.isPresent()) {
            model.addAttribute("overflow", "hidden");
            model.addAttribute("blur", "5px");
        } else {
            model.addAttribute("overflow", "visible");
            model.addAttribute("blur", "0");
        }

        //Table data
        model.addAttribute("rows", Objects.requireNonNull(userRepository.findById(username).orElse(null)).getPerformances());
        return "performance/main";
    }

    /**
     * Method for GET REQUEST to add performance record.
     *
     * @param model          to build template
     * @param username       to choose user
     * @return the template or redirect
     */
    @GetMapping("/performance/add")
    public String getAddPerformanceRecord(Model model, @RequestParam("username") String username) {
        model.addAttribute("performance", new Performance());
        return "performance/add";
    }

    /**
     * Method for POST REQUEST to validate and save new performance record.
     *
     * @param username       to choose user
     * @param performance     data to validate and save
     * @param bindingResult  to validate
     * @param model          to return errors
     * @return the template or redirect
     */
    @PostMapping("/performance/add")
    public String postAddPerformanceRecord(@RequestParam("username") String username, @Valid Performance performance,
                                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("performance", performance);
            return "performance/add";
        }

        User user = userRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        performance.setUser(user);
        user.getPerformances().add(performance);
        userRepository.save(user);
        return "performance/add";
    }

    /**
     * Method for GET REQUEST to update the performance record.
     *
     * @param id             to choose the record
     * @param model          to build template
     * @return the template or redirect
     */
    @GetMapping("/performance/update")
    public String getUpdatePerformanceRecord(@RequestParam("id") Integer id, Model model) {
        Performance performance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        model.addAttribute("performance", performance);
        return "performance/update";
    }

    /**
     * Method for POST REQUEST to validate and save updated performance record.
     *
     * @param id             to choose the record
     * @param newPerformance  new updated data
     * @param bindingResult  to validate
     * @param model          to return errors
     * @return the template or redirect
     */
    @PostMapping("/performance/update")
    public String postUpdatePerformanceRecord(@RequestParam("id") Integer id, @Valid Performance newPerformance, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("performance", newPerformance);
            return "performance/update";
        }

        Performance oldPerformance = performanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        performanceRepository.save(oldPerformance.toBuilder().name(newPerformance.getName()).mark(newPerformance.getMark()).year(newPerformance.getYear()).build());
        return "performance/update";
    }
}
