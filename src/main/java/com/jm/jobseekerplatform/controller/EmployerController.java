package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.model.profiles.ProfileEmployer;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EmployerController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private SeekerService seekerService;

    @Autowired
    private VacancyService vacancyService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;


    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model, Authentication authentication) {
        boolean isOwner = false;
        ProfileEmployer profileEmployer = employerProfileService.getById(employerProfileId);

        model.addAttribute("eprofile", profileEmployer);

        Set<Vacancy> vacancies = vacancyService.getAllByEmployerProfileId(profileEmployer.getId());
        model.addAttribute("vacancies", vacancies);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(profileEmployer.getLogo()));

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = ((User) authentication.getPrincipal()).getId();
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> ((GrantedAuthority) grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_EMPLOYER")) {
                EmployerUser employerUser = employerService.getById(userId);
                isOwner = employerUser.getProfile().getId().equals(employerProfileId);
            }
            if (roles.contains("ROLE_SEEKER") | roles.contains("ROLE_ADMIN")) {
                if (roles.contains("ROLE_SEEKER")) {
                    SeekerUser seekerUser = seekerService.getById(userId);
                    model.addAttribute("seekerProfileId", seekerUser.getProfile().getId());
                }
            }
            if (!profileEmployer.getReviews().isEmpty()) {
                Set<EmployerReviews> employerReviews = profileEmployer.getReviews();
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

        model.addAttribute("isOwner", isOwner);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "employer";
    }
}
