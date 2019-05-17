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

import java.util.*;
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
    private UserRoleService userRoleService;

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private TagService tagService;

    private UserRole roleSeeker = new UserRole("ROLE_SEEKER");

    @RequestMapping("/")
    public String mainPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<Tag, List<Vacancy>> mapVacancy = vacancyService.getMapByTags(new HashSet<>(tagService.getAll()), 10).entrySet().stream()
                    .filter(tagListEntry -> !tagListEntry.getValue().isEmpty())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            model.addAttribute("vacMess", "Доступные вакансии:");
            model.addAttribute("mapVacancies",mapVacancy);
        } else {
            if (authentication.getAuthorities().contains(roleSeeker)) {
                Set<Tag> tags = ((Seeker) authentication.getPrincipal()).getSeekerProfile().getTags();
                Map<Tag, List<Vacancy>> mapVacancy = vacancyService.getMapByTags(tags, 10).entrySet().stream()
                        .filter(tagListEntry -> !tagListEntry.getValue().isEmpty())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                model.addAttribute("vacMess", "Вакансии с учетом Вашего опыта:");
                model.addAttribute("mapVacancies", mapVacancy);
            }
        }
        return "index";
    }

    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model, Authentication authentication) {
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        model.addAttribute("eprofile", employerProfile);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));
        if (authentication != null && authentication.isAuthenticated()) {
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> ((GrantedAuthority) grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_SEEKER") | roles.contains("ROLE_ADMIN")) {
                Long id = ((User) authentication.getPrincipal()).getId();
                if (roles.contains("ROLE_SEEKER")) {
                    Seeker seeker = (Seeker) seekerService.getById(id);
                    model.addAttribute("seekerId", seeker.getSeekerProfile().getId());
                }
            }
            if (!employerProfile.getReviews().isEmpty()) {
                Set<EmployerReviews> employerReviews = employerProfile.getReviews();
                if (employerReviews.size() < 2) {
                    model.addAttribute("minReviewsEvaluation", employerReviews.iterator().next());
                } else if (employerReviews.size() >= 2) {
                    model.addAttribute("minReviewsEvaluation", employerReviews.stream().sorted().skip(employerReviews.size() - 1).findFirst().orElse(null));
                    model.addAttribute("maxReviewsEvaluation", employerReviews.stream().sorted().findFirst().orElse(null));
                } else {
                    model.addAttribute("reviewStatus", false);
                }
            } else {
                model.addAttribute("reviewStatus", false);
            }
        }
        return "employer";
    }

    @RequestMapping("/seeker/{seekerProfileId}")
    public String seekerProfilePage(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        model.addAttribute("sprofile", seekerProfile);
        model.addAttribute("photoimg", Base64.getEncoder().encodeToString(seekerProfile.getPhoto()));
        return "seeker";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
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