/*
 * Copyright (c) 2002 - 2022. xPressed Inc.
 * Maxim Zvyagincev.
 * All rights not reserved.
 */

package ru.xpressed.performanceattendancecoursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.xpressed.performanceattendancecoursework.entity.Discipline;
import ru.xpressed.performanceattendancecoursework.entity.User;
import ru.xpressed.performanceattendancecoursework.enumerate.Role;
import ru.xpressed.performanceattendancecoursework.repository.DisciplineRepository;
import ru.xpressed.performanceattendancecoursework.repository.UserRepository;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller to work with performance table.
 *
 * @see UserRepository
 * @see User
 * @see DisciplineRepository
 * @see Discipline
 * @see Role
 */
@Controller
public class PerformanceController {
    private UserRepository userRepository;

    private DisciplineRepository disciplineRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setDisciplineRepository(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
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
    public String showPerformanceTable(Authentication authentication, Model model, @RequestParam("username") String username, @RequestParam("add") Optional<String> add, @RequestParam("update") Optional<String> update, @RequestParam("delete") Optional<Integer> id) {
        if (!Objects.equals(username, authentication.getName()) && authentication.getAuthorities().contains(Role.ROLE_STUDENT)) {
            return "redirect:/performance?username=" + authentication.getName();
        }

        //Build tempalte
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

            //Check for add or update
            if (update.isPresent() || add.isPresent()) {
                model.addAttribute("overflow", "hidden");
                model.addAttribute("blur", "5px");
            } else {
                model.addAttribute("overflow", "visible");
                model.addAttribute("blur", "0");
            }

            //Check for deletion
            if (id.isPresent()) {
                //Delete foreign key from discipline
                Discipline discipline = disciplineRepository.findById(id.orElse(null)).orElse(null);
                assert discipline != null;
                discipline.setUser(null);
                disciplineRepository.save(discipline);

                //Delete discipline
                disciplineRepository.deleteById(id.orElse(null));
            }
        }

        //Table data
        model.addAttribute("rows", Objects.requireNonNull(userRepository.findById(username).orElse(null)).getDisciplines());
        return "performance/main";
    }

    /**
     * Method for GET REQUEST to add performance record.
     *
     * @param authentication to check for permission
     * @param model          to build template
     * @param username       to choose user
     * @return the template or redirect
     */
    @GetMapping("/performance/add")
    public String getAddPerformanceRecord(Authentication authentication, Model model, @RequestParam("username") String username) {
        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("discipline", new Discipline());
            return "performance/add";
        }
        return "redirect:/performance?username=" + authentication.getName();
    }

    /**
     * Method for POST REQUEST to validate and save new performance record.
     *
     * @param username       to choose user
     * @param discipline     data to validate and save
     * @param bindingResult  to validate
     * @param model          to return errors
     * @param authentication to check for permission
     * @return the template or redirect
     */
    @PostMapping("/performance/add")
    public String postAddPerformanceRecord(@RequestParam("username") String username, @Valid Discipline discipline, BindingResult bindingResult, Model model, Authentication authentication) {
        if (!(authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN))) {
            return "redirect:/performance?username=" + authentication.getName();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("discipline", discipline);
            return "performance/add";
        }

        User user = userRepository.findById(username).orElse(null);
        assert user != null;
        discipline.setUser(user);
        user.getDisciplines().add(discipline);
        userRepository.save(user);
        return "performance/add";
    }

    /**
     * Method for GET REQUEST to update the performance record.
     *
     * @param id             to choose the record
     * @param model          to build template
     * @param authentication to check for rights
     * @return the template or reridrect
     */
    @GetMapping("/performance/update")
    public String getUpdatePerformanceRecord(@RequestParam("id") Integer id, Model model, Authentication authentication) {
        if (authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN)) {
            Discipline discipline = disciplineRepository.findById(id).orElse(null);
            model.addAttribute("discipline", discipline);
            return "performance/update";
        }
        return "redirect:/performance?username=" + authentication.getName();
    }

    /**
     * Method for POST REQUEST to validate and save updated performance record.
     *
     * @param id             to choose the record
     * @param newDiscipline  new updated data
     * @param bindingResult  to validate
     * @param model          to return errors
     * @param authentication to check for rights
     * @return the template or redirect
     */
    @PostMapping("/performance/update")
    public String postUpdatePerformanceRecord(@RequestParam("id") Integer id, @Valid Discipline newDiscipline, BindingResult bindingResult, Model model, Authentication authentication) {
        if (!(authentication.getAuthorities().contains(Role.ROLE_TEACHER) || authentication.getAuthorities().contains(Role.ROLE_ADMIN))) {
            return "redirect:/performance?username=" + authentication.getName();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("discipline", newDiscipline);
            return "performance/update";
        }

        Discipline oldDiscipline = disciplineRepository.findById(id).orElse(null);
        assert oldDiscipline != null;
        disciplineRepository.save(oldDiscipline.toBuilder().name(newDiscipline.getName()).mark(newDiscipline.getMark()).year(newDiscipline.getYear()).build());
        return "performance/update";
    }
}
