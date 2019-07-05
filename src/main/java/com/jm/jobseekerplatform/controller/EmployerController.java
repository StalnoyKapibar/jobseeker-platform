package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.impl.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.EmployerService;
import com.jm.jobseekerplatform.service.impl.SeekerService;
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

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;


    @RequestMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model, Authentication authentication) {
        boolean isOwner = false;
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        model.addAttribute("eprofile", employerProfile);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = ((User) authentication.getPrincipal()).getId();
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> ((GrantedAuthority) grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_EMPLOYER")) {
                Employer employer = (Employer) employerService.getById(userId);
                isOwner = employer.getProfile().getId().equals(employerProfileId);
            }
            if (roles.contains("ROLE_SEEKER") | roles.contains("ROLE_ADMIN")) {
                if (roles.contains("ROLE_SEEKER")) {
                    Seeker seeker = (Seeker) seekerService.getById(userId);
                    model.addAttribute("seekerId", seeker.getProfile().getId());
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
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "employer";
    }
}
