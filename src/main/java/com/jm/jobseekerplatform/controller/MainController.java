package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private EmployerService employerService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");

    @RequestMapping("/")
    public String mainPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            List<Vacancy> vacancies = vacancyService.getAllWithLimit(10);
            model.addAttribute("vacMess", "Доступные вакансии:");
            model.addAttribute("vacancies", vacancies);
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                try {
                    Set<Tag> tags = ((Seeker) authentication.getPrincipal()).getSeekerProfile().getTags();
                    Set<Vacancy> vacancies = vacancyService.getByTags(tags, 10);
                    model.addAttribute("vacMess", "Вакансии с учетом Вашего опыта:");
                    model.addAttribute("vacancies", vacancies);
                } catch (NullPointerException e) {
                    List<Vacancy> vacancies = vacancyService.getAllWithLimit(10);
                    model.addAttribute("vacMess", "Доступные вакансии: (Создайте свой профиль, чтобы увидеть вакансии с учетом Вашего опыта)");
                    model.addAttribute("vacancies", vacancies);
                }
            }
        }
        return "index";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @RequestMapping("/admin/vacancies")
    public String adminPageVacancies() {
        return "admin_vacancies";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String reg() {
        return "registration";
    }

    @RequestMapping(value = "/confirm_reg/{token}", method = RequestMethod.GET)
    public String confirmRegistration(@PathVariable String token, Model model) {
        try {
            VerificationToken verificationToken = verificationTokenService.findByToken(token);
            boolean complete = verificationTokenService.tokenIsNonExpired(verificationToken);
            model.addAttribute("complete", complete);
            if (complete) {
                verificationTokenService.completeRegistration(verificationToken);
            }
        } catch (NoResultException e) {
            e.printStackTrace();
            model.addAttribute("complete", false);
        } finally {
            return "confirm_reg";
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String filterProfilePage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Long id = ((User) authentication.getPrincipal()).getId();
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> ((GrantedAuthority) grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_EMPLOYER")) {
                Employer employer = (Employer) employerService.getById(id);
                return "redirect:/employer/" + employer.getEmployerProfile().getId();
            } else if (roles.contains("ROLE_SEEKER")) {
                Seeker seeker = (Seeker) seekerService.getById(id);
                return "redirect:/seeker/" + seeker.getSeekerProfile().getId();
            } else if (roles.contains("ROLE_ADMIN")) {
                return "redirect:/admin";
            }
        }
        return "index";
    }
}
