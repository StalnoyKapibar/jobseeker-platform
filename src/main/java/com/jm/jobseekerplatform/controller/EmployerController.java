package com.jm.jobseekerplatform.controller;

import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.model.Vacancy;
import com.jm.jobseekerplatform.model.profiles.EmployerProfile;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.EmployerUser;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.model.users.User;
import com.jm.jobseekerplatform.service.impl.VacancyService;
import com.jm.jobseekerplatform.service.impl.chats.ChatWithTopicService;
import com.jm.jobseekerplatform.service.impl.profiles.EmployerProfileService;
import com.jm.jobseekerplatform.service.impl.profiles.SeekerProfileService;
import com.jm.jobseekerplatform.service.impl.users.EmployerUserService;
import com.jm.jobseekerplatform.service.impl.users.SeekerUserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class EmployerController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @Autowired
    private EmployerUserService employerUserService;

    @Autowired
    private SeekerUserService seekerUserService;

    @Autowired
    private SeekerProfileService seekerProfileService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private ChatWithTopicService chatWithTopicService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;


    @GetMapping("/employer/{employerProfileId}")
    public String employerProfilePage(@PathVariable Long employerProfileId, Model model, Authentication authentication) {
        boolean isOwner = false;
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);

        model.addAttribute("employerProfile", employerProfile);
        Set<Vacancy> vacancies = vacancyService.getAllByEmployerProfileId(employerProfile.getId());
        model.addAttribute("vacancies", vacancies);
        model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));

        if (authentication != null && authentication.isAuthenticated()) {
            Long userId = ((User) authentication.getPrincipal()).getId();
            Set<String> roles = authentication.getAuthorities().stream().map(grantedAuthority -> (grantedAuthority).getAuthority()).collect(Collectors.toSet());
            if (roles.contains("ROLE_EMPLOYER")) {
                EmployerUser employerUser = employerUserService.getById(userId);
                isOwner = employerUser.getProfile().getId().equals(employerProfileId);
            }
            if (roles.contains("ROLE_SEEKER") || roles.contains("ROLE_ADMIN")) {
                if (roles.contains("ROLE_SEEKER")) {
                    boolean isContain = false;
                    SeekerUser seekerUser = seekerUserService.getById(userId);
                    if (employerProfile.getReviews().stream().anyMatch(reviews1 -> Objects.equals(
                            ((SeekerProfile)Hibernate.unproxy(reviews1.getCreatorProfile())).getId(),
                            seekerUser.getProfile().getId()))) {
                        isContain = true;
                    }
                    model.addAttribute("seekerProfileId", seekerUser.getProfile().getId());
                    model.addAttribute("isContain", isContain);
                }
            }
        }

        if (!employerProfile.getReviews().isEmpty()) {
            Set<EmployerReviews> reviews = employerProfile.getReviews();
            reviews.forEach(item -> item.setCreatorProfile((SeekerProfile) Hibernate.unproxy(item.getCreatorProfile())));
            model.addAttribute("employerProfileReviews", reviews);
            model.addAttribute("reviewStatus", true);
            model.addAttribute("averageRating", employerProfile.getAverageRating());
        } else {
            model.addAttribute("reviewStatus", false);
        }

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("employerUser", employerUserService.getByProfileId(employerProfileId));
        }

        model.addAttribute("isOwner", isOwner);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "employer";
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @GetMapping("/employer/get_news")
    public String getEmployerProfileNews(Model model, Authentication authentication) {
        model.addAttribute("employerProfileId", ((User) authentication.getPrincipal()).getProfile().getId());
        return "employer_all_news";
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @GetMapping("/employer/get_resumes")
    public String getResumes(Model model, Authentication authentication) {
        model.addAttribute("employerProfileId", ((User) authentication.getPrincipal()).getProfile().getId());
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "resume/all_resumes";
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @GetMapping("/employer/get_chats")
    public String EmployerPageChatsMy(Model model, Authentication authentication) {
        Long memberId = ((User) authentication.getPrincipal()).getId();
        Long employerProfileId = ((User) authentication.getPrincipal()).getProfile().getId();
        model.addAttribute("employerProfileId", employerProfileId);
        model.addAttribute("chats", chatWithTopicService.getAllChatsByMemberProfileId(memberId));
        return "employer_chats_my";
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @RequestMapping("/employer/update/{employerProfileId}")
    public String getEmployerProfileUpdatePage(@PathVariable Long employerProfileId, Model model,
                                               Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getProfile().getId();
        EmployerProfile employerProfile = employerProfileService.getById(employerProfileId);
        if (employerProfile.getId().equals(employerProfileId)) {
            model.addAttribute("employerProfile", employerProfile);
            Set<Vacancy> vacancies = vacancyService.getAllByEmployerProfileId(employerProfile.getId());
            model.addAttribute("vacancies", vacancies);
            model.addAttribute("logoimg", Base64.getEncoder().encodeToString(employerProfile.getLogo()));
            return "update_employer_profile";
        } else {
            model.addAttribute("status", "403");
            return "error";
        }
    }

    @RolesAllowed({"ROLE_EMPLOYER"})
    @GetMapping("/resumes/{seekerProfileId}")
    public String seekerResumesPageForEmployer(@PathVariable Long seekerProfileId, Model model) {
        SeekerProfile seekerProfile = seekerProfileService.getById(seekerProfileId);
        if (seekerProfile != null) {
            model.addAttribute("seekerProfileId", seekerProfile.getId());
            model.addAttribute("resumesList", seekerProfile.getResumes());
            model.addAttribute("googleMapsApiKey", googleMapsApiKey);
            return "resumes";
        }
        model.addAttribute("status", "404");
        return "error";
    }
}
